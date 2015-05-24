package io.cyberstock.tcdop.server.service;

import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Account;
import io.cyberstock.tcdop.server.DOIntegrationMode;
import io.cyberstock.tcdop.server.DOSettings;
import io.cyberstock.tcdop.server.error.ConfigurationValidationError;
import io.cyberstock.tcdop.server.error.DOCommunicationError;
import io.cyberstock.tcdop.server.error.UnsupportedDOModeError;

/**
 * Created by beolnix on 16/05/15.
 */
public class DOConfigurationValidator {

    public static void validateConfiguration(DOSettings settings) {
        validateCommonProperties(settings);

        if (DOIntegrationMode.PREPARED_IMAGE.equals(settings.getMode())) {
            validatePreparedImageProperties(settings);
        } else {
            throw new UnsupportedDOModeError(settings.getMode());
        }
    }

    private static void validatePreparedImageProperties(DOSettings settings) {
        if (settings.getImageId() == null) {
            throw new ConfigurationValidationError("Image id must be provided for " + settings.getMode().toString() + " mode");
        }

        validateImage(settings.getImageId(), settings.getToken());
    }


    private static void validateCommonProperties(DOSettings settings) {
        DOIntegrationMode mode = settings.getMode();
        if (mode == null) {
            throw new ConfigurationValidationError("Integration mode must be provided");
        }

        if (settings.getToken() == null) {
            throw new ConfigurationValidationError("Token must be provided");
        }

        validateToken(settings.getToken());
    }

    /**
     * Provided token is completely valid if no exception has been thrown
     * @param token Digital Ocean account token
     */
    private static void validateToken(String token) {
        DigitalOceanClient client = new DigitalOceanClient(token);

        try {
            client.getAccountInfo();
        } catch (DigitalOceanException e) {
            throw new ConfigurationValidationError("Token isn't valid");
        } catch (RequestUnsuccessfulException e) {
            throw new DOCommunicationError(e);
        }

    }

    /**
     * Provided image is valid if no exception has been thrown
     * @param imageId Digital Ocean image name
     * @param token Digital Ocean account token
     */
    private static void validateImage(String imageId, String token) {
        DigitalOceanClient client = new DigitalOceanClient(token);

        try {
            client.getImageInfo(imageId);
        } catch (DigitalOceanException e) {
            throw new ConfigurationValidationError("Provided image id doesn't seem to be valid");
        } catch (RequestUnsuccessfulException e) {
            throw new DOCommunicationError(e);
        }
    }

}
