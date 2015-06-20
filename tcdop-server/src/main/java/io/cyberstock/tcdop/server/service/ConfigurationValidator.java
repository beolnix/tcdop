package io.cyberstock.tcdop.server.service;

import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import io.cyberstock.tcdop.model.DOConfigConstants;
import io.cyberstock.tcdop.model.DOIntegrationMode;
import io.cyberstock.tcdop.model.DOSettings;
import jetbrains.buildServer.serverSide.InvalidProperty;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by beolnix on 16/05/15.
 */
public class ConfigurationValidator {

    public static Collection<InvalidProperty> validateConfiguration(DOSettings settings) {
        Collection<InvalidProperty> validationErrors = validateCommonProperties(settings);
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }

        if (DOIntegrationMode.PREPARED_IMAGE.equals(settings.getMode())) {
            return validatePreparedImageProperties(settings);
        } else {
            return Collections.singletonList(new InvalidProperty(DOConfigConstants.DO_INTEGRATION_MODE,
                    "Selected mode insn't supported yet"));
        }
    }

    private static Collection<InvalidProperty> validatePreparedImageProperties(DOSettings settings) {
        if (settings.getImageId() == null) {
            return Collections.singletonList(new InvalidProperty(DOConfigConstants.IMAGE_ID,
                    "Image id must be provided for " + settings.getMode().toString() + " mode"));
        }

        return validateImage(settings.getImageId(), settings.getToken());
    }


    private static Collection<InvalidProperty> validateCommonProperties(DOSettings settings) {


        if (settings.getToken() == null) {
            return Collections.singletonList(new InvalidProperty(DOConfigConstants.TOKEN,
                    "Token must be provided."));
        }

        return validateToken(settings.getToken());
    }

    private static Collection<InvalidProperty> validateToken(String token) {
        DigitalOceanClient client = new DigitalOceanClient(token);

        try {
            client.getAccountInfo();
        } catch (DigitalOceanException e) {
            return Collections.singletonList(new InvalidProperty(DOConfigConstants.TOKEN, "Token isn't valid."));
        } catch (RequestUnsuccessfulException e) {
            return Collections.singletonList(new InvalidProperty(DOConfigConstants.TOKEN,
                    "Can't reach Digital Ocean in order to verify token."));
        }

        return Collections.EMPTY_LIST;
    }

    private static Collection<InvalidProperty> validateImage(String imageId, String token) {
        DigitalOceanClient client = new DigitalOceanClient(token);

        try {
            client.getImageInfo(imageId);
        } catch (DigitalOceanException e) {
            return Collections.singletonList(new InvalidProperty(DOConfigConstants.IMAGE_ID,
                    "Provided image id doesn't seem to be valid"));
        } catch (RequestUnsuccessfulException e) {
            return Collections.singletonList(new InvalidProperty(DOConfigConstants.IMAGE_ID,
                    "Can't reach Digital Ocean in order to verify is everything fine with provided image id."));
        }

        return Collections.EMPTY_LIST;
    }

}
