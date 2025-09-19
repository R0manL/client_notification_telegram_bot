package org.grow.telegrambot.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSession {
    private long chatId;
    private boolean registeredUser;
    private boolean waitOnPaymentConfirmation;
}
