import com.myjeeva.digitalocean.DigitalOcean
import com.myjeeva.digitalocean.impl.DigitalOceanClient
import io.cyberstock.tcdop.model.WebConstants
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.DOAdapter
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.impl.DOAdapterImpl
import io.cyberstock.tcdop.server.integration.teamcity.web.DOSettingsUtils
import org.testng.annotations.Test

/**
 * Created by beolnix on 16/08/15.
 */
class SSHConnectionTestCase {

    // put token and pub key here for the integration test
    def TOKEN = ""
    def PUB_KEY = ""

    def threashold = 3 * 60 * 1000L
    def RSA_KEY_NAME = "INT_test"

    @Test
    public void test() {
        DigitalOcean doClient = new DigitalOceanClient(TOKEN)
        DOAdapter adapter = new DOAdapterImpl(doClient, 10, threashold)

        def droplet = adapter.createInstance(settings)
        adapter.waitForDropletInitialization(droplet.id)

        //TODO execute uname -a remotely
    }

    def getSettings() {
        def params = [:]
        params[WebConstants.DROPLET_SIZE] = '512MB'
        params[WebConstants.DROPLET_NAME_PREFIX] = "INT"
        params[WebConstants.REGION] = "lon1"
        params[WebConstants.RSA_KEY_NAME] = RSA_KEY_NAME
        params[WebConstants.RSA_PUBLIC_KEY] = PUB_KEY
        params[WebConstants.OS_IMAGE_NAME] = "ubuntu-14-04-x64"
        DOSettingsUtils.convertToDOSettings(params)
    }
}
