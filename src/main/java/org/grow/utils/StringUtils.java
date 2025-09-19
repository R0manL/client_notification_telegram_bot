package org.grow.utils;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Created by Roman Liubun.
 */

@Log4j2
public class StringUtils {
    private static final String TEST_PREFIX = "qa_";


    private StringUtils() {
        // NONE
    }

    @NotNull
    public static String generateStringBasedOnCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddhhmmssSSS");
        return TEST_PREFIX + now.format(formatter);
    }

    /**
     * Method format phone number into DB format.
     * @param phoneNumber - number in any format.
     * @return number in format ready to insert into DB.
     */
    @NotNull
    public static String formatPhoneNumberForDB(@NotNull String phoneNumber) {
        return phoneNumber.replace("+", "");
    }
}
