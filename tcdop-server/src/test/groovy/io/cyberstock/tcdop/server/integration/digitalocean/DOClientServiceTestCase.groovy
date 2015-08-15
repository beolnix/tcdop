package io.cyberstock.tcdop.server.integration.digitalocean

import com.myjeeva.digitalocean.DigitalOcean
import com.myjeeva.digitalocean.common.ActionStatus
import com.myjeeva.digitalocean.pojo.Action
import com.myjeeva.digitalocean.pojo.Image
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.DOAdapter
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.impl.DOAdapterImpl
import io.cyberstock.tcdop.server.integration.digitalocean.impl.DOClientServiceImpl
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudInstance
import org.testng.annotations.Test

import static org.testng.Assert.assertTrue

/**
 * Created by beolnix on 15/08/15.
 */
class DOClientServiceTestCase {

    @Test
    public void restartInstanceTest() {
        def instance = new DOCloudInstance(new DOCloudImage(new Image(id: 123)), "123", "test")
        def match = false

        def client = [
                rebootDroplet: { dropletId ->
                    if (dropletId == Integer.parseInt(instance.getInstanceId())){
                        match = true
                    }
                    return new Action(id: 1, status: ActionStatus.IN_PROGRESS, completedAt: new Date())
                },
                getActionInfo: { actionId ->
                    return new Action(status: ActionStatus.COMPLETED, completedAt: new Date())
                }
        ] as DigitalOcean

        DOAdapter adapter = new DOAdapterImpl(client, 1, 500L)
        DOClientServiceImpl service = new DOClientServiceImpl(adapter);
        service.restartInstance(instance)

        assertTrue(match)
    }



}
