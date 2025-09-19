package org.grow.telegrambot.handler.impl;

import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;

import static org.grow.telegrambot.components.Buttons.btnFriendAppointmentFAQ;
import static org.grow.telegrambot.components.Keyboards.FAQ_INLINE_KEYBOARD_MARKUP;


public class FaqFriendAppointmentHandler extends UserRequestHandler {
    private static final String FRIEND_APP_MSG = "⚠️ Однією з важливих умов, для роботи з психологом, психотерапевтом є добровільна згода клієнта. " +
            "\nВи можете запропонувати Вашому другові або родичу спробувати звернутися на одну консультацію і після цього вже вирішити чи йому підходить " +
            "співпраця з психологом. Якщо Ваш друг, або родич, погоджується піти до спеціаліста, але не знає як записатись, ви можете передати йому контакти Grow: /contacts";

    private final TelegramSendService sendService;


    public FaqFriendAppointmentHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return hasCallback(userRequest.getUpdate(), btnFriendAppointmentFAQ.getCallbackData());
    }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = userRequest.getChatId();
        int messageId = userRequest.getUpdate().getCallbackQuery().getMessage().getMessageId();

        sendService.editMessageText(chatId, messageId, FRIEND_APP_MSG, FAQ_INLINE_KEYBOARD_MARKUP);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
