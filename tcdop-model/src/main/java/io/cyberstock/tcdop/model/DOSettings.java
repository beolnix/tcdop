package io.cyberstock.tcdop.model;


import java.util.Map;

/**
 * Created by beolnix on 16/05/15.
 */
public class DOSettings {

    private DOIntegrationMode mode;
    private String token;
    private String imageName;
    private Integer instancesLimit;
    private DropletSize size;
    private String dropletNamePrefix;

    public DOSettings(DOIntegrationMode mode, String token, String imageName, Integer instancesLimit, DropletSize size, String dropletNamePrefix) {
        this.mode = mode;
        this.token = token;
        this.imageName = imageName;
        this.instancesLimit = instancesLimit;
        this.size = size;
        this.dropletNamePrefix = dropletNamePrefix;
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
                "mode=" + mode +
                ", token='" + token + '\'' +
                ", imageName='" + imageName + '\'' +
                ", instancesLimit=" + instancesLimit +
                ", size=" + size +
                '}';
    }
}
