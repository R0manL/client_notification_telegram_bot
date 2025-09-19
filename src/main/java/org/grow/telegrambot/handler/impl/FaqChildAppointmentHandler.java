package org.grow.telegrambot.handler.impl;

import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;

import static org.grow.telegrambot.components.Buttons.btnChildAppointmentFAQ;
import static org.grow.telegrambot.components.Keyboards.FAQ_INLINE_KEYBOARD_MARKUP;


public class FaqChildAppointmentHandler extends UserRequestHandler {
    private static final String CHILD_APP_MSG = "Консультація проводиться за допомогою методів дитячої або юнацької психотерапії: " +
            "\n - ігротерапія " +
            "\n - арт-терапія " +
            "\n - казкотерапія " +
            "\n - поведінкова терапія " +
            "\n - спеціально побудована бесіда з психологом " +
            "\n\n 1️⃣ перша консультація відбувається разом з батьками, для встановлення контакту з дитиною, та узгодження подальшого плану терапії. " +
            "\n\n⚠️Запис на консультацію дітей до 18 років можливий лише за згодою батьків \uD83D\uDC6A";

    private final TelegramSendService sendService;


    public FaqChildAppointmentHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return hasCallback(userRequest.getUpdate(), btnChildAppointmentFAQ.getCallbackData());
    }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = userRequest.getChatId();
        int messageId = userRequest.getUpdate().getCallbackQuery().getMessage().getMessageId();

        sendService.editMessageText(chatId, messageId, CHILD_APP_MSG, FAQ_INLINE_KEYBOARD_MARKUP);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
