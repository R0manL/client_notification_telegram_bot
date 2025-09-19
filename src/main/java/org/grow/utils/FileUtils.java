package org.grow.utils;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Objects;


/**
 * Created by Roman Liubun.
 */

@Log4j2
public class FileUtils {


    private FileUtils() {
        // NONE
    }

    @NotNull
    public static URL getAbsolutePathForFileInResourceDir(@NotNull String relPath) {
        log.debug("Getting absolute path to resources dir from related: '{}'", relPath);

        URL result = FileUtils.class.getClassLoader().getResource(relPath);

        return Objects.requireNonNull(result, "Can't get absolute path from related: " + relPath);
    }

}
