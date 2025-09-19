package org.grow.telegrambot;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.opencsv.CSVWriter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.grow.csv.pojo.AppointmentCSV;
import org.grow.db.pojo.AppointmentDB;
import org.grow.service.AppointmentService;
import org.grow.service.GoogleCalendarService;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.grow.EnvConfig.ENV_CONFIG;
import static org.grow.db.service.AppointmentDBService.*;
import static org.grow.telegrambot.PredPhoneNumbers.EXISTED_PHONE_NUMBER;
import static org.grow.db.service.ClientDBService.getPhoneNumbersForAllUserThatAre;
import static org.grow.utils.DateTimeUtils.toGoogleDateTime;
import static org.grow.utils.StringUtils.generateStringBasedOnCurrentDate;

@Log4j2
class CalendarTests {
    private static final TelegramClient telegramClient = new OkHttpTelegramClient(ENV_CONFIG.telegramToken());
    private static final String TEST_CALENDAR_NAME = "Test calendar1";


    @Test
    void verifySendingAppointmentsFromDebugCalendar() {
        new AppointmentService(telegramClient).sendNotificationsViaTelegramFrom(ENV_CONFIG.calendarIDsForDebug());
    }

    @Test
    void verifySendingAppointmentsFromStryiskaCalendar() {
        new AppointmentService(telegramClient).sendNotificationsViaTelegramFrom(ENV_CONFIG.calendarIDsStryiska());
    }

    @Test
    void verifyAllCalendarsConnectivity() {
        List<String> calendarIDs = ENV_CONFIG.calendarsIDsLemkivska();
        calendarIDs.addAll(ENV_CONFIG.calendarIDsStryiska());
        calendarIDs.addAll(ENV_CONFIG.calendarIDsForDebug());

        List<Event> allEvents = new ArrayList<>();
        for (String calendarID : calendarIDs) {
            try {
                LocalDateTime startOfTheDayLocalDateTime = LocalDateTime.now().plusDays(1).with(LocalTime.MIN);
                LocalDateTime endOfTheDayLocalDateTime = LocalDateTime.now().plusDays(1).with(LocalTime.MAX);

                List<Event> events = new GoogleCalendarService().getEvents(calendarID, startOfTheDayLocalDateTime, endOfTheDayLocalDateTime);
                log.debug("Got '{}' events.", events.size());
                allEvents.addAll(events);
            } catch (IOException e) {
                throw new IllegalStateException("Can't get events from calendar: '" + calendarID + "'.\n" + e.getMessage());
            }
        }

        assertThat(allEvents.size())
                .as("Unexpected number of events where received from all calendars. Expect at least one event to be present.")
                .isNotZero();
    }

