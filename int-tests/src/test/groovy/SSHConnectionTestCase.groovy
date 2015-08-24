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
import io.cyberstock.tcdop.ssh.SSHClientFactory
import io.cyberstock.tcdop.ssh.SSHClientService
import io.cyberstock.tcdop.ssh.SSHClientServiceFactory
import io.cyberstock.tcdop.ssh.impl.SSHClientFactoryImpl
import io.cyberstock.tcdop.ssh.impl.SSHClientServiceFactoryImpl
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
        String privKey = new File(PRIV_KEY_PATH).getText()

        SSHClientFactory sshClientFactory = new SSHClientFactoryImpl()
        SSHClientServiceFactory clientServiceFactory = new SSHClientServiceFactoryImpl(sshClientFactory)
        SSHClientService clientService = clientServiceFactory.createSSHClientService("test", privKey, ipv4, "root")
        return clientService.executeRemote(command)
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
