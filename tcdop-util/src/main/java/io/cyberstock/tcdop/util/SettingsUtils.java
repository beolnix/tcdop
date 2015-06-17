package io.cyberstock.tcdop.util;

import io.cyberstock.tcdop.model.DOConfigConstants;
import io.cyberstock.tcdop.model.DOIntegrationMode;
import io.cyberstock.tcdop.model.DOSettings;
import jetbrains.buildServer.clouds.CloudClientParameters;

import java.util.HashMap;

/**
 * Created by beolnix on 18/06/15.
 */
public class SettingsUtils {

    public static DOSettings convertClientParametersToDOSettings(CloudClientParameters cloudClientParameters) {
        HashMap<String, String> paramsMap = new HashMap<String, String>();

        String strMode = cloudClientParameters.getParameter(DOConfigConstants.DO_INTEGRATION_MODE);
        paramsMap.put(DOConfigConstants.DO_INTEGRATION_MODE, strMode);

        String token = cloudClientParameters.getParameter(DOConfigConstants.TOKEN);
        paramsMap.put(DOConfigConstants.TOKEN, token);

        String imageId = cloudClientParameters.getParameter(DOConfigConstants.IMAGE_ID);
        paramsMap.put(DOConfigConstants.IMAGE_ID, imageId);

        return new DOSettings(paramsMap);
    }
}
