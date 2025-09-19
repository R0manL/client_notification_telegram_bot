package org.grow.telegrambot.components;

public class Messages {

    public static final String APPOINTMENT_PREFIX = "У вас запланована зустіч:";
    public static final String TIME_EMOJI = "🗒 ";

    public static final String APPOINTMENT_MSG_TEMPLATE = APPOINTMENT_PREFIX + "\n     📍%s\n     👤%s\n     " + TIME_EMOJI + "%s";

    private Messages() {
        // NONE
    }
}
