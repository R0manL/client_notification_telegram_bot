package org.grow.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.grow.csv.pojo.AppointmentCSV;
import org.grow.db.pojo.AppointmentDB;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.api.services.calendar.CalendarScopes.CALENDAR_READONLY;
import static org.grow.EnvConfig.ENV_CONFIG;
import static org.grow.db.service.ClientDBService.getChatIdForClientThatContains;
import static org.grow.db.service.TherapistDBService.*;
import static org.grow.service.GoogleAuthService.*;
import static org.grow.utils.DateTimeUtils.*;
import static org.grow.utils.FileUtils.getAbsolutePathForFileInResourceDir;

@Log4j2
public class GoogleCalendarService {
    private static final URL CREDENTIALS_FILE_PATH = getAbsolutePathForFileInResourceDir(ENV_CONFIG.googleCredentials());
    private static final String APPLICATION_NAME = "Calendar events reader";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String EVENT_SUMMARY_FORMAT_REGEX = "^([\\u0404-\\u0491]+)\\s\\(([\\u0404-\\u0491-\\u0404-\\u0491']+)\\s(\\+?\\d{10,16}?|\\d{3}\\s\\d{3}\\s\\d{4})\\)";

    private final Calendar calendarService;


    public GoogleCalendarService() {
        NetHttpTransport httpTransport = null;

        GoogleCredentials credentials = readCredentialsFrom(CREDENTIALS_FILE_PATH, CALENDAR_READONLY);
        Objects.requireNonNull(credentials, "Goggle credentials can't by null.");

        // Create a HttpRequestInitializer, which will provide authenticated access.
        HttpCredentialsAdapter credentialsAdapter = new HttpCredentialsAdapter(credentials);
        // Create the transport.
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            Objects.requireNonNull(httpTransport, "Google http transport can't be null.");
        } catch (GeneralSecurityException | IOException e) {
            log.error("Can't create a new trusted connection to google calendar. Error: " + e.getMessage());
        }

        this.calendarService = new Calendar
                .Builder(httpTransport, JSON_FACTORY, credentialsAdapter)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public List<AppointmentDB> getCalendarEventsOnLemkivska() {
        return getAppointmentsForTomorrowFrom(ENV_CONFIG.calendarsIDsLemkivska());
    }

    public List<AppointmentDB> getCalendarEventsOnStryiska() {
        return getAppointmentsForTomorrowFrom(ENV_CONFIG.calendarIDsStryiska());
    }

    public List<AppointmentDB> getCalendarEventsForDebug() {
        return getAppointmentsForTomorrowFrom(ENV_CONFIG.calendarIDsForDebug());
    }

    public List<AppointmentDB> getAppointmentsForTomorrowFrom(List<String> calendarIDs) {
        return calendarIDs
                .stream()
                .map(calendarID -> new GoogleCalendarService().buildRemindersFromEventsForTomorrow(calendarID))
                .flatMap(List::stream)
                .toList();
    }

    public List<AppointmentDB> buildRemindersFromEventsForTomorrow(@NotNull String calendarID) {
        List<AppointmentDB> result = new ArrayList<>();

        try {
            String calendarName = getCalendarNameBy(calendarID);

            getEventsForTomorrow(calendarID)
                    .forEach(event -> {
                AppointmentDB appointment = convertEventToAppointmentDB(event, calendarName);
                if (appointment != null) {
                    result.add(appointment);
                } else {
                    log.error("Can't build appointment from event: '{}'", event.getSummary());
                }
            });
        } catch (IOException e) {
            log.error("Can't get calendar's name or events. Check if calendar's permission setup properly.\nDetails:" + e.getMessage());
        }

        return result;
    }

