package io.cyberstock.tcdop.server.integration.teamcity.web;

import io.cyberstock.tcdop.model.*;
import jetbrains.buildServer.clouds.CloudClientParameters;

import java.util.Map;

/**
 * Created by beolnix on 18/06/15.
 */
public class DOSettingsUtils {

    private DOSettingsUtils() {}

    public static DOSettings convertToDOSettings(CloudClientParameters cloudClientParameters) {
        DOSettingsBuilder builder = new DOSettingsBuilder();

        String strMode = cloudClientParameters.getParameter(WebConstants.DO_INTEGRATION_MODE);
        builder.withMode(DOIntegrationMode.getByCode(strMode));

        String token = cloudClientParameters.getParameter(WebConstants.TOKEN);
        builder.withToken(token);

        String imageName = cloudClientParameters.getParameter(WebConstants.IMAGE_NAME);
        builder.withImageName(imageName);

        String sizeSlug = cloudClientParameters.getParameter(WebConstants.DROPLET_SIZE);
        if (sizeSlug != null) {
            //incorrect format and null value should be intercepted by validator of user input
            builder.withSize(DropletSize.resolveBySlug(sizeSlug));
        }

        String dropletNamePrefix = cloudClientParameters.getParameter(WebConstants.DROPLET_NAME_PREFIX);
        builder.withDropletNamePrefix(dropletNamePrefix);

        String instancesNumberLimit = cloudClientParameters.getParameter(WebConstants.INSTANCES_COUNT_LIMIT);
        if (instancesNumberLimit != null) {
            //incorrect format and null value should be intercepted by validator of user input
            builder.withInstancesLimit(Integer.parseInt(instancesNumberLimit));
        }

        return builder.build();
    }

    public static DOSettings convertToDOSettings(Map<String, String> stringStringMap) {
        DOSettingsBuilder builder = new DOSettingsBuilder();

        String strMode = stringStringMap.get(WebConstants.DO_INTEGRATION_MODE);
        builder.withMode(DOIntegrationMode.getByCode(strMode));

        builder.withToken(stringStringMap.get(WebConstants.TOKEN));
        builder.withImageName(stringStringMap.get(WebConstants.IMAGE_NAME));

        String instancesLimitStr = stringStringMap.get(WebConstants.INSTANCES_COUNT_LIMIT);
        if (instancesLimitStr != null) {
            //incorrect format and null value should be intercepted by validator of user input
            builder.withInstancesLimit(Integer.parseInt(instancesLimitStr));
        }

        String sizeSlugStr = stringStringMap.get(WebConstants.DROPLET_SIZE);
        if (sizeSlugStr != null) {
            //incorrect format and null value should be intercepted by validator of user input
            builder.withSize(DropletSize.resolveBySlug(sizeSlugStr));
        }

        builder.withDropletNamePrefix(stringStringMap.get(WebConstants.DROPLET_NAME_PREFIX));

        return builder.build();
    }
}
