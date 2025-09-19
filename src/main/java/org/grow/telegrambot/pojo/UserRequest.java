package org.grow.telegrambot.pojo;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
@Builder
public class UserRequest {
    private Update update;
    private long chatId;
}
