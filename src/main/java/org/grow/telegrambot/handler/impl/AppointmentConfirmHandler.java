package org.grow.telegrambot.handler.impl;

import lombok.extern.log4j.Log4j2;
import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.sql.Timestamp;
import java.util.Optional;

import static org.grow.telegrambot.components.Buttons.btnAppConfirm;
import static org.grow.telegrambot.components.Messages.APPOINTMENT_PREFIX;
import static org.grow.db.service.AppointmentDBService.updateAppointmentStatusWith;
import static org.grow.db.service.TherapistDBService.getIBANsForConfirmedAppointmentsForTodayOrTomorrowFor;
import static org.grow.telegrambot.enums.Location.getAdminChatIdByLocation;
import static org.grow.utils.DateTimeUtils.getAppointmentTimeStampFrom;
import static org.grow.utils.MessageUtils.convertToMessageFrom;


@Log4j2
public class AppointmentConfirmHandler extends UserRequestHandler {
    private static final String CONFIRMATION_REQUEST_MSG = "⚠️ Для підтвредження, надішліть, будь-ласка, скріншот \uD83D\uDDBC квитанції про оплату";
    private static final String CONFIRM_MSG = "✅ Зустріч підтверджено";
    private final TelegramSendService sendService;


    public AppointmentConfirmHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return hasCallback(userRequest.getUpdate(), btnAppConfirm.getCallbackData());
    }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = userRequest.getChatId();
        MaybeInaccessibleMessage maybeInaccessibleMsg = userRequest.getUpdate().getCallbackQuery().getMessage();
        Optional<Message> message = convertToMessageFrom(maybeInaccessibleMsg);

        if (message.isPresent()) {
            int messageId = message.get().getMessageId();
            String messageText = message.get().getText();

            // Update status in DB
            Timestamp eventStartDate = getAppointmentTimeStampFrom(messageText);
            updateAppointmentStatusWith(true, chatId, eventStartDate);

            sendService.sendMessage(chatId, CONFIRMATION_REQUEST_MSG);

            Optional<String> iban = getIBANsForConfirmedAppointmentsForTodayOrTomorrowFor(chatId);

            iban.ifPresent(s -> sendService.sendMessage(chatId, s));
            sendService.editMessageReplyMarkup(chatId, messageId, null);

            // Send message to admin
            String newMessageText = messageText.replace(APPOINTMENT_PREFIX, CONFIRM_MSG);
            long adminChatId = getAdminChatIdByLocation(messageText);
            sendService.sendMessage(adminChatId, newMessageText);
        } else {
            log.error("Can't parse callback message for chatID: {}", chatId);
        }
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
