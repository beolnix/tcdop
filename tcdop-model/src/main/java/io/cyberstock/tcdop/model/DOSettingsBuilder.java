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

    public DOSettings build() {
        return new DOSettings(mode, token, imageName, instancesLimit, size, dropletNamePrefix);
    }
}
