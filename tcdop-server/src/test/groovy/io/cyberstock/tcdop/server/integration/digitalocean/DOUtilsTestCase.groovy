package io.cyberstock.tcdop.server.integration.digitalocean

import com.myjeeva.digitalocean.DigitalOcean
import com.myjeeva.digitalocean.impl.DigitalOceanClient
import com.myjeeva.digitalocean.pojo.Droplet
import com.myjeeva.digitalocean.pojo.Image
import com.myjeeva.digitalocean.pojo.Region
import io.cyberstock.tcdop.model.DOConfigConstants
import io.cyberstock.tcdop.model.DOSettings
import io.cyberstock.tcdop.model.WebConstants
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage
import io.cyberstock.tcdop.server.integration.teamcity.web.DOSettingsUtils
import org.testng.annotations.Test

import static org.testng.Assert.assertNotNull;

/**
 * Created by beolnix on 08/08/15.
 */
class DOUtilsTestCase {

    def client = [createDroplet: { droplet ->
        return new Droplet()
    }] as DigitalOcean

    @Test
    public void createInstanceTest() {


        DOSettings settings = DOSettingsUtils.convertToDOSettings(getParametersMap())
        DOCloudImage cloudImage = new DOCloudImage(
                new Image(id: 123,
                        name: "test",
                        minDiskSize: settings.getSize().diskSize,
                        slug: "deb",
                        regions: ["test"]
                        )
                )


        Droplet droplet = DOUtils.createInstance(client, settings, cloudImage)
        assertNotNull(droplet)
    }

    def getParametersMap() {
        def params = [:]
        params[WebConstants.DO_INTEGRATION_MODE] = DOConfigConstants.PREPARED_IMAGE_MODE_CODE
        params[WebConstants.INSTANCES_COUNT_LIMIT] = '5'
        params[WebConstants.TOKEN] = 'test'
        params[WebConstants.DROPLET_SIZE] = '32GB'
        params[WebConstants.IMAGE_NAME] = 'name'
        params[WebConstants.DROPLET_NAME_PREFIX] = 'prefix'
        params
    }
}
