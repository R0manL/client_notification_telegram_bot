package org.grow.telegrambot.enums;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
public enum ScreenshotType {
    JPEG("image/jpeg"),
    PNG("image/png"),
    PDF("application/pdf");

    private final String mimeType;

    ScreenshotType(@NotNull String mimeType) {
        this.mimeType = mimeType;
    }

    public static boolean hasMimeType(@NotNull String mimeType) {
        for (ScreenshotType screenshotType : values()) {
            if (screenshotType.mimeType.equals(mimeType)) {
                return true;
            }
        }

        return false;
    }
}
