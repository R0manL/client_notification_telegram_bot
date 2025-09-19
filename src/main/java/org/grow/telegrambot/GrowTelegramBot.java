package org.grow.telegrambot;

import lombok.extern.log4j.Log4j2;
import org.grow.telegrambot.pojo.UserRequest;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.grow.utils.UpdateUtils.getChatId;


@Log4j2
public class GrowTelegramBot implements LongPollingSingleThreadUpdateConsumer {
    private static final String UNEXPECTED_UPDATE_MSG = "Unrecognized reply: ";
    private final Dispatcher dispatcher;

    public GrowTelegramBot(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void consume(Update update) {
        long chatId = getChatId(update);

        UserRequest userRequest = UserRequest
                .builder()
                .update(update)
                .chatId(chatId)
                .build();

        boolean dispatched = dispatcher.dispatch(userRequest);
        if (!dispatched) { log.warn(UNEXPECTED_UPDATE_MSG + update.getMessage()); }
    }
}
