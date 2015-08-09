package io.cyberstock.tcdop.server.integration.digitalocean

import com.myjeeva.digitalocean.DigitalOcean
import com.myjeeva.digitalocean.common.ActionStatus
import com.myjeeva.digitalocean.common.DropletStatus
import com.myjeeva.digitalocean.impl.DigitalOceanClient
import com.myjeeva.digitalocean.pojo.Account
import com.myjeeva.digitalocean.pojo.Action
import com.myjeeva.digitalocean.pojo.Droplet
import com.myjeeva.digitalocean.pojo.Image
import com.myjeeva.digitalocean.pojo.Network
import com.myjeeva.digitalocean.pojo.Networks
import com.myjeeva.digitalocean.pojo.Region
import io.cyberstock.tcdop.model.DOConfigConstants
import io.cyberstock.tcdop.model.DOSettings
import io.cyberstock.tcdop.model.WebConstants
import io.cyberstock.tcdop.model.error.DOError
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage
import io.cyberstock.tcdop.server.integration.teamcity.web.DOSettingsUtils
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals
import static org.testng.Assert.assertEqualsNoOrder
import static org.testng.Assert.assertNotNull
import static org.testng.Assert.assertTrue;

/**
 * Created by beolnix on 08/08/15.
 */
class DOUtilsTestCase {

    def client = [createDroplet: { droplet ->
        return droplet
    }] as DigitalOcean

    def PREFIX = "prefix"


    @Test
    public void createInstanceTest() {
        DOSettings settings = DOSettingsUtils.convertToDOSettings(getParametersMap())
        def IMAGE_ID = 123
        def REGION_SLUG = "test"

        DOCloudImage cloudImage = new DOCloudImage(
                new Image(id: IMAGE_ID,
                        name: "test",
                        minDiskSize: settings.getSize().diskSize,
                        slug: "deb",
                        regions: [REGION_SLUG]
                        )
                )


        Droplet droplet = DOUtils.createInstance(client, settings, cloudImage)

        assertNotNull(droplet)
        assertTrue(droplet.name.startsWith(PREFIX))
        assertEquals(droplet.size, settings.getSize().slug)
        assertEquals(droplet.image.id, IMAGE_ID)
        assertEquals(droplet.region.slug, REGION_SLUG)
    }

    @Test
    public void getAccountTest() {
        def getAccountInfoExecuted = false
        def client = [getAccountInfo: {
            getAccountInfoExecuted = true
            return new Account()
        }] as DigitalOcean

        Account account = DOUtils.checkAccount(client)

        assertTrue(getAccountInfoExecuted)
        assertNotNull(account)
    }

    @Test
    public void waitForDropletInitializationTestPositive() {
        def client = [getDropletInfo: {
            def droplet = new Droplet()
            droplet.status = DropletStatus.ACTIVE

            def networks = new Networks()
            networks.version4Networks = [new Network(ipAddress: "127.0.0.1")]
            droplet.networks = networks

            return droplet
        }] as DigitalOcean

        String ipv4 = DOUtils.waitForDropletInitialization(client, 123)

        assertNotNull(ipv4)
    }

    @Test(expectedExceptions = [DOError])
    public void waitForDropletInitializationTestPositiveNegative1() {
        def client = [getDropletInfo: {
            def droplet = new Droplet()
            droplet.status = DropletStatus.ARCHIVE

            def networks = new Networks()
            networks.version4Networks = [new Network(ipAddress: "127.0.0.1")]
            droplet.networks = networks

            return droplet
        }] as DigitalOcean

        String ipv4 = DOUtils.waitForDropletInitialization(client, 123, -1L, 1)

        assertNotNull(ipv4)
    }

    @Test
    public void waitForActionResultTestPositive() {
        def client = [getActionInfo: { id ->
            def action = new Action(id: id)
            action.completedAt = new Date()
            action.status = ActionStatus.COMPLETED
            return action
        }] as DigitalOcean

        Date endDate = DOUtils.waitForActionResult(client, new Action(id: 1), 500L, 1)
        assertNotNull(endDate)
    }

    def getParametersMap() {
        def params = [:]
        params[WebConstants.DO_INTEGRATION_MODE] = DOConfigConstants.PREPARED_IMAGE_MODE_CODE
        params[WebConstants.INSTANCES_COUNT_LIMIT] = '5'
        params[WebConstants.TOKEN] = 'test'
        params[WebConstants.DROPLET_SIZE] = '32GB'
        params[WebConstants.IMAGE_NAME] = 'name'
        params[WebConstants.DROPLET_NAME_PREFIX] = PREFIX
        params
    }
}
