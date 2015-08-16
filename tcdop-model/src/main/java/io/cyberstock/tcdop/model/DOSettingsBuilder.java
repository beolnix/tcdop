package io.cyberstock.tcdop.model;

/**
 * Created by beolnix on 02/08/15.
 */
public class DOSettingsBuilder {

    private DOIntegrationMode mode;
    private String token;
    private String imageName;
    private Integer instancesLimit;
    private DropletSize size;
    private String dropletNamePrefix;
    private String region;
    private String publicRsaKeyPart;
    private String privateRsaKeyPart;
    private String osImageName;
    private String rsaKeyName;

    public DOSettingsBuilder() {}

    public DOSettingsBuilder withMode(DOIntegrationMode mode) {
        this.mode = mode;
        return this;
    }

    public DOSettingsBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    public DOSettingsBuilder withImageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public DOSettingsBuilder withInstancesLimit(Integer instancesLimit) {
        this.instancesLimit = instancesLimit;
        return this;
    }

    public DOSettingsBuilder withSize(DropletSize size) {
        this.size = size;
        return this;
    }

    public DOSettingsBuilder withDropletNamePrefix(String dropletNamePrefix) {
        this.dropletNamePrefix = dropletNamePrefix;
        return this;
    }

    public DOSettingsBuilder withRegion(String region) {
        this.region = region;
        return this;
    }

    public DOSettingsBuilder withPublicRsaKeyPart(String publicRsaKeyPart) {
        this.publicRsaKeyPart = publicRsaKeyPart;
        return this;
    }

    public DOSettingsBuilder withPrivateRsaKeyPart(String privateRsaKeyPart) {
        this.privateRsaKeyPart = privateRsaKeyPart;
        return this;
    }

    public DOSettingsBuilder withOsImageName(String osImageName) {
        this.osImageName = osImageName;
        return this;
    }

    public DOSettingsBuilder withRsaKeyName(String rsaKeyName) {
        this.rsaKeyName = rsaKeyName;
        return this;
    }

    public DOSettings build() {
        return new DOSettings(mode,
                token,
                imageName,
                instancesLimit,
                size,
                dropletNamePrefix,
                region,
                rsaKeyName,
                publicRsaKeyPart,
                privateRsaKeyPart,
                osImageName);
    }
}
