package org.grow;

import lombok.extern.log4j.Log4j2;
import org.grow.service.AppointmentService;
import org.grow.telegrambot.Dispatcher;
import org.grow.telegrambot.GrowTelegramBot;
import org.grow.telegrambot.handler.TelegramDispatcher;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static org.grow.EnvConfig.ENV_CONFIG;

@Log4j2
public class Application {
    private static final String BOT_TOKEN = ENV_CONFIG.telegramToken();

    public static void main(String[] args) throws InterruptedException {
        setLocalTimezone();

        log.info("Start server...");
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            log.info("Registering telegram bot...");
            TelegramClient telegramClient = new OkHttpTelegramClient(BOT_TOKEN);
            Dispatcher dispatcher = new TelegramDispatcher(telegramClient).getDispatcher();

            botsApplication.registerBot(BOT_TOKEN, new GrowTelegramBot(dispatcher));
            log.info("Telegram bot is ready...");

            List<String> calendarIDs = new ArrayList<>();
            calendarIDs.addAll(ENV_CONFIG.calendarIDsStryiska());
            calendarIDs.addAll(ENV_CONFIG.calendarsIDsLemkivska());

            AppointmentService appointmentService = new AppointmentService(telegramClient);
            appointmentService.sendNotificationsViaTelegramFrom(calendarIDs);

            Thread.currentThread().join();
            log.debug("Server has start and running.");
        } catch (Exception e) {
            throw new InterruptedException("Long poling telegram bot exception was thrown. Error: " + e.getMessage());
        }
    }

    private static void setLocalTimezone() {
        final String timeZone = "Europe/Kiev";
        log.debug("Set JDK timezone: {}", timeZone);
        System.setProperty("user.timezone", timeZone);
        TimeZone.setDefault(null);
    }
}
