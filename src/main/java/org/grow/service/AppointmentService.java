package org.grow.service;

import lombok.extern.log4j.Log4j2;
import org.grow.db.pojo.AppointmentDB;
import org.grow.db.pojo.ClientDB;
import org.grow.telegrambot.service.TelegramSendService;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Arrays;
import java.util.List;

import static org.grow.db.service.AppointmentDBService.isAppointmentExist;
import static org.grow.db.service.TherapistDBService.getTherapistFirstNameBy;
import static org.grow.db.service.TherapistDBService.getTherapistLastNameBy;
import static org.grow.telegrambot.components.Keyboards.APPOINTMENT_INLINE_KEYBOARD_MARKUP;
import static org.grow.telegrambot.components.Messages.APPOINTMENT_MSG_TEMPLATE;
import static org.grow.db.service.AppointmentDBService.insertAppointment;
import static org.grow.db.service.ClientDBService.getClientBy;
import static org.grow.utils.DateTimeUtils.toDayMonthAndHoursMinutes;


@Log4j2
public class AppointmentService {
    private final TelegramSendService sendService;

    public AppointmentService(TelegramClient telegramClient) {
        this.sendService = new TelegramSendService(telegramClient);
    }

    public void sendNotificationsViaTelegramFrom(List<String> calendarIDs) {
        log.info("Send notifications to Telegram from calendars: {}", Arrays.toString(calendarIDs.toArray()));
        GoogleCalendarService calendarService = new GoogleCalendarService();
        List<AppointmentDB> appointments = calendarService.getAppointmentsForTomorrowFrom(calendarIDs);

        for (AppointmentDB appointment : appointments) {
            long chatId = appointment.getChatId();

            String room = appointment.getRoom();
            String time = toDayMonthAndHoursMinutes(appointment.getEventStart());

            int therapistId = appointment.getTherapistId();
            String therapistFirstName = getTherapistFirstNameBy(therapistId);
            String therapistLastName = getTherapistLastNameBy(therapistId);

            boolean exist = isAppointmentExist(room, chatId, appointment.getEventStart());
            if (!exist) {
                insertAppointment(appointment);

                ClientDB client = getClientBy(chatId);

                // Send to Telegram.
                if(client != null && client.isActive()) {
                sendService.sendMessage(chatId, String.format(
                                APPOINTMENT_MSG_TEMPLATE,
                                room,
                                therapistFirstName + " " + therapistLastName,
                                time),
                        APPOINTMENT_INLINE_KEYBOARD_MARKUP);
                } else {
                    log.debug("Client does not exist or inactive for chatId: {}", chatId);
                }
            } else {
                log.warn("Appointment has already existed in DB. Skip Sending notification to the user.");
            }
        }
    }
}
