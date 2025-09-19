package org.grow.telegrambot.handler.impl;

import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;

import static org.grow.telegrambot.components.Buttons.btnWorkSchedulerFAQ;
import static org.grow.telegrambot.components.Keyboards.FAQ_INLINE_KEYBOARD_MARKUP;


public class FaqWorkSchedulerHandler extends UserRequestHandler {
    private static final String WORK_SCHEDULER_MSG = "Студія працює: 9:30 - 20:00 пн-пт, 10:00 - 14:00 сб. " +
            "\nІндивідуальний графік та можливість запису до конкретного спеціалістів можна дізнатись за телефоном чи написавши нам в месенджері.";



    private final TelegramSendService sendService;


    public FaqWorkSchedulerHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return hasCallback(userRequest.getUpdate(), btnWorkSchedulerFAQ.getCallbackData());
    }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = userRequest.getChatId();
        int messageId = userRequest.getUpdate().getCallbackQuery().getMessage().getMessageId();

        sendService.editMessageText(chatId, messageId, WORK_SCHEDULER_MSG, FAQ_INLINE_KEYBOARD_MARKUP);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
