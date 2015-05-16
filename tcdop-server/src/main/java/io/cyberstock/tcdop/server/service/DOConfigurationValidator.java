package io.cyberstock.tcdop.server.service;

import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Account;
import com.myjeeva.digitalocean.pojo.Image;
import io.cyberstock.tcdop.server.DOIntegrationMode;
import io.cyberstock.tcdop.server.DOSettings;
import io.cyberstock.tcdop.server.error.DOCommunicationError;

/**
 * Created by beolnix on 16/05/15.
 */
public class DOConfigurationValidator {

    public static boolean isConfigurationValid(DOSettings settings) {
        DOIntegrationMode mode = settings.getMode();
        if (mode == null) {
            throw new RuntimeException("Integration mode must be provided");
        }

        if (settings.getToken() == null) {
            throw new RuntimeException("Token must be provided");
        }

        if (!isTokenValid(settings.getToken())) {
            throw new RuntimeException("Token isn't valid");
        }

        if (DOIntegrationMode.PREPARED_IMAGE.equals(mode)) {
            if (settings.getImageId() == null) {
                throw new RuntimeException("Image id must be provided for " + mode.toString() + " mode");
            }

            if (!isImageIdValid(settings.getImageId(), settings.getToken())) {
                throw new RuntimeException("Provided image id doesn't seem to be valid");
            }

            return true;
        } else {
            throw new IllegalArgumentException("Mode " + mode.toString() + " isn't supported yet.");
        }
    }

    public static boolean isTokenValid(String token) {
        DigitalOceanClient client = new DigitalOceanClient(token);

        try {
            Account account = client.getAccountInfo();
            return true;
        } catch (DigitalOceanException e) {
            return false;
        } catch (RequestUnsuccessfulException e) {
            throw new DOCommunicationError(e);
        }

    }

    public static boolean isImageIdValid(String imageId, String token) {
        DigitalOceanClient client = new DigitalOceanClient(token);

        try {
            Image image = client.getImageInfo(imageId);
            return true;
        } catch (DigitalOceanException e) {
            return false;
        } catch (RequestUnsuccessfulException e) {
            throw new DOCommunicationError(e);
        }
    }

}
