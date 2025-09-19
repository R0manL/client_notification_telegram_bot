package org.grow.telegrambot;

import org.junit.jupiter.api.Test;
import org.grow.service.AppointmentService;
import org.grow.telegrambot.service.BotTestService;

import static org.grow.EnvConfig.ENV_CONFIG;


class AdminTests {
    private final BotTestService botTestService = new BotTestService();

    @Test
    void verifyGettingPaymentScreenshots() {
        new AppointmentService(botTestService.getTelegramClient()).sendNotificationsViaTelegramFrom(ENV_CONFIG.calendarIDsForDebug());
    }

    @Test
    void verifySendingAppointmentsFromStryiskaCalendar() {
        new AppointmentService(botTestService.getTelegramClient()).sendNotificationsViaTelegramFrom(ENV_CONFIG.calendarIDsStryiska());
    }
}
