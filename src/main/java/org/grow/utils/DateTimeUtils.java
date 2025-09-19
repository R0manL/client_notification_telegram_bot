package org.grow.utils;

import com.google.api.client.util.DateTime;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import static org.grow.telegrambot.components.Messages.TIME_EMOJI;


@Log4j2
public class DateTimeUtils {
    private static final String DEFAULT_TIME_ZONE = "Europe/Kiev";
    private static final String APPOINTMENT_DATE_UA_PATTERN = "HH:mm, d.MM.yyyy";
    private static final String REMINDER_DATE_UA_PATTERN = "d.MM.yyyy, HH:mm:ss";


    private DateTimeUtils() {
        // NONE
    }

    @NotNull
    public static String getDefaultTimeZone() {
        return DEFAULT_TIME_ZONE;
    }

    public static LocalDateTime toLocalDateTime(DateTime googleDateTime) {
       Date date = new java.util.Date(googleDateTime.getValue());
       Instant instant = date.toInstant();
       ZoneId defaultZoneId = ZoneId.of(getDefaultTimeZone());

       return instant.atZone(defaultZoneId).toLocalDateTime();
    }

    public static DateTime toGoogleDateTime(LocalDateTime localDateTime) {
        log.debug("Convert LocalDateTime: '{}' to google DateTime.", localDateTime);
        ZoneId defaultZoneId = ZoneId.of(getDefaultTimeZone());
        java.time.ZonedDateTime zonedDateTime = localDateTime.atZone(defaultZoneId);
        Date date = java.util.Date.from(zonedDateTime.toInstant());

        return new DateTime(date.getTime());
    }

    @NotNull
    public static Timestamp toTimestamp(DateTime googleDateTime) {
        LocalDateTime localDateTime = toLocalDateTime(googleDateTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(localDateTime.format(formatter));
    }

    @NotNull
    public static String toViberDateTime(DateTime googleDateTime) {
        log.debug("Convert google date time: '{}' to viber date time.", googleDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(REMINDER_DATE_UA_PATTERN);
        return toLocalDateTime(googleDateTime).format(formatter);
    }

    @NotNull
    public static String toDayMonthAndHoursMinutes(Timestamp timestamp) {
        log.debug("Extract [hour]:[minutes] from db timestamp: '{}'.", timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat(APPOINTMENT_DATE_UA_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE));

        return dateFormat.format(timestamp);
    }

    @NotNull
    public static Timestamp getAppointmentTimeStampFrom(@NotNull String messageText) {
        log.debug("Extract event's start timestamp from message: '{}'.", messageText);

        String dateTime = messageText.substring(messageText.indexOf(TIME_EMOJI) + TIME_EMOJI.length());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(APPOINTMENT_DATE_UA_PATTERN).withZone(ZoneId.of(DEFAULT_TIME_ZONE));
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);

        return Timestamp.valueOf(localDateTime);
    }
}
