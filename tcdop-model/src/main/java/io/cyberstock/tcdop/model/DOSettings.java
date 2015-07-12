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
    private Integer instancesLimit;
    private DropletSize size;
    private String dropletNamePrefix;


    public DOSettings(Map<String, String> stringStringMap) {
        this.strMode = stringStringMap.get(WebConstants.DO_INTEGRATION_MODE);
        this.mode = DOIntegrationMode.getByCode(strMode);

        this.token = stringStringMap.get(WebConstants.TOKEN);
        this.imageName = stringStringMap.get(WebConstants.IMAGE_NAME);
        if (stringStringMap.get(WebConstants.INSTANCES_COUNT_LIMIT) != null) {
            this.instancesLimit = Integer.parseInt(stringStringMap.get(WebConstants.INSTANCES_COUNT_LIMIT));
        }

        String sizeSlug = stringStringMap.get(WebConstants.DROPLET_SIZE);
        this.size = DropletSize.resolveBySlug(sizeSlug);

        this.dropletNamePrefix = stringStringMap.get(WebConstants.DROPLET_NAME_PREFIX);
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

    public String getDropletNamePrefix() {
        return dropletNamePrefix;
    }

    public Integer getInstancesLimit() {
        return instancesLimit;
    }

    public DropletSize getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "DOSettings{" +
                "strMode='" + strMode + '\'' +
                ", mode=" + mode +
                ", token='" + token + '\'' +
                ", imageName='" + imageName + '\'' +
                ", instancesLimit=" + instancesLimit +
                ", size=" + size +
                '}';
    }
}
