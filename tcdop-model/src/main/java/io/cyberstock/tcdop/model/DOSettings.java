package io.cyberstock.tcdop.model;


import java.util.Map;

/**
 * Created by beolnix on 16/05/15.
 */
public class DOSettings {

    private String strMode;
    private DOIntegrationMode mode;
    private String token;
    private String imageName;
    private DropletConfig dropletConfig;
    private Integer instancesLimit = 4;


    public DOSettings(Map<String, String> stringStringMap) {
        this.strMode = stringStringMap.get(DOConfigConstants.DO_INTEGRATION_MODE);
        this.mode = DOIntegrationMode.getByCode(strMode);

        this.token = stringStringMap.get(DOConfigConstants.TOKEN);
        this.imageName = stringStringMap.get(DOConfigConstants.IMAGE_NAME);
        if (stringStringMap.get(DOConfigConstants.INSTANCES_COUNT_LIMIT) != null) {
            this.instancesLimit = Integer.parseInt(stringStringMap.get(DOConfigConstants.INSTANCES_COUNT_LIMIT));
        }

        this.dropletConfig = new DropletConfig(imageName);
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

    public String getImageName() {
        return imageName;
    }

    public boolean isPreparedInstanceMode() {
        return DOIntegrationMode.PREPARED_IMAGE.equals(mode);
    }

    public DropletConfig getDropletConfig() {
        return dropletConfig;
    }

    public Integer getInstancesLimit() {
        return instancesLimit;
    }

    @Override
    public String toString() {
        return "DOSettings{" +
                "strMode='" + strMode + '\'' +
                ", mode=" + mode +
                ", token='" + token + '\'' +
                ", imageName='" + imageName + '\'' +
                ", dropletConfig=" + dropletConfig +
                ", instancesLimit=" + instancesLimit +
                '}';
    }
}
