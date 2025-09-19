package org.grow.telegrambot.handler;

import org.grow.telegrambot.Dispatcher;
import org.grow.telegrambot.handler.impl.*;
import org.grow.telegrambot.service.TelegramSendService;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Arrays;

public class TelegramDispatcher {
    private final TelegramClient telegramClient;

    public TelegramDispatcher(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    public Dispatcher getDispatcher() {
        return new Dispatcher(Arrays.asList(
                new StartHandler(new TelegramSendService(this.telegramClient)),
                new NewUserHandler(new TelegramSendService(this.telegramClient)),
                new AppointmentConfirmHandler(new TelegramSendService(this.telegramClient)),
                new PaymentConfHandler(new TelegramSendService(this.telegramClient)),
                new AppointmentCancelHandler(new TelegramSendService(this.telegramClient)),
                new ContactsHandler(new TelegramSendService(this.telegramClient)),
                new PaymentRequestHandler(new TelegramSendService(this.telegramClient)),
                new FaqHandler(new TelegramSendService(this.telegramClient)),
                new FaqWorkSchedulerHandler(new TelegramSendService(this.telegramClient)),
                new FaqCancelAppointmentHandler(new TelegramSendService(this.telegramClient)),
                new FaqOnlineAppointmentHandler(new TelegramSendService(this.telegramClient)),
                new FaqChildAppointmentHandler(new TelegramSendService(this.telegramClient)),
                new FaqFriendAppointmentHandler(new TelegramSendService(this.telegramClient)),
                new FaqDrugsHandler(new TelegramSendService(this.telegramClient)),
                new FaqSocialProjectHandler(new TelegramSendService(this.telegramClient)),
                new BotBanHandler(new TelegramSendService(this.telegramClient))
        ));
    }
}
