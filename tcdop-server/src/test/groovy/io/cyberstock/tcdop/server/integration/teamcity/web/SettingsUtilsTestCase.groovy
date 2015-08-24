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
package io.cyberstock.tcdop.server.integration.teamcity.web

import io.cyberstock.tcdop.model.DOConfigConstants
import io.cyberstock.tcdop.model.DOIntegrationMode
import io.cyberstock.tcdop.model.DOSettings
import io.cyberstock.tcdop.model.DropletSize
import io.cyberstock.tcdop.model.WebConstants
import jetbrains.buildServer.clouds.CloudClientParameters
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

/**
 * Created by beolnix on 16/08/15.
 */
class SettingsUtilsTestCase {

    @Test
    public void convertToDOSettingsTest() {
        def CloudClientParameters params = new CloudClientParameters()
        params.myParams.putAll(getParametersMap())

        DOSettings doSettings = DOSettingsUtils.convertToDOSettings(params)

        assertEquals(doSettings.mode, DOIntegrationMode.PREPARED_IMAGE)
        assertEquals(doSettings.instancesLimit, 5)
        assertEquals(doSettings.token, "test")
        assertEquals(doSettings.size, DropletSize.M32GB)
        assertEquals(doSettings.imageName, "name")
        assertEquals(doSettings.dropletNamePrefix, "PREFIX")
    }


    @Test
    public void convertToDOSettingsTes2() {

        DOSettings doSettings = DOSettingsUtils.convertToDOSettings(getParametersMap())

        assertEquals(doSettings.mode, DOIntegrationMode.PREPARED_IMAGE)
        assertEquals(doSettings.instancesLimit, 5)
        assertEquals(doSettings.token, "test")
        assertEquals(doSettings.size, DropletSize.M32GB)
        assertEquals(doSettings.imageName, "name")
        assertEquals(doSettings.dropletNamePrefix, "PREFIX")
    }

    def getParametersMap() {
        def params = [:]
        params[WebConstants.DO_INTEGRATION_MODE] = DOConfigConstants.PREPARED_IMAGE_MODE_CODE
        params[WebConstants.INSTANCES_COUNT_LIMIT] = '5'
        params[WebConstants.TOKEN] = 'test'
        params[WebConstants.DROPLET_SIZE] = '32GB'
        params[WebConstants.IMAGE_NAME] = 'name'
        params[WebConstants.DROPLET_NAME_PREFIX] = "PREFIX"
        params
    }
}
