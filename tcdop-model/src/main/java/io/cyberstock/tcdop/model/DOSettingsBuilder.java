/* The MIT License
 *
 * Copyright (c) 2010-2015 Danila A. (atmakin.dv@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
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
