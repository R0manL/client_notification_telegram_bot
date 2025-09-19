package org.grow.utils;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * Created by Roman Liubun.
 */

@Log4j2
public class UpdateUtils {


    private UpdateUtils() {
        // NONE
    }

    public static long getChatId(Update update) {
        log.debug("Get chatId from update.");
        Message message = update.getMessage();
        if (message != null) {
            return message.getChatId();
        }

        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery != null) {
            return update.getCallbackQuery().getMessage().getChatId();
        }

        ChatMemberUpdated chatMemberUpdated = update.getMyChatMember();
        if (chatMemberUpdated != null) {
            return chatMemberUpdated.getChat().getId();
        }

        log.error("Can't get chat id from update: " + update);
        return 0;
    }
}
