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
