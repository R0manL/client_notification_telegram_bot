package org.grow.telegrambot.handler.impl;

import lombok.extern.log4j.Log4j2;
import org.grow.db.pojo.AppointmentDB;
import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

import static org.grow.db.service.AppointmentDBService.getAppointmentsConfirmedForTodayOrTomorrowFor;
import static org.grow.db.service.AppointmentDBService.updateAppointmentImageIdFor;
import static org.grow.db.service.TherapistDBService.getTherapistLastNameBy;
import static org.grow.telegrambot.enums.Location.getAdminChatIdByLocation;
import static org.grow.utils.DateTimeUtils.toDayMonthAndHoursMinutes;


@Log4j2
public class PaymentConfHandler extends UserRequestHandler {
    private final TelegramSendService sendService;


    public PaymentConfHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isPhoto(userRequest.getUpdate());
    }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = userRequest.getChatId();

        Message message = userRequest.getUpdate().getMessage();
        PhotoSize photo = null;
        Document document = null;
        String fieldId;

        if (message.hasPhoto()) {
            List<PhotoSize> photos = message.getPhoto();
            if (photos.size() > 1) { log.warn("Expect single photo, but found more: " + photos.size() + ". Send only first one."); }
            photo = photos.get(photos.size() - 1);
            fieldId = photo.getFileId();
        } else {
          document = message.getDocument();
          fieldId = document.getFileId();
        }

        List<AppointmentDB> confirmedAppointments = getAppointmentsConfirmedForTodayOrTomorrowFor(chatId);

        int numOfAppointmentForTomorrow = confirmedAppointments.size();
        log.debug("Found '{}' confirmed appointments for today/tomorrow for chatId: '{}'.", numOfAppointmentForTomorrow, chatId);
        if (numOfAppointmentForTomorrow > 0) {
            // Update in DB.
            AppointmentDB lastAppointment = confirmedAppointments.get(numOfAppointmentForTomorrow - 1);
            updateAppointmentImageIdFor(fieldId, lastAppointment.getAppointmentId());

            sendService.sendMessage(chatId, "❤️ дякую за підтвердження");

            // Send photo to admin
            String caption = "✅ " + lastAppointment.getRoom()
                    + " - " + getTherapistLastNameBy(lastAppointment.getTherapistId())
                    + " - " + toDayMonthAndHoursMinutes(lastAppointment.getEventStart());
            long adminChatId = getAdminChatIdByLocation(lastAppointment.getRoom());

            if (photo != null) {
                sendService.sendPhoto(adminChatId, photo, caption);
            } else {
                sendService.sendDocument(adminChatId, document);
            }
        } else {
            log.warn("User send image, but no confirmed appointments have been found on today or tomorrow for chatId: {}", chatId);
        }
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
