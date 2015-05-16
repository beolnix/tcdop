package io.cyberstock.tcdop.server;

import io.cyberstock.tcdop.api.DOConfigConstants;
import jetbrains.buildServer.clouds.CloudClientParameters;

/**
 * Created by beolnix on 16/05/15.
 */
public class DOSettings {

    private String strMode;
    private DOIntegrationMode mode;
    private String token;
    private String imageId;

    public DOSettings(CloudClientParameters cloudClientParameters) {
        this.strMode = cloudClientParameters.getParameter(DOConfigConstants.DO_INTEGRATION_MODE);
        this.mode = DOIntegrationMode.getByCode(strMode);

        this.token = cloudClientParameters.getParameter(DOConfigConstants.TOKEN);
        this.imageId = cloudClientParameters.getParameter(DOConfigConstants.IMAGE_ID);
    }

    public String getStrMode() {
        return strMode;
    }

    public DOIntegrationMode getMode() {
        return mode;
    }

    public String getToken() {
        return token;
    }

    public String getImageId() {
        return imageId;
    }
}
