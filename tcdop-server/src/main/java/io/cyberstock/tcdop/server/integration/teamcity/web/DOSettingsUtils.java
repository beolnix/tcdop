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
package io.cyberstock.tcdop.server.integration.teamcity.web;

import io.cyberstock.tcdop.model.*;
import jetbrains.buildServer.clouds.CloudClientParameters;

import java.util.Map;

/**
 * Created by beolnix on 18/06/15.
 */
public class DOSettingsUtils {

    private DOSettingsUtils() {}

    public static DOSettings convertToDOSettings(CloudClientParameters cloudClientParameters) {
        DOSettingsBuilder builder = new DOSettingsBuilder();

//        String strMode = cloudClientParameters.getParameter(WebConstants.DO_INTEGRATION_MODE);
        builder.withMode(DOIntegrationMode.PREPARED_IMAGE);

        String token = cloudClientParameters.getParameter(WebConstants.TOKEN);
        builder.withToken(token);

        String imageName = cloudClientParameters.getParameter(WebConstants.IMAGE_NAME);
        builder.withImageName(imageName);

        String sizeSlug = cloudClientParameters.getParameter(WebConstants.DROPLET_SIZE);
        if (sizeSlug != null) {
            //incorrect format and null value should be intercepted by validator of user input
            builder.withSize(DropletSize.resolveBySlug(sizeSlug));
        }

        String dropletNamePrefix = cloudClientParameters.getParameter(WebConstants.DROPLET_NAME_PREFIX);
        builder.withDropletNamePrefix(dropletNamePrefix);

        String instancesNumberLimit = cloudClientParameters.getParameter(WebConstants.INSTANCES_COUNT_LIMIT);
        if (instancesNumberLimit != null) {
            //incorrect format and null value should be intercepted by validator of user input
            builder.withInstancesLimit(Integer.parseInt(instancesNumberLimit));
        }

        String region = cloudClientParameters.getParameter(WebConstants.REGION);
        builder.withRegion(region);

        String rsaPublicKey = cloudClientParameters.getParameter(WebConstants.RSA_PUBLIC_KEY);
        builder.withPublicRsaKeyPart(rsaPublicKey);

        String rsaPrivateKey = cloudClientParameters.getParameter(WebConstants.RSA_PRIVATE_KEY);
        builder.withPrivateRsaKeyPart(rsaPrivateKey);

        String osImageName = cloudClientParameters.getParameter(WebConstants.OS_IMAGE_NAME);
        builder.withOsImageName(osImageName);

        String rsaKeyName = cloudClientParameters.getParameter(WebConstants.RSA_KEY_NAME);
        builder.withRsaKeyName(rsaKeyName);

        return builder.build();
    }

    public static DOSettings convertToDOSettings(Map<String, String> stringStringMap) {
        DOSettingsBuilder builder = new DOSettingsBuilder();

//        String strMode = stringStringMap.get(WebConstants.DO_INTEGRATION_MODE);
        builder.withMode(DOIntegrationMode.PREPARED_IMAGE);

        builder.withToken(stringStringMap.get(WebConstants.TOKEN));
        builder.withImageName(stringStringMap.get(WebConstants.IMAGE_NAME));

        String instancesLimitStr = stringStringMap.get(WebConstants.INSTANCES_COUNT_LIMIT);
        if (instancesLimitStr != null) {
            //incorrect format and null value should be intercepted by validator of user input
            builder.withInstancesLimit(Integer.parseInt(instancesLimitStr));
        }

        String sizeSlugStr = stringStringMap.get(WebConstants.DROPLET_SIZE);
        if (sizeSlugStr != null) {
            //incorrect format and null value should be intercepted by validator of user input
            builder.withSize(DropletSize.resolveBySlug(sizeSlugStr));
        }

        builder.withDropletNamePrefix(stringStringMap.get(WebConstants.DROPLET_NAME_PREFIX));
        builder.withRegion(stringStringMap.get(WebConstants.REGION));
        builder.withPublicRsaKeyPart(stringStringMap.get(WebConstants.RSA_PUBLIC_KEY));
        builder.withPrivateRsaKeyPart(stringStringMap.get(WebConstants.RSA_PRIVATE_KEY));
        builder.withOsImageName(stringStringMap.get(WebConstants.OS_IMAGE_NAME));
        builder.withRsaKeyName(stringStringMap.get(WebConstants.RSA_KEY_NAME));

        return builder.build();
    }
}
