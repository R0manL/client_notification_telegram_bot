package org.grow.telegrambot.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class Buttons {

    // APPOINTMENT_INLINE_KEYBOARD_MARKUP
    public static final InlineKeyboardButton btnAppConfirm = InlineKeyboardButton.builder().text("✅ підтвердити").callbackData("confirmApp").build();
    public static final InlineKeyboardButton btnAppCancel = InlineKeyboardButton.builder().text("\uD83D\uDEAB відмінити").callbackData("cancelApp").build();
    public static final InlineKeyboardButton btnWorkSchedulerFAQ = InlineKeyboardButton.builder().text("🕐 Графік роботи").callbackData("WorkSchedulerFAQ").build();
    public static final InlineKeyboardButton btnCancelAppointmentFAQ = InlineKeyboardButton.builder().text("🚫️Відміна консультації").callbackData("CancelAppointmentFAQ").build();
    public static final InlineKeyboardButton btnOnlineAppointmentFAQ = InlineKeyboardButton.builder().text("💻 Консультування онлайн").callbackData("OnlineAppointmentFAQ").build();
    public static final InlineKeyboardButton btnChildAppointmentFAQ = InlineKeyboardButton.builder().text("👶 Консультування дитини").callbackData("ChildAppointmentFAQQ").build();
    public static final InlineKeyboardButton btnFriendAppointmentFAQ = InlineKeyboardButton.builder().text("👬 Консультування друга чи родича").callbackData("FriendAppointmentFAQ").build();
    public static final InlineKeyboardButton btnDrugsFAQ = InlineKeyboardButton.builder().text("💊 Призначення медикаментів").callbackData("DrugsFAQ").build();
    public static final InlineKeyboardButton btnSocialProjectFAQ = InlineKeyboardButton.builder().text("🆘 Cоціальний проект").callbackData("SocialProjectFAQ").build();


    private Buttons() {
        // NONE
    }
}
