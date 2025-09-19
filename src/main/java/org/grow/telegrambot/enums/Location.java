package org.grow.telegrambot.enums;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import static org.grow.EnvConfig.ENV_CONFIG;

@Log4j2
public enum Location {
    STRYISKA(ENV_CONFIG.adminChatIdStryiska(), "Стрийська"),
    LEMKIVSKA(ENV_CONFIG.adminChatIdLemkivska(), "Лемківська"),
    DEBUG(ENV_CONFIG.adminChatIdDebug(), "TEST");

    private final long chatID;
    private final String name;

    Location(long adminChatID, @NotNull String name) {
        this.chatID = adminChatID;
        this.name = name;
    }

    public long getChatId() {
        return this.chatID;
    }

    public static long getAdminChatIdByLocation(@NotNull String name) {
        for (Location location : values()) {
            if (name.contains(location.name)) {
                return location.chatID;
            }
        }

        log.warn("Unknown location: '{}'. Return default value: DEBUG.", name);
        return DEBUG.chatID;
    }
}
