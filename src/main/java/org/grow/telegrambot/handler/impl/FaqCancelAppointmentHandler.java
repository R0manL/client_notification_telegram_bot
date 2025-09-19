package org.grow.telegrambot.handler.impl;

import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;

import static org.grow.telegrambot.components.Buttons.btnCancelAppointmentFAQ;
import static org.grow.telegrambot.components.Keyboards.FAQ_INLINE_KEYBOARD_MARKUP;


public class FaqCancelAppointmentHandler extends UserRequestHandler {
    private static final String CANCEL_APP_MSG = "- При відміні консультації <i>клієнтом</i> менш ніж за 24 години, консультація <b>оплачується повністю</b>;" +
            "\n\n- При відміні консультації <i>психологом</i> менш ніж за 24 години, наступна консультація проводиться <b>безкоштовно</b>;" +
            "\n\n- Відміна чи перенесення консультації більш ніж за 24 години <b> не оплачується</b>.";


    private final TelegramSendService sendService;


    public FaqCancelAppointmentHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return hasCallback(userRequest.getUpdate(), btnCancelAppointmentFAQ.getCallbackData());
    }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = userRequest.getChatId();
        int messageId = userRequest.getUpdate().getCallbackQuery().getMessage().getMessageId();

        sendService.editMessageText(chatId, messageId, CANCEL_APP_MSG, FAQ_INLINE_KEYBOARD_MARKUP);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
