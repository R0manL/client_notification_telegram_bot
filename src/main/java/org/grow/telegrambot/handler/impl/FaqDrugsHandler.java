package org.grow.telegrambot.handler.impl;

import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;

import static org.grow.telegrambot.components.Buttons.btnDrugsFAQ;
import static org.grow.telegrambot.components.Keyboards.FAQ_INLINE_KEYBOARD_MARKUP;


public class FaqDrugsHandler extends UserRequestHandler {
    private static final String DRUGS_MSG = "Психологічна Студія 'Grow' ⚠️ не є медичним закладом \uD83C\uDFE5, " +
            "тому наші спеціалісти не призначають медикаментозного лікування. Якщо у клієнтів виникає потреба у медикаментах, " +
            "наш спеціаліст може направити на консультацію до \uD83D\uDC68\u200D⚕️ психіатра, який призначає відповідні медикаменти.";

    private final TelegramSendService sendService;


    public FaqDrugsHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return hasCallback(userRequest.getUpdate(), btnDrugsFAQ.getCallbackData());
    }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = userRequest.getChatId();
        int messageId = userRequest.getUpdate().getCallbackQuery().getMessage().getMessageId();

        sendService.editMessageText(chatId, messageId, DRUGS_MSG, FAQ_INLINE_KEYBOARD_MARKUP);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