    @Disabled("It's not a test, just method for reading data from calendars and store into csv file.")
    @Test
    void generateCalendarDataForAnalysisTest() {
        LocalDateTime startOfTheDayLocalDateTime = LocalDateTime.now().minusYears(1).plusDays(1).with(LocalTime.MIN);
        LocalDateTime endOfTheDayLocalDateTime = LocalDateTime.now().with(LocalTime.MAX);

        //ToDo currently test will read max 2500 records per calendar. Add reading in a loop (year by year) if we have more records.
        //Get events from all calendars
        List<String> calendarIDs = ENV_CONFIG.calendarsIDsLemkivska();
        calendarIDs.addAll(ENV_CONFIG.calendarIDsStryiska());

        List<AppointmentCSV> result = new ArrayList<>();
        GoogleCalendarService calService = new GoogleCalendarService();

        for (String calendarID : calendarIDs) {
                try {
                    String calName = calService.getCalendarNameBy(calendarID);
                    List<Event> calEvents = calService.getEvents(calendarID, startOfTheDayLocalDateTime, endOfTheDayLocalDateTime);

                    calEvents
                            .forEach(event -> {
                                AppointmentCSV appointment = calService.convertEventToAppointmentCSV(event, calName);
                                if (appointment != null) {
                                    result.add(appointment);
                                } else {
                                    log.error("Can't build appointment from event: '{}'", event.getSummary());
                                }
                            });
                } catch (IOException e) {
                    log.error("Can't get calendar's name or events. Check if calendar's permission setup properly.\nDetails:" + e.getMessage());
                }
        }

        String filePath = "data.csv";

        // Data to write into the CSV file
        String[] header = { "подія", "кабінет", "терапевт", "клієнт", "новий", "онлайн", "вартість", "оплата", "порадив", "коментар" };

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Write header
            writer.writeNext(header);

            result
                    .stream()
                    .forEach(event -> {
                List<String> record = new ArrayList<>();
                record.add(event.getEventStart());
                record.add(event.getRoom());
                record.add(event.getTherapist());
                record.add(event.getClientPhone());
                record.add(event.getIsNew());
                record.add(event.getIsOnline());
                record.add(event.getPrice());
                record.add(event.getHasPaid());
                record.add(event.getComeFrom());
                record.add(event.getComment());

                writer.writeNext(record.toArray(new String[0]));
            });

            System.out.println("CSV file created successfully at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"99999@group.calendar.google.com", "555@group.calendar.google.com"})
    void verifyCalendarWithoutProperPermissions(String calendarID) {
        List<AppointmentDB> actualEvents = new GoogleCalendarService().buildRemindersFromEventsForTomorrow(calendarID);
        assertThat(actualEvents).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Терапевт1 (Юрій 068 000 000) ВБ ! не новий ! очно ! не оплачено ! 0000 ! лист очікування ! ДДД (квитанція тут  097 000 0007)",
            "Терапевт1 (Юрій-Степан 068 000 0000) ВБ ! не новий ! очно ! не оплачено ! 0000 ! лист очікування ! ДДДД (квитанція тут  097 000 0007)",
            "Терапевт1 (Мар'яна 068 000 0000) ВБ ! не новий ! очно ! не оплачено ! 00000 ! лист очікування ! ДДД (квитанція тут  097 000 0007)",
            "Терапевт1 (Юрій 068000000) ВБ ! не новий ! очно ! не оплачено ! 0000 ! лист очікування ! ДДД (квитанція тут  097 000 0007)",
            "Терапевт2 (Олександр +4552900000) ТБ ! не новий ! онлайн ! 0000 ! оплачено ! родичі ! ППП (цуцуцуцу@gmail.com)"})
    void verifyCalendarWithValidSummary(String summary) {
        // Note. Test calendar must not have events for tomorrow.
        EventDateTime now = new EventDateTime().setDateTime(toGoogleDateTime(LocalDateTime.now()));
        Event event = new Event()
                .setSummary(summary)
                .setStart(now);

        AppointmentDB appointment = new GoogleCalendarService().convertEventToAppointmentDB(event, "Test calendar1");

        assertThat(appointment).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Терапевт3 (Юрій 67 000 0000) ВБ ! не новий", // Invalid phone num.
            "Терапевт3 ( 067 000 0000) ВБ ! не новий ! очно ", // Missed client's name
            "(Юрій 067 000 0000) ВБ ! не новий ! очно ", // Missed therapist's name
            "Терапевт3 (Юрій 000 000 0000) ВБ ! не новий ! очно ", // Missed client's phone number
            "Тест (Юрій 067 000 0000) ВБ ! не новий ! очно "}) // Missed therapist's name
    void verifyCalendarWithInvalidSummary(String summary) {
        // Test calendar. Expects that there are no events for tomorrow.
        Event event = new Event().setSummary(summary);
        AppointmentDB appointment = new GoogleCalendarService().convertEventToAppointmentDB(event, TEST_CALENDAR_NAME);

        assertThat(appointment).isNull();
    }

    private static Stream<String> validPhoneNumbersFromDB() {
        List<String> params = getPhoneNumbersForAllUserThatAre(true);
        return params.stream();
    }

