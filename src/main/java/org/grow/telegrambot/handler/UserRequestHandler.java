package org.grow.telegrambot.handler;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.grow.telegrambot.enums.ScreenshotType;
import org.grow.telegrambot.pojo.UserRequest;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberBanned;

@Log4j2
public abstract class UserRequestHandler {
    public abstract boolean isApplicable(UserRequest request);
    public abstract void handle(UserRequest dispatchRequest);
    public abstract boolean isGlobal();

    public boolean hasCommand(@NotNull Update update, @NotNull String command) {
        return update.hasMessage() && update.getMessage().isCommand() && update.getMessage().getText().equals(command);
    }

    public boolean hasCallback(@NotNull Update update, @NotNull String callBackData) {
        return update.hasCallbackQuery() && update.getCallbackQuery().getData().equals(callBackData);
    }

    public boolean hasContact(@NotNull Update update) {
        return update.hasMessage() && update.getMessage().hasContact();
    }

    public boolean hasBanned(@NotNull Update update) {
        return update.hasMyChatMember() && (update.getMyChatMember().getNewChatMember() instanceof ChatMemberBanned);
    }

    public boolean isPhoto(@NotNull Update update) {
        return update.hasMessage() && (
                update.getMessage().hasPhoto() || ( update.getMessage().hasDocument()
                        && ScreenshotType.hasMimeType(update.getMessage().getDocument().getMimeType()) )
        );
    }
}
