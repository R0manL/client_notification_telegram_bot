package org.grow.telegrambot.service;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.grow.db.pojo.ClientDB;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.grow.db.service.ClientDBService.getClientBy;
import static org.grow.db.service.ClientDBService.setClientActivityTo;
import static org.grow.telegrambot.enums.Location.*;
import static org.grow.utils.ExceptionUtils.processException;

@Log4j2
public class TelegramSendService {
    private final TelegramClient telegramClient;

    public TelegramSendService(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    public void sendMessage(long chatId, @NotNull String text) {
        sendMessage(chatId, text, null);
    }

    public void sendMessage(long chatId, @NotNull String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = SendMessage
                .builder()
                .text(text)
                .chatId(chatId)
                //Other possible parse modes: MARKDOWNV2, MARKDOWN, which allows to make text bold, and all other things
                .parseMode(ParseMode.HTML)
                .replyMarkup(replyKeyboard)
                .build();
        try {
            log.info("Send to: '{}', message: '{}'.", chatId, text);
            telegramClient.execute(sendMessage);
            setClientActivityTo(true, chatId);
        } catch (TelegramApiException e) {
            processException(e.getMessage(), chatId);
        }
    }

    public void sendPhoto(long chatId, PhotoSize photo, @Nullable String caption) {
        String fileId = photo.getFileId();

        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(fileId))
                .caption(caption)
                .build();

        try {
            log.info("Send photo to: '{}'.", chatId);
            telegramClient.execute(sendPhoto);
        } catch (TelegramApiException e) {
            processException("Send photo exception: " + e.getMessage(), chatId);
        }
    }

    public void sendDocument(long chatId, @NotNull Document document) {
        String fileId = document.getFileId();

        SendDocument sendDocument = SendDocument
                .builder()
                .chatId(chatId)
                .document(new InputFile(fileId))
                .build();
        try {
            log.info("Send document to: '{}'.", chatId);
            telegramClient.execute(sendDocument);
        } catch (TelegramApiException e) {
            processException("Send document exception: " + e.getMessage(), chatId);
        }
    }

    public void editMessageReplyMarkup(long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        log.debug("Edit message reply markup for chatID: {}, messageID: {}, inlineKeyboardMarkup: {}", chatId, messageId, inlineKeyboardMarkup);
        EditMessageReplyMarkup message = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error("Send message exception: {}", e.getMessage());
        }
    }

    public void editMessageText(long chatId, int messageId, @NotNull String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .parseMode(ParseMode.HTML)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error("Send html message exception: {}", e.getMessage());
        }
    }

    public void replyKeyboardRemove(long chatId, @NotNull String text) {
        ReplyKeyboardRemove keyboard = ReplyKeyboardRemove
                .builder()
                .removeKeyboard(true)
                .build();

        sendMessage(chatId, text, keyboard);
    }

    /**
     * Notify administrators that user register / unregister a bot.
     * @param active - true / false (register / unregister)
     * @param chatId - user's chat id.
     */
    public void notifyAdministratorsIfBotIsActive(boolean active, long chatId) {
        ClientDB client = getClientBy(chatId);
        if (client != null) {
            String message = "користувач ☎️" + client.getPhone();
            if(active) {
                message = "\uD83C\uDF89️ " + message + " налаштував \uD83E\uDD16";
            } else {
                message = "⚠️ " + message + " відключив \uD83E\uDD16";
            }

            sendMessage(STRYISKA.getChatId(), message);
            sendMessage(LEMKIVSKA.getChatId(), message);
        } else {
            log.warn("No user with chatId: {}", chatId);
        }
    }
}