    @ParameterizedTest
    @MethodSource("validPhoneNumbersFromDB")
    void verifyValidClientPhoneNumbersFromDB(@NotNull String phoneNumber) {
        String summary = String.format("Терапевт1 (Юрій %s) ВБ ! не новий", phoneNumber);
        EventDateTime now = new EventDateTime().setDateTime(toGoogleDateTime(LocalDateTime.now()));

        Event event = new Event()
                .setSummary(summary)
                .setStart(now);

        AppointmentDB appointment = new GoogleCalendarService().convertEventToAppointmentDB(event, TEST_CALENDAR_NAME);
        assertThat(appointment).isNotNull();
    }

    private static Stream<String> inActiveUsersPhoneNumbersFromDB() {
        List<String> params = getPhoneNumbersForAllUserThatAre(false);
        return params.stream();
    }

    @ParameterizedTest
    @MethodSource("inActiveUsersPhoneNumbersFromDB")
    void verifyInactiveClientPhoneNumbersFromDB(@NotNull String phoneNumber) {
        String summary = String.format("Терапевт1 (Юрій %s) ВБ ! не новий", phoneNumber);

        EventDateTime now = new EventDateTime().setDateTime(toGoogleDateTime(LocalDateTime.now()));
        Event event = new Event()
                .setSummary(summary)
                .setStart(now);

        AppointmentDB appointment = new GoogleCalendarService().convertEventToAppointmentDB(event, TEST_CALENDAR_NAME);
        assertThat(appointment).isNotNull();
    }

    @Test
    void verifyAppointmentDBDefaultValues() {
        String summary = String.format("Терапевт1 (Юрій %s) ВБ ! не новий", EXISTED_PHONE_NUMBER);
        LocalDateTime now = LocalDateTime.now();
        EventDateTime eventDateTimeNow = new EventDateTime().setDateTime(toGoogleDateTime(now));
        Event event = new Event()
                .setSummary(summary)
                .setStart(eventDateTimeNow);

        String randomRoomName = generateStringBasedOnCurrentDate();

        AppointmentDB originalAppointment = new GoogleCalendarService().convertEventToAppointmentDB(event, randomRoomName);
        insertAppointment(originalAppointment);

        List<AppointmentDB> actualAppointments = getAppointmentsBy(randomRoomName);
        assertThat(actualAppointments).as("Expect single appointment will be inserted into DB.").hasSize(1);

        AppointmentDB actualAppointment = actualAppointments.get(0);
        assertThat(actualAppointment.getRoom()).isEqualTo(randomRoomName);
        assertThat(actualAppointment.getStatus()).isNull();
        assertThat(actualAppointment.getChatId()).isNotZero();
        assertThat(actualAppointment.getImageId()).isNull();
        assertThat(actualAppointment.getTherapistId()).isNotZero();
        assertThat(actualAppointment.getUpdateDate()).isAfterOrEqualTo(Timestamp.valueOf(now));
        assertThat(actualAppointment.getCreateDate()).isAfterOrEqualTo(Timestamp.valueOf(now));
        assertThat(actualAppointment.getEventStart()).isEqualToIgnoringMillis(Timestamp.valueOf(now));

        deleteAppointmentWithRoomNameContains(randomRoomName);
    }

    @Test
    void extractDataFromEvents() {
        List<String> calendarIDs = ENV_CONFIG.calendarIDsStryiska();
        LocalDateTime startOfTheDayLocalDateTime = LocalDateTime.now().minusYears(1).with(LocalTime.MIN);
        LocalDateTime endOfTheDayLocalDateTime = LocalDateTime.now().with(LocalTime.MIN);
        String calendarID = calendarIDs.get(0);
        GoogleCalendarService googleCalendarService = new GoogleCalendarService();

        List<AppointmentDB> appointments = new ArrayList<>();

        try {
            String calendarName = googleCalendarService.getCalendarNameBy(calendarID);
            List<Event> events = googleCalendarService.getEvents(calendarID, startOfTheDayLocalDateTime, endOfTheDayLocalDateTime);
            events.forEach(event -> {
                appointments.add(googleCalendarService.convertEventToAppointmentDB(event, calendarName));
            });
        } catch (IOException e) {
            throw new IllegalStateException("Can't get events from calendar: '" + calendarID + "'.\n" + e.getMessage());
        }
    }
}
