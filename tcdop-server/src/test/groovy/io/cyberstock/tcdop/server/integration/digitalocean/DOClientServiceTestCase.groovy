package io.cyberstock.tcdop.server.integration.digitalocean

import com.myjeeva.digitalocean.pojo.Image
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.DOAdapter
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.impl.DOAdapterImpl
import io.cyberstock.tcdop.server.integration.digitalocean.impl.DOClientServiceImpl
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudInstance
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

}
