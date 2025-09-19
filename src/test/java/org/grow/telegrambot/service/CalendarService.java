package org.grow.telegrambot.service;

import com.google.api.services.calendar.model.Event;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.grow.db.pojo.AppointmentDB;
import org.grow.service.GoogleCalendarService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.grow.EnvConfig.ENV_CONFIG;

@Log4j2
class CalendarService {

    @Disabled
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
