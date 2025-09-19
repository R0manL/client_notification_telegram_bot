package org.grow.telegrambot.handler.impl;

import org.grow.db.pojo.ClientDB;
import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.service.ClientService;
import org.grow.telegrambot.service.TelegramSendService;
import org.telegram.telegrambots.meta.api.objects.Contact;

import static org.grow.db.service.ClientDBService.getClientBy;

public class NewUserHandler extends UserRequestHandler {
    private final TelegramSendService sendService;
    private final ClientService clientService = new ClientService();

    @Override
    public boolean isApplicable(UserRequest userRequest) { return hasContact(userRequest.getUpdate()); }

    public NewUserHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = userRequest.getChatId();
        Contact contact = userRequest.getUpdate().getMessage().getContact();
        String phoneNumber = contact.getPhoneNumber();

        ClientDB client = getClientBy(chatId);
        if (client == null) {
            clientService.addNewClient(chatId, phoneNumber);
        } else {
            clientService.updateClient(client.getClientId(), phoneNumber);
        }

        sendService.replyKeyboardRemove(chatId, "Ви успішно зареєстувались.");

        sendService.notifyAdministratorsIfBotIsActive(true, chatId);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
