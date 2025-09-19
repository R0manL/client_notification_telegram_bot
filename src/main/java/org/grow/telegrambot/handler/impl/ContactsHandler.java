package org.grow.telegrambot.handler.impl;

import org.grow.telegrambot.handler.UserRequestHandler;
import org.grow.telegrambot.pojo.UserRequest;
import org.grow.telegrambot.service.TelegramSendService;


public class ContactsHandler extends UserRequestHandler {
    private static final String MSG_TEXT = "📍<a href='https://goo.gl/maps/C00000'>Офіс 1. Місто, Лемківська 1</a>" +
            "\n 📞 067 000 0000 " +
            "\n 🚗 заїзд з вул. Ч " +
            "\n 🚌 зупинка: палац" +
            "\n 💬 @grow_center" +
            "\n\n📍<a href='https://goo.gl/maps/test'>Офіс 2. м.Місто вул. Стрийстка 40</a>" +
            "\n  📞 097 000 0000 " +
            "\n 🚗 заїзд з вул. Ф " +
            "\n  🚌 зупинка: завод " +
            "\n \uD83D\uDCAC  @grow_str" +
            "\n\n\uD83D\uDCF7 <a href='https://instagram.com/centr.grow'>Instagram</a>" +
            "\n🎦 <a href='https://www.youtube.com/c/CenterGrow'>Youtube</a>" +
            "\n👍 <a href='https://g.page/r/000000/review'>Залишити відгук</a>" +
            "\n👥 <a href='https://www.test.ua/specialists/'>Спеціалісти студії 'Grow'</a>";


    private final TelegramSendService sendService;


    public ContactsHandler(TelegramSendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return hasCommand(userRequest.getUpdate(), "/contacts");
    }

    @Override
    public void handle(UserRequest userRequest) {
        long chatId = userRequest.getChatId();
        sendService.sendMessage(chatId, MSG_TEXT);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
