/* The MIT License
 *
 * Copyright (c) 2010-2015 Danila A. (atmakin.dv@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package io.cyberstock.tcdop.server.integration.teamcity.web;

import com.google.common.base.Strings;
import io.cyberstock.tcdop.model.*;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService;
import io.cyberstock.tcdop.server.integration.digitalocean.DOAsyncClientServiceFactory;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientServiceFactory;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;
import jetbrains.buildServer.serverSide.InvalidProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Created by beolnix on 16/05/15.
 */
public class ConfigurationValidator {

    private final DOClientServiceFactory clientFactory;

    public ConfigurationValidator(DOClientServiceFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    public Collection<InvalidProperty> formatValidation(Map<String, String> stringStringMap) {

        Collection<InvalidProperty> errors = new LinkedHashSet<InvalidProperty>();
        errors.addAll(checkInstancesLimitFormat(stringStringMap));
        errors.addAll(checkDropletSize(stringStringMap));
        errors.addAll(checkImageName(stringStringMap));
        errors.addAll(checkDropletNamePrefix(stringStringMap));
        errors.addAll(checkToken(stringStringMap));

        return errors;
    }

    private Collection<InvalidProperty> checkImageName(Map<String, String> stringStringMap) {
        return checkNotNull(stringStringMap, WebConstants.IMAGE_NAME);
    }

    private Collection<InvalidProperty> checkToken(Map<String, String> stringStringMap) {
        return checkNotNull(stringStringMap, WebConstants.TOKEN);
    }

    private Collection<InvalidProperty> checkDropletSize(Map<String, String> stringStringMap) {
        Collection<InvalidProperty> result = checkNotNull(stringStringMap, WebConstants.DROPLET_SIZE);
        if (!result.isEmpty()) {
            return result;
        }

        String sizeSlug = stringStringMap.get(WebConstants.DROPLET_SIZE);
        try {
            DropletSize.resolveBySlug(sizeSlug);
        } catch (IllegalArgumentException e) {
            return singleErrorList(WebConstants.DROPLET_SIZE, e.getMessage());
        }

        return Collections.emptyList();
    }

    private Collection<InvalidProperty> checkDropletNamePrefix(Map<String, String> stringStringMap) {
        return checkNotNull(stringStringMap, WebConstants.DROPLET_NAME_PREFIX);
    }

    private Collection<InvalidProperty> checkNotNull(Map<String, String> stringStringMap, String prop) {
        String propValue = stringStringMap.get(prop);
        if (Strings.isNullOrEmpty(propValue)) {
            return singleErrorList(prop, "Must be provided.");
        }
        return Collections.emptyList();
    }

    private Collection<InvalidProperty> checkInstancesLimitFormat(Map<String, String> stringStringMap) {
        String instancesLimitNumber = stringStringMap.get(WebConstants.INSTANCES_COUNT_LIMIT);
        if (Strings.isNullOrEmpty(instancesLimitNumber)) {
            return singleErrorList(WebConstants.INSTANCES_COUNT_LIMIT,
                    "Must be provided.");
        } else {
            try {
                Integer parsedInteger = Integer.parseInt(instancesLimitNumber);
                if (parsedInteger <= 0) {
                    return singleErrorList(WebConstants.INSTANCES_COUNT_LIMIT,
                            "Must be positive number.");
                }
            } catch (NumberFormatException e) {
                return singleErrorList(WebConstants.INSTANCES_COUNT_LIMIT,
                        "Must be number.");
            }
        }

        return Collections.emptyList();
    }

    public Collection<InvalidProperty> validateConfigurationValues(DOSettings settings) {
        Collection<InvalidProperty> validationErrors = validateToken(settings.getToken());
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }

        if (DOIntegrationMode.PREPARED_IMAGE.equals(settings.getMode())) {
            return validateImage(settings.getImageName(), settings.getSize(), settings.getToken());
        } else {
            return singleErrorList(WebConstants.DO_INTEGRATION_MODE,
                    "Selected mode isn't supported yet");
        }
    }


    private Collection<InvalidProperty> validateToken(String token) {
        DOClientService client = clientFactory.createClient(token);

        try {
            client.accountCheck();
        } catch (DOError e) {
            return singleErrorList(WebConstants.TOKEN, e.getMessage());
        }

        return Collections.emptyList();
    }

    private Collection<InvalidProperty> validateImage(String imageName, DropletSize dropletSize, String token) {
        DOClientService client = clientFactory.createClient(token);

        try {
            DOCloudImage image = client.findImageByName(imageName);

            Integer minDiskSize = image.getImage().getMinDiskSize();
            DropletSize minSize = DropletSize.resolveByDiskSize(minDiskSize);
            if (!minSize.isLessOrEqualThen(dropletSize)) {
                return singleErrorList(WebConstants.DROPLET_SIZE,
                        "Selected image requires droplet with minimum " + minDiskSize + " disk size.");
            }
        } catch (DOError e) {
            return singleErrorList(WebConstants.IMAGE_NAME, e.getMessage());
        }

        return Collections.emptyList();
    }

    private Collection<InvalidProperty> singleErrorList(String fieldId, String msg) {
        return Collections.singleton(new InvalidProperty(fieldId, msg));
    }
}