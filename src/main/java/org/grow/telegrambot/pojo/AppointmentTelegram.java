package org.grow.telegrambot.pojo;

import com.google.api.client.util.DateTime;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Data
public class AppointmentTelegram {
    @NotNull String therapist;
    @NotNull String phone;
    @NotNull String room;
    String botName;
    boolean isNew;
    boolean isLocal;
    int price;
    @NotNull DateTime eventStart;
}
