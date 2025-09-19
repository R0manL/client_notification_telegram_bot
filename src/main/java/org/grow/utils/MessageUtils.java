package org.grow.utils;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Optional;


/**
 * Created by Roman Liubun.
 */

@Log4j2
public class MessageUtils {

    private MessageUtils() {
        // NONE
    }

    @NotNull
    public static Optional<Message> convertToMessageFrom(@NotNull MaybeInaccessibleMessage message) {
        return Optional.ofNullable(message instanceof Message ? (Message) message : null);
    }
}
