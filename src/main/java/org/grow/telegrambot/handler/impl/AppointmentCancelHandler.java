package org.grow.telegrambot.handler.impl;

import lombok.extern.log4j.Log4j2;
import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.sql.Timestamp;
import java.util.Optional;

import static org.grow.telegrambot.components.Buttons.btnAppCancel;
import static org.grow.telegrambot.components.Messages.APPOINTMENT_PREFIX;
import static org.grow.db.service.AppointmentDBService.updateAppointmentStatusWith;
import static org.grow.telegrambot.enums.Location.getAdminChatIdByLocation;
import static org.grow.utils.DateTimeUtils.getAppointmentTimeStampFrom;
import static org.grow.utils.MessageUtils.convertToMessageFrom;


@Log4j2
public class AppointmentCancelHandler extends UserRequestHandler {
    private static final String APP_CANCELED_MSG = "\uD83D\uDEAB Зустріч скасовано";
    private static final String THANK_YOU_MSG = "❤️ дякуємо що повідомили";



    private final TelegramSendService sendService;


    public AppointmentCancelHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return hasCallback(userRequest.getUpdate(), btnAppCancel.getCallbackData());
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
            updateAppointmentStatusWith(false, chatId, eventStartDate);

            sendService.sendMessage(chatId, APP_CANCELED_MSG);
            sendService.sendMessage(chatId, THANK_YOU_MSG);
            sendService.editMessageReplyMarkup(chatId, messageId, null);

            // Send message to admin
            String newMessageText = messageText.replace(APPOINTMENT_PREFIX, APP_CANCELED_MSG);
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
