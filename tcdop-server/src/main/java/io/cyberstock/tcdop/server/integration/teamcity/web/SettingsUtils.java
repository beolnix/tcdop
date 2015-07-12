package io.cyberstock.tcdop.server.integration.teamcity.web;

import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.WebConstants;
import jetbrains.buildServer.clouds.CloudClientParameters;

import java.util.HashMap;

/**
 * Created by beolnix on 18/06/15.
 */
public class SettingsUtils {

    public static DOSettings convertClientParametersToDOSettings(CloudClientParameters cloudClientParameters) {
        HashMap<String, String> paramsMap = new HashMap<String, String>();

        String strMode = cloudClientParameters.getParameter(WebConstants.DO_INTEGRATION_MODE);
        paramsMap.put(WebConstants.DO_INTEGRATION_MODE, strMode);

        String token = cloudClientParameters.getParameter(WebConstants.TOKEN);
        paramsMap.put(WebConstants.TOKEN, token);

        String imageId = cloudClientParameters.getParameter(WebConstants.IMAGE_NAME);
        paramsMap.put(WebConstants.IMAGE_NAME, imageId);

        String sizeSlug = cloudClientParameters.getParameter(WebConstants.DROPLET_SIZE);
        paramsMap.put(WebConstants.DROPLET_SIZE, sizeSlug);

        String dropletNamePrefix = cloudClientParameters.getParameter(WebConstants.DROPLET_NAME_PREFIX);
        paramsMap.put(WebConstants.DROPLET_NAME_PREFIX, dropletNamePrefix);

        String instancesNumberLimit = cloudClientParameters.getParameter(WebConstants.INSTANCES_COUNT_LIMIT);
        paramsMap.put(WebConstants.INSTANCES_COUNT_LIMIT, instancesNumberLimit);

        return new DOSettings(paramsMap);
    }
}
