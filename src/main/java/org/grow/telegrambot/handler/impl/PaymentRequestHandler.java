package org.grow.telegrambot.handler.impl;

import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;

import java.util.Optional;

import static org.grow.db.service.TherapistDBService.getIBANsForConfirmedAppointmentsForTodayOrTomorrowFor;


public class PaymentRequestHandler extends UserRequestHandler {
    private static final String MSG_TEXT = "Рахунок для оплати:";
    private static final String NO_IBAN_MSG_TEXT = "Номер рахунку невідомий. У вас ще немає призначених консультацій або спеціаліста.";


    private final TelegramSendService sendService;


    public PaymentRequestHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return hasCommand(userRequest.getUpdate(), "/payment");
    }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = userRequest.getChatId();

        Optional<String> iban = getIBANsForConfirmedAppointmentsForTodayOrTomorrowFor(chatId);

        if (iban.isPresent()) {
            sendService.sendMessage(chatId, MSG_TEXT);
            sendService.sendMessage(chatId, iban.get());
        } else {
            sendService.sendMessage(chatId, NO_IBAN_MSG_TEXT);
        }
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
