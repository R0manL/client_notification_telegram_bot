package org.grow.telegrambot.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import static org.grow.telegrambot.components.Buttons.*;

public class Keyboards {
    public static final InlineKeyboardMarkup APPOINTMENT_INLINE_KEYBOARD_MARKUP = InlineKeyboardMarkup.builder().keyboardRow(new InlineKeyboardRow(btnAppConfirm, btnAppCancel)).build();

    public static final InlineKeyboardMarkup FAQ_INLINE_KEYBOARD_MARKUP = InlineKeyboardMarkup
            .builder()
            .keyboardRow(new InlineKeyboardRow(btnWorkSchedulerFAQ, btnCancelAppointmentFAQ))
            .keyboardRow(new InlineKeyboardRow(btnOnlineAppointmentFAQ, btnChildAppointmentFAQ))
            .keyboardRow(new InlineKeyboardRow(btnFriendAppointmentFAQ, btnDrugsFAQ))
            .keyboardRow(new InlineKeyboardRow(btnSocialProjectFAQ))
            .build();

    private Keyboards() {
        // NONE
    }
}
