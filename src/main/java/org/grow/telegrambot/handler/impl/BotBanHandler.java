package org.grow.telegrambot.handler.impl;

import lombok.extern.log4j.Log4j2;
import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;

import static org.grow.db.service.ClientDBService.setClientActivityTo;
import static org.grow.utils.UpdateUtils.getChatId;

@Log4j2
public class BotBanHandler extends UserRequestHandler {
    private final TelegramSendService sendService;

    public BotBanHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) { return hasBanned(userRequest.getUpdate()); }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = getChatId(userRequest.getUpdate());
        log.debug("Deactivate user: '{}'.", chatId);
        setClientActivityTo(false, chatId);

        sendService.notifyAdministratorsIfBotIsActive(false, chatId);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
