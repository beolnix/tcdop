package io.cyberstock.tcdop.model;


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
    private String region;
    private String RsaKeyName;
    private String publicRsaKey;
    private String privateRsaKey;
    private String osImageName;

    public DOSettings(DOIntegrationMode mode, String token, String imageName, Integer instancesLimit, DropletSize size, String dropletNamePrefix, String region, String rsaKeyName, String publicRsaKey, String privateRsaKey, String osImageName) {
        this.mode = mode;
        this.token = token;
        this.imageName = imageName;
        this.instancesLimit = instancesLimit;
        this.size = size;
        this.dropletNamePrefix = dropletNamePrefix;
        this.region = region;
        RsaKeyName = rsaKeyName;
        this.publicRsaKey = publicRsaKey;
        this.privateRsaKey = privateRsaKey;
        this.osImageName = osImageName;
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

    public String getRegion() {
        return region;
    }

    public String getPublicRsaKey() {
        return publicRsaKey;
    }

    public String getPrivateRsaKey() {
        return privateRsaKey;
    }

    public String getOsImageName() {
        return osImageName;
    }

    public String getRsaKeyName() {
        return RsaKeyName;
    }

    @Override
    public String toString() {
        return "DOSettings{" +
                "mode=" + mode +
                ", token='" + token + '\'' +
                ", imageName='" + imageName + '\'' +
                ", instancesLimit=" + instancesLimit +
                ", size=" + size +
                ", dropletNamePrefix='" + dropletNamePrefix + '\'' +
                ", region='" + region + '\'' +
                ", RsaKeyName='" + RsaKeyName + '\'' +
                ", publicRsaKey='" + publicRsaKey + '\'' +
                ", privateRsaKey='" + privateRsaKey + '\'' +
                ", osImageName='" + osImageName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DOSettings that = (DOSettings) o;

        if (RsaKeyName != null ? !RsaKeyName.equals(that.RsaKeyName) : that.RsaKeyName != null) return false;
        if (dropletNamePrefix != null ? !dropletNamePrefix.equals(that.dropletNamePrefix) : that.dropletNamePrefix != null)
            return false;
        if (imageName != null ? !imageName.equals(that.imageName) : that.imageName != null) return false;
        if (instancesLimit != null ? !instancesLimit.equals(that.instancesLimit) : that.instancesLimit != null)
            return false;
        if (mode != that.mode) return false;
        if (osImageName != null ? !osImageName.equals(that.osImageName) : that.osImageName != null) return false;
        if (privateRsaKey != null ? !privateRsaKey.equals(that.privateRsaKey) : that.privateRsaKey != null)
            return false;
        if (publicRsaKey != null ? !publicRsaKey.equals(that.publicRsaKey) : that.publicRsaKey != null) return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;
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
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (RsaKeyName != null ? RsaKeyName.hashCode() : 0);
        result = 31 * result + (publicRsaKey != null ? publicRsaKey.hashCode() : 0);
        result = 31 * result + (privateRsaKey != null ? privateRsaKey.hashCode() : 0);
        result = 31 * result + (osImageName != null ? osImageName.hashCode() : 0);
        return result;
    }
}
