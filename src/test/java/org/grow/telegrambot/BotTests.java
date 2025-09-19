package org.grow.telegrambot;

import org.junit.jupiter.api.Test;
import org.grow.telegrambot.service.BotTestService;


class BotTests {

    @Test
    void testBotReactsToUpdates() {
        new BotTestService().sendCommand("/start");
    }
}
