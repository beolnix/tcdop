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
package io.cyberstock.tcdop.server.integration.digitalocean

import com.google.common.base.Optional
import com.myjeeva.digitalocean.pojo.Account
import com.myjeeva.digitalocean.pojo.Droplet
import com.myjeeva.digitalocean.pojo.Image
import com.myjeeva.digitalocean.pojo.Network
import com.myjeeva.digitalocean.pojo.Networks
import io.cyberstock.tcdop.model.DOConfigConstants
import io.cyberstock.tcdop.model.WebConstants
import io.cyberstock.tcdop.model.error.DOError
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.DOAdapter
import io.cyberstock.tcdop.server.integration.digitalocean.impl.DOClientServiceImpl
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudInstance
import io.cyberstock.tcdop.server.integration.teamcity.web.DOSettingsUtils
import jetbrains.buildServer.clouds.InstanceStatus
import org.testng.annotations.Test

import static org.testng.Assert.assertEqualsNoOrder
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
        assertEquals(instance.image.instances.size(), 0)
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
        assertEquals(instance.image.instances.size(), 1)
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

    @Test
    public void findImageByNameTestPositive() {
        def originalName = "test"
        def image = new Image(id: 123)
        def match = false

        def adapter = [
                findImageByName: { name ->
                    if (name == originalName) {
                        match = true
                    }

                    return Optional.of(image)
                }] as DOAdapter

        DOClientServiceImpl service = new DOClientServiceImpl(adapter);
        DOCloudImage cloudImage = service.findImageByName(originalName)

        assertTrue(match)
        assertNotNull(cloudImage)
    }

    @Test(expectedExceptions = [DOError.class])
    public void findImageByNameTestNegative() {

        def adapter = [
                findImageByName: { name ->
                    return Optional.absent()
                }] as DOAdapter

        DOClientServiceImpl service = new DOClientServiceImpl(adapter);
        service.findImageByName("test")
    }

    @Test
    public void getImagesTest() {
        def image = new Image(id: 123, name: "test")
        def ipv4 = "127.0.0.1"

        def droplet = new Droplet(id: 123, name: "test", image: image)
        def networks = new Networks()
        networks.version4Networks = [new Network(ipAddress: ipv4)]
        droplet.networks = networks

        def adapter = [
                getImages: {
                    return [image]
                },
                getDroplets: {
                    return [droplet]
                },
                transformStatus: {
                    return InstanceStatus.RUNNING
                }] as DOAdapter

        DOClientServiceImpl service = new DOClientServiceImpl(adapter);
        def images = service.getImages()

        assertNotNull(images)
        assertEquals(images.size(), 1)
        assertEquals(images[0].instances.size(), 1)

        assertEquals(images[0].instances[0].networkIdentity, ipv4)
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
