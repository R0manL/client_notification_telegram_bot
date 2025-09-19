package org.grow.telegrambot.handler.impl;

import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;

import static org.grow.telegrambot.components.Buttons.btnOnlineAppointmentFAQ;
import static org.grow.telegrambot.components.Keyboards.FAQ_INLINE_KEYBOARD_MARKUP;


public class FaqOnlineAppointmentHandler extends UserRequestHandler {
    private static final String ONLINE_APP_MSG = "Зазвичай наші спеціалісти консультують використовуючи застосунок: Google meet. " +
            "\n\nДля консультації необхідно:\n- Забронювати час;\n- Оплатити консультацію за 24 години до призначеного часу консультації.";



    private final TelegramSendService sendService;


    public FaqOnlineAppointmentHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return hasCallback(userRequest.getUpdate(), btnOnlineAppointmentFAQ.getCallbackData());
    }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = userRequest.getChatId();
        int messageId = userRequest.getUpdate().getCallbackQuery().getMessage().getMessageId();

        sendService.editMessageText(chatId, messageId, ONLINE_APP_MSG, FAQ_INLINE_KEYBOARD_MARKUP);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
