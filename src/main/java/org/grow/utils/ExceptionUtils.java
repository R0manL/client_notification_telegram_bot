package org.grow.utils;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import static org.grow.db.service.ClientDBService.setClientActivityTo;

@Log4j2
public class ExceptionUtils {
    private static final String ERROR_403 = "[403] Forbidden: bot was blocked by the user";


    private ExceptionUtils() {
        // NONE
    }

    public static void processException(@NotNull String msg, long chatID) {
        if (msg.contains(ERROR_403)) {
            log.warn("({}) Can't send msg: '{}' to user. Chat was disabled by user.", chatID, msg);
            setClientActivityTo(false, chatID);
        } else {
            log.error("Unknown error: " + msg);
        }
    }
}
