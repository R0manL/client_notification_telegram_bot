package org.grow.telegrambot.service;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.grow.EnvConfig;
import org.grow.telegrambot.Dispatcher;
import org.grow.telegrambot.GrowTelegramBot;
import org.grow.telegrambot.handler.TelegramDispatcher;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Data
public class BotTestService {
    private static final String BOT_TOKEN = EnvConfig.ENV_CONFIG.telegramToken();

    private static final long TEST_CHAT_ID = 503645241L;


    private final GrowTelegramBot bot;
    private final Update update;
    private final Chat chat;
    private final Message message;

    private final TelegramClient telegramClient = new OkHttpTelegramClient(BOT_TOKEN);
    private final Dispatcher dispatcher = new TelegramDispatcher(telegramClient).getDispatcher();


    public BotTestService() {
       this.bot = new GrowTelegramBot(dispatcher);
       this.update = new Update();

       this.chat = new Chat(TEST_CHAT_ID, "private");
       this.chat.setId(TEST_CHAT_ID);

       this.message = new Message();
       this.message.setChat(chat);

       this.update.setMessage(message);
    }

    public void sendCommand(@NotNull String command) {
        MessageEntity entity = new MessageEntity("bot_command", 0, command.length());

        message.setText(command);
        message.setEntities(List.of(entity));

        this.update.setMessage(message);
        this.bot.consume(update);
    }
}
