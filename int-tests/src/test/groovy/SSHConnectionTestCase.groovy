import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.myjeeva.digitalocean.DigitalOcean
import com.myjeeva.digitalocean.impl.DigitalOceanClient
import io.cyberstock.tcdop.model.WebConstants
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.DOAdapter
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.impl.DOAdapterImpl
import io.cyberstock.tcdop.server.integration.teamcity.web.DOSettingsUtils
import org.testng.annotations.BeforeSuite
import org.testng.annotations.Test

import static org.testng.Assert.assertTrue
import static org.testng.Assert.assertNotNull

/**
 * Created by beolnix on 16/08/15.
 */
class SSHConnectionTestCase {

    def threashold = 5 * 60 * 1000L
    def RSA_KEY_NAME = "INT_test"

    def RSA_PRIV_KEY_PATH_PROP = "RSA_PRIV_KEY_PATH"
    def RSA_PUB_KEY_PROP = "RSA_PUB_KEY"
    def TOKEN_PROP = "DO_TOKEN"

    def TOKEN = ""              //Digital ocean token must be here
    def PUB_KEY = ""            //content of rsa public key must be here
    def PRIV_KEY_PATH = ""      //path to the rsa private key must be here

    @BeforeSuite
    public void before() {
        assertNotNull(System.getenv(RSA_PRIV_KEY_PATH_PROP))
        assertNotNull(System.getenv(RSA_PUB_KEY_PROP))
        assertNotNull(System.getenv(TOKEN_PROP))

        TOKEN = System.getenv(TOKEN_PROP)
        PUB_KEY = System.getenv(RSA_PUB_KEY_PROP)
        PRIV_KEY_PATH = System.getenv(RSA_PRIV_KEY_PATH_PROP)
    }

    /**
     * Integration test
     *  - creates empty ubuntu based instance with RSA key
     *  - executes uname -a on created instance
     *  - checks that output of uname -a contains instance name
     */
    @Test
    public void test() {
        DigitalOcean doClient = new DigitalOceanClient(TOKEN)
        DOAdapter adapter = new DOAdapterImpl(doClient, 10, threashold)

        def droplet = adapter.createInstance(settings)
        String ipv4 = adapter.waitForDropletInitialization(droplet.id)

        assertNotNull(ipv4)

        println "ipv4: " + ipv4

        try {
            Thread.sleep(10 * 1000) // wait system initialization
            def result = executeRemote(ipv4, "uname -a")

            assertNotNull(result)

            println "output: " + result

            //validate output
            assertTrue(result.contains(droplet.getName()))
        } finally {
            def deleted = adapter.terminateInstance(droplet.id)
            assertTrue(deleted)
        }

    }

    def executeRemote(String ipv4, String command) {
        JSch jsch = new JSch();

        String user = "root";
        String host = ipv4;

        jsch.addIdentity(PRIV_KEY_PATH)
        Session session = jsch.getSession(user, host)

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect()
        def result = runSshCommand(command, session)
        session.disconnect()

        return result
    }

    def runSshCommand(String command, Session session) {
        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.connect();

            InputStream inc = channel.getInputStream();

            byte[] tmp = new byte[1024];
            StringBuilder resultBuilder = new StringBuilder()
            while (true) {
                while (inc.available() > 0) {
                    int i = inc.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    resultBuilder.append(new String(tmp, 0, i))
                }
                if (channel.isClosed()) {
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (Exception ee) {

                }
            }
            channel.disconnect();
            return resultBuilder.toString()
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
