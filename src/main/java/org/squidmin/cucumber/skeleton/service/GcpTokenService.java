package org.squidmin.cucumber.skeleton.service;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.squidmin.cucumber.skeleton.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

@Service
@Slf4j
public class GcpTokenService {

    private static final String CLOUD_PLATFORM_SCOPE = "https://www.googleapis.com/auth/cloud-platform";

    @Value("${gcp.sa-key-path:#{null}}")
    private String gcpSaKeyPath;

    /**
     * Attempts to generate a GCP access token.
     * Priority:
     *   1. Service account key file (if path is configured and valid)
     *   2. Application Default Credentials (ADC)
     *
     * @return A valid GCP access token string, or null if authentication fails.
     */
    public String generateAccessToken() {
        // 1. Try service account key file first
        if (StringUtils.isNotBlank(gcpSaKeyPath)) {
            String token = generateTokenFromSaKeyFile(gcpSaKeyPath);
            if (token != null) {
                return token;
            }
        }

        // 2. Fall back to Application Default Credentials
        return generateTokenFromAdc();
    }

    /**
     * Generates an access token from a service account key file.
     *
     * @param saKeyPath Absolute path to the service account JSON key file.
     * @return Access token string, or null on failure.
     */
    public String generateTokenFromSaKeyFile(String saKeyPath) {
        Logger.log(
            String.format("Attempting SA key file authentication from: %s", saKeyPath),
            Logger.LogType.INFO
        );
        try (FileInputStream stream = new FileInputStream(saKeyPath)) {
            GoogleCredentials credentials = ServiceAccountCredentials
                .fromStream(stream)
                .createScoped(Collections.singletonList(CLOUD_PLATFORM_SCOPE));
            credentials.refreshIfExpired();
            AccessToken token = credentials.getAccessToken();
            if (token != null && StringUtils.isNotBlank(token.getTokenValue())) {
                Logger.log("Access token generated successfully using service account key file.", Logger.LogType.INFO);
                return token.getTokenValue();
            }
        } catch (IOException e) {
            Logger.log(
                String.format("Failed to generate token from SA key file: %s", e.getMessage()),
                Logger.LogType.ERROR
            );
        }
        return null;
    }

    /**
     * Generates an access token using Application Default Credentials (ADC).
     * Relies on the environment being authenticated via:
     *   - GOOGLE_APPLICATION_CREDENTIALS env var, or
     *   - `gcloud auth application-default login`
     *
     * @return Access token string, or null on failure.
     */
    public String generateTokenFromAdc() {
        Logger.log("Attempting Application Default Credentials (ADC) authentication.", Logger.LogType.INFO);
        try {
            GoogleCredentials credentials = GoogleCredentials
                .getApplicationDefault()
                .createScoped(Collections.singletonList(CLOUD_PLATFORM_SCOPE));
            credentials.refreshIfExpired();
            AccessToken token = credentials.getAccessToken();
            if (token != null && StringUtils.isNotBlank(token.getTokenValue())) {
                Logger.log("Access token generated successfully using ADC.", Logger.LogType.INFO);
                return token.getTokenValue();
            }
        } catch (IOException e) {
            Logger.log(
                String.format("Failed to generate token from ADC: %s", e.getMessage()),
                Logger.LogType.ERROR
            );
        }
        return null;
    }

}
