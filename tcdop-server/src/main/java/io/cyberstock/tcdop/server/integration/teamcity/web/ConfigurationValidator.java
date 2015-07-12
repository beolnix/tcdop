package io.cyberstock.tcdop.server.integration.teamcity.web;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Image;
import io.cyberstock.tcdop.model.*;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.DOUtils;
import jetbrains.buildServer.serverSide.InvalidProperty;
import org.springframework.format.annotation.NumberFormat;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Created by beolnix on 16/05/15.
 */
public class ConfigurationValidator {

    public static Collection<InvalidProperty> formatValidation(Map<String, String> stringStringMap) {

        Collection<InvalidProperty> errors = new LinkedHashSet<InvalidProperty>();
        errors.addAll(checkInstancesLimitFormat(stringStringMap));
        errors.addAll(checkDropletSize(stringStringMap));
        errors.addAll(checkImageName(stringStringMap));
        errors.addAll(checkDropletNamePrefix(stringStringMap));
        errors.addAll(checkToken(stringStringMap));

        return errors;
    }

    private static Collection<InvalidProperty> checkImageName(Map<String, String> stringStringMap) {
        return checkNotNull(stringStringMap, WebConstants.IMAGE_NAME);
    }

    private static Collection<InvalidProperty> checkToken(Map<String, String> stringStringMap) {
        return checkNotNull(stringStringMap, WebConstants.TOKEN);
    }

    private static Collection<InvalidProperty> checkDropletSize(Map<String, String> stringStringMap) {
        return checkNotNull(stringStringMap, WebConstants.DROPLET_SIZE);
    }

    private static Collection<InvalidProperty> checkDropletNamePrefix(Map<String, String> stringStringMap) {
        return checkNotNull(stringStringMap, WebConstants.DROPLET_NAME_PREFIX);
    }

    private static Collection<InvalidProperty> checkNotNull(Map<String, String> stringStringMap, String prop) {
        String propValue = stringStringMap.get(prop);
        if (Strings.isNullOrEmpty(propValue)) {
            return Collections.singletonList(new InvalidProperty(prop, "Must be provided."));
        }
        return Collections.EMPTY_LIST;
    }

    private static Collection<InvalidProperty> checkInstancesLimitFormat(Map<String, String> stringStringMap) {
        String instancesLimitNumber = stringStringMap.get(WebConstants.INSTANCES_COUNT_LIMIT);
        if (Strings.isNullOrEmpty(instancesLimitNumber)) {
            return Collections.singletonList(new InvalidProperty(WebConstants.INSTANCES_COUNT_LIMIT,
                    "Must be provided."));
        } else {
            try {
                Integer parsedInteger = Integer.parseInt(instancesLimitNumber);
                if (parsedInteger <= 0) {
                    return Collections.singletonList(new InvalidProperty(WebConstants.INSTANCES_COUNT_LIMIT,
                            "Must be positive number."));
                }
            } catch (NumberFormatException e) {
                return Collections.singletonList(new InvalidProperty(WebConstants.INSTANCES_COUNT_LIMIT,
                        "Must be number."));
            }
        }

        return Collections.EMPTY_LIST;
    }

    public static Collection<InvalidProperty> validateConfigurationValues(DOSettings settings) {
        Collection<InvalidProperty> validationErrors = validateToken(settings.getToken());
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }

        if (DOIntegrationMode.PREPARED_IMAGE.equals(settings.getMode())) {
            return validateImage(settings.getImageName(), settings.getSize(), settings.getToken());
        } else {
            return Collections.singletonList(new InvalidProperty(WebConstants.DO_INTEGRATION_MODE,
                    "Selected mode isn't supported yet"));
        }
    }


    private static Collection<InvalidProperty> validateToken(String token) {
        DigitalOceanClient client = new DigitalOceanClient(token);

        try {
            client.getAccountInfo();
        } catch (DigitalOceanException e) {
            return Collections.singletonList(new InvalidProperty(WebConstants.TOKEN, "Token isn't valid."));
        } catch (RequestUnsuccessfulException e) {
            return Collections.singletonList(new InvalidProperty(WebConstants.TOKEN,
                    "Can't reach Digital Ocean in order to verify token."));
        }

        return Collections.EMPTY_LIST;
    }

    private static Collection<InvalidProperty> validateImage(String imageName, DropletSize dropletSize, String token) {
        DigitalOceanClient client = new DigitalOceanClient(token);

        try {
            Optional<Image> imageOpt = DOUtils.findImageByName(client, imageName);
            if (!imageOpt.isPresent()) {
                return Collections.singletonList(new InvalidProperty(WebConstants.IMAGE_NAME,
                        "Image with name \"" + imageName + "\" not found in user images."));
            } else {
                Image image = imageOpt.get();
                Integer minDiskSize = image.getMinDiskSize();
                DropletSize minSize = DropletSize.resolveByDiskSize(minDiskSize);
                if (!minSize.isLessOrEqualThen(dropletSize)) {
                    return Collections.singletonList(new InvalidProperty(WebConstants.DROPLET_SIZE,
                            "Selected image requires droplet with minimum " + minDiskSize + " disk size."));
                }
            }
        } catch (DOError e) {
            return Collections.singletonList(new InvalidProperty(WebConstants.IMAGE_NAME,
                    "Provided image id doesn't seem to be valid"));
        }

        return Collections.EMPTY_LIST;
    }

}
