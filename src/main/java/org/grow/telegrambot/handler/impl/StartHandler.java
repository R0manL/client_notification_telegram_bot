package org.grow.telegrambot.handler.impl;

import org.grow.db.pojo.ClientDB;
import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static org.grow.db.service.ClientDBService.getClientBy;
import static org.grow.db.service.ClientDBService.setClientActivityTo;


public class StartHandler extends UserRequestHandler {
    private final TelegramSendService sendService;

    private static final String REGISTRATION_MSG = "Вітаю, для продовження вам необхідно зареєструватись ⬇️️";


    public StartHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return hasCommand(userRequest.getUpdate(), "/start");
    }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = userRequest.getChatId();
        ClientDB client = getClientBy(chatId);

        // Check if it's a new client.
        if (client == null) {
            sendService.sendMessage(chatId, REGISTRATION_MSG, requestAuthorizationKeyboard());
        } else {
            setClientActivityTo(true, chatId);
            sendService.notifyAdministratorsIfBotIsActive(true, chatId);
        }
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

    private ReplyKeyboardMarkup requestAuthorizationKeyboard() {
        KeyboardButton clickHereBtn = new KeyboardButton("Зареєструватись за номером телефону");
        clickHereBtn.setRequestContact(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(clickHereBtn);

        List <KeyboardRow> keyboardRows = List.of(keyboardRow);
        ReplyKeyboardMarkup result = new ReplyKeyboardMarkup(keyboardRows);
        result.setResizeKeyboard(true);
        result.setOneTimeKeyboard(true);

        return result;
    }
}
