package org.grow.telegrambot.handler.impl;

import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;

import static org.grow.telegrambot.components.Buttons.btnSocialProjectFAQ;
import static org.grow.telegrambot.components.Keyboards.FAQ_INLINE_KEYBOARD_MARKUP;


public class FaqSocialProjectHandler extends UserRequestHandler {
    private static final String SOCIAL_PROJ_MSG = "Соціальним проектом можуть скористатись люди, які мають потребу в " +
            "психологічній допомозі \uD83D\uDC9B проте на даний час не можуть оплатити собі повну вартість психотерапії. " +
            "\n\nДля запису необхідно заповнити <a href='https://forms.gle/0000'>анкету</a>";

    private final TelegramSendService sendService;


    public FaqSocialProjectHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return hasCallback(userRequest.getUpdate(), btnSocialProjectFAQ.getCallbackData());
    }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = userRequest.getChatId();
        int messageId = userRequest.getUpdate().getCallbackQuery().getMessage().getMessageId();

        sendService.editMessageText(chatId, messageId, SOCIAL_PROJ_MSG, FAQ_INLINE_KEYBOARD_MARKUP);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
