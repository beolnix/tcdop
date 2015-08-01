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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DOSettings that = (DOSettings) o;

        if (dropletNamePrefix != null ? !dropletNamePrefix.equals(that.dropletNamePrefix) : that.dropletNamePrefix != null)
            return false;
        if (imageName != null ? !imageName.equals(that.imageName) : that.imageName != null) return false;
        if (instancesLimit != null ? !instancesLimit.equals(that.instancesLimit) : that.instancesLimit != null)
            return false;
        if (mode != that.mode) return false;
        if (size != that.size) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mode != null ? mode.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (imageName != null ? imageName.hashCode() : 0);
        result = 31 * result + (instancesLimit != null ? instancesLimit.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (dropletNamePrefix != null ? dropletNamePrefix.hashCode() : 0);
        return result;
    }
}
