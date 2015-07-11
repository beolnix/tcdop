package io.cyberstock.tcdop.server.service;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Image;
import io.cyberstock.tcdop.model.DOConfigConstants;
import io.cyberstock.tcdop.model.DOIntegrationMode;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.DOUtils;
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
                    "Selected mode isn't supported yet"));
        }
    }

    private static Collection<InvalidProperty> validatePreparedImageProperties(DOSettings settings) {
        if (Strings.isNullOrEmpty(settings.getImageName())) {
            return Collections.singletonList(new InvalidProperty(DOConfigConstants.IMAGE_NAME,
                    "Image id must be provided for " + settings.getMode().toString() + " mode"));
        }

        return validateImage(settings.getImageName(), settings.getToken());
    }


    private static Collection<InvalidProperty> validateCommonProperties(DOSettings settings) {


        if (Strings.isNullOrEmpty(settings.getToken())) {
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

    private static Collection<InvalidProperty> validateImage(String imageName, String token) {
        DigitalOceanClient client = new DigitalOceanClient(token);

        try {
            Optional<Image> imageOpt = DOUtils.findImageByName(client, imageName);
            if (!imageOpt.isPresent()) {
                return Collections.singletonList(new InvalidProperty(DOConfigConstants.IMAGE_NAME,
                        "Image with name \"" + imageName + "\" not found in user images."));
            }
        } catch (DOError e) {
            return Collections.singletonList(new InvalidProperty(DOConfigConstants.IMAGE_NAME,
                    "Provided image id doesn't seem to be valid"));
        }

        return Collections.EMPTY_LIST;
    }

}