    @Nullable
    public AppointmentDB convertEventToAppointmentDB(Event event, @NotNull String calendarName) {
        String summary = event.getSummary();
        log.info("Parse summary: {}", summary);

        AppointmentDB result = null;
        Matcher matcher = Pattern.compile(EVENT_SUMMARY_FORMAT_REGEX).matcher(summary);

        if (matcher.find()) {
            String therapistLastName = matcher.group(1);
            String clientPhoneNumber = matcher.group(3).replaceAll("\\D", "");

            Optional<Integer> therapistID = getTherapistIdBy(therapistLastName);
            Optional<Long> chatID = getChatIdForClientThatContains(clientPhoneNumber);

            if (therapistID.isPresent() && chatID.isPresent()) {
                DateTime eventStart = event.getStart().getDateTime();
                Timestamp appointmentStart = toTimestamp(eventStart);

                result = AppointmentDB.builder()
                        .room(calendarName)
                        .eventStart(appointmentStart)
                        .chatId(chatID.get())
                        .therapistId(therapistID.get())
                        .build();
            } else {
                if (therapistID.isEmpty()) {
                    log.warn("No therapist with last name: '{}' has been found.", therapistLastName);
                }
                if (chatID.isEmpty()) {
                    log.warn("Bot hasn't yet registered for phone number: '{}'", clientPhoneNumber);
                }
            }
        } else {
            log.warn("Invalid format of summary: {}", summary);
        }

        return result;
    }

    @Nullable
    public AppointmentCSV convertEventToAppointmentCSV(Event event, @NotNull String calendarName) {
        String summary = event.getSummary();
        AppointmentCSV result = null;

        if (summary != null) {
        log.info("Parse summary: {}", summary);

            String[] parts = summary.split("!");
            if (parts.length > 0) {
                Matcher matcher = Pattern.compile(EVENT_SUMMARY_FORMAT_REGEX).matcher(parts[0]);

                if (matcher.find()) {
                    DateTime eventStart = event.getStart().getDateTime();
                    Timestamp appointmentStart = toTimestamp(eventStart);

                    result = AppointmentCSV
                            .builder()
                            .room(calendarName)
                            .eventStart(appointmentStart.toString())
                            .therapist(matcher.group(1).trim())
                            .clientPhone(matcher.group(3).trim())
                            .bot(parts.length > 1 ? parts[1].trim() : "")
                            .isNew(parts.length > 2 ? parts[2].trim() : "")
                            .isOnline(parts.length > 3 ? parts[3].trim() : "")
                            .price(parts.length > 4 ? parts[4].trim() : "")
                            .hasPaid(parts.length > 5 ? parts[5].trim() : "")
                            .comeFrom(parts.length > 6 ? parts[6].trim() : "")
                            .comment(parts.length > 7 ? parts[7].trim() : "")
                            .build();
                } else {
                    log.warn("Invalid format of summary: {}", summary);
                }
            } else {
                log.warn("Invalid format of summary: {}", summary);
            }
        } else {
            log.warn("Unexpected, summary is null for event: {}", event.toString());
        }

        return result;
    }

    private List<Event> getEventsForTomorrow(@NotNull String calendarID) throws IOException {
        LocalDateTime startOfTheDayLocalDateTime = LocalDateTime.now().plusDays(1).with(LocalTime.MIN);
        LocalDateTime endOfTheDayLocalDateTime = LocalDateTime.now().plusDays(1).with(LocalTime.MAX);
        log.debug("Getting events in range: {}, {} from calendar: {}", startOfTheDayLocalDateTime, endOfTheDayLocalDateTime, calendarID);

        DateTime startOfTheDayGoogleDateTime = toGoogleDateTime(startOfTheDayLocalDateTime);
        DateTime endOfTheDayGoogleDateTime = toGoogleDateTime(endOfTheDayLocalDateTime);

        return getEventsFrom(calendarID, startOfTheDayGoogleDateTime, endOfTheDayGoogleDateTime);
    }

    public List<Event> getEvents(@NotNull String calendarID, LocalDateTime from, LocalDateTime to) throws IOException {
        DateTime startOfTheDayGoogleDateTime = toGoogleDateTime(from);
        DateTime endOfTheDayGoogleDateTime = toGoogleDateTime(to);

        return getEventsFrom(calendarID, startOfTheDayGoogleDateTime, endOfTheDayGoogleDateTime);
    }

    private List<Event> getEventsFrom(@NotNull String calendarID, DateTime from, DateTime to) throws IOException {
        log.info("Getting calendar: '{}' events, in range: {} - {}", calendarID, from, to);
        return this.calendarService
                .events()
                .list(calendarID)
                .setTimeMin(from)
                .setTimeMax(to)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .setMaxResults(2500)
                .execute()
                .getItems();
    }

    @NotNull
    public String getCalendarNameBy(@NotNull String calendarID)  throws IOException {
        log.debug("Get calendar's name by ID: '{}'", calendarID);
        return this.calendarService
                .events()
                .list(calendarID)
                .execute()
                .getSummary();
    }
}
