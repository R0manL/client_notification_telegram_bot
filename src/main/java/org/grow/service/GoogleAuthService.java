package org.grow.service;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;


@Log4j2
public class GoogleAuthService {

    private GoogleAuthService() {
        // NONE
    }

    @Nullable
    public static GoogleCredentials readCredentialsFrom(@NotNull URL credentialsFile, @NotNull String scope) {
        return readCredentialsFrom(credentialsFile, List.of(scope));
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param credentialsFile relative creadentialsFile to *.json keys creadentialsFile in resource dir.
     * @return An authorized Credential object.
     */
    @Nullable
    public static GoogleCredentials readCredentialsFrom(@NotNull URL credentialsFile, List<String> scopes) {
        try {
            InputStream stream = credentialsFile.openStream();

            return GoogleCredentials
                    .fromStream(stream)
                    .createScoped(scopes);
        } catch (IOException e) {
            log.error("Can't build credentials object. Error: " + e.getMessage());
        }

        return null;
    }
}
