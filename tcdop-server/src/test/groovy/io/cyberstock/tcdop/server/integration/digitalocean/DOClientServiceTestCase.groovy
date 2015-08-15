package io.cyberstock.tcdop.server.integration.digitalocean

import com.myjeeva.digitalocean.pojo.Account
import com.myjeeva.digitalocean.pojo.Droplet
import com.myjeeva.digitalocean.pojo.Image
import io.cyberstock.tcdop.model.DOConfigConstants
import io.cyberstock.tcdop.model.WebConstants
import io.cyberstock.tcdop.model.error.DOError
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.DOAdapter
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.impl.DOAdapterImpl
import io.cyberstock.tcdop.server.integration.digitalocean.impl.DOClientServiceImpl
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudInstance
import io.cyberstock.tcdop.server.integration.teamcity.web.DOSettingsUtils
import org.testng.annotations.Test

import static org.testng.Assert.assertTrue
import static org.testng.Assert.assertEquals
import static org.testng.Assert.assertNotNull

/**
 * Created by beolnix on 15/08/15.
 */
class DOClientServiceTestCase {

    @Test
    public void restartInstanceTest() {
        def instance = new DOCloudInstance(new DOCloudImage(new Image(id: 123)), "123", "test")
        def match = false

        def adapter = [
                restartInstance: { instanceId ->
                    if (instanceId == Integer.parseInt(instance.getInstanceId())){
                        match = true
                    }
                    return new Date()
                }] as DOAdapter

        DOClientServiceImpl service = new DOClientServiceImpl(adapter);
        service.restartInstance(instance)

        assertTrue(match)
        assertNotNull(instance.getStartedTime())
    }

    @Test
    public void waitForInitializationTest() {
        def instance = new DOCloudInstance(new DOCloudImage(new Image(id: 123)), "123", "test")
        def match = false
        def ipv4 = "127.0.0.1"

        def adapter = [
                waitForDropletInitialization: { instanceId ->

                    if (instanceId == Integer.parseInt(instance.getInstanceId())){
                        match = true
                    }

                    return "127.0.0.1"
                }] as DOAdapter


        DOClientServiceImpl service = new DOClientServiceImpl(adapter);
        service.waitInstanceInitialization(instance)

        assertTrue(match)
        assertEquals(instance.getNetworkIdentity(), ipv4)
    }

    @Test
    public void terminateInstanceTest() {
        def instance = new DOCloudInstance(new DOCloudImage(new Image(id: 123)), "123", "test")
        instance.image.instancesMap.put("123", instance)
        def match = false

        def adapter = [
                terminateInstance: { instanceId ->
                    if (instanceId == Integer.parseInt(instance.getInstanceId())){
                        match = true
                    }
                    return true
                }] as DOAdapter


        DOClientServiceImpl service = new DOClientServiceImpl(adapter);

        assertTrue(instance.image.instances.size() == 1)
        service.terminateInstance(instance)

        assertTrue(match)
        assertTrue(instance.image.instances.size() == 0)
    }

    @Test
    public void createInstance() {
        def originalImage = new DOCloudImage(new Image(id: 123))
        def settings = DOSettingsUtils.convertToDOSettings(getParametersMap())
        def droplet = new Droplet(id: 123, name: "test")

        def adapter = [
                createInstance: { doSettings, image ->
                    return droplet
                }] as DOAdapter


        DOClientServiceImpl service = new DOClientServiceImpl(adapter);
        DOCloudInstance instance = service.createInstance(originalImage, settings)

        assertEquals(instance.name, droplet.name)
        assertTrue(instance.image.instances.size() == 1)
    }

    @Test
    public void accountCheckTestPositive() {
        def match = false

        def adapter = [
                checkAccount: {
                    match = true
                    return new Account()
                }] as DOAdapter

        DOClientServiceImpl service = new DOClientServiceImpl(adapter);
        service.accountCheck()
        assertTrue(match)
    }

    @Test(expectedExceptions = [DOError.class])
    public void accountCheckTestNegative() {
        def adapter = [
                checkAccount: {
                    return null
                }] as DOAdapter

        DOClientServiceImpl service = new DOClientServiceImpl(adapter);
        service.accountCheck()
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
