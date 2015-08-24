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
package io.cyberstock.tcdop.server.integration.digitalocean.storage

import com.myjeeva.digitalocean.pojo.Image
import io.cyberstock.tcdop.model.error.DOError
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService
import io.cyberstock.tcdop.server.integration.digitalocean.DOAsyncClientServiceFactory
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientServiceFactory
import io.cyberstock.tcdop.server.integration.digitalocean.storage.impl.CloudImageStorageFactoryImpl
import io.cyberstock.tcdop.server.integration.digitalocean.storage.impl.CloudImageStorageImpl
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudInstance
import org.testng.annotations.Test

import java.util.concurrent.Executors

import static org.testng.Assert.assertEquals
import static org.testng.Assert.assertNotNull
import static org.testng.Assert.assertTrue

/**
 * Created by beolnix on 02/08/15.
 */
class CloudImageStorageTestCase {

    private final static Integer IMAGE_ID = Random.newInstance().nextInt()

    @Test(expectedExceptions = [DOError.class])
    public void testInitializationThreshold() {
        def storage = new CloudImageStorageImpl(null, null, -1)
        storage.waitInitialization()
    }

    @Test
    public void testMerge() {
        def storage = new CloudImageStorageImpl(null, null, 10)

        def A = new DOCloudImage(new Image(id:234, name: "test"))
        def B = new DOCloudImage(new Image(id:234, name: "test2"))

        def oldMap = [:]
        oldMap[A.id] = A

        def newMap = [:]
        newMap[B.id] = B

        storage.mergeOldImagesToNewImagesMap(newMap, oldMap)
        assertEquals(storage.imageMap, oldMap)
    }

    @Test
    public void testGetById() {
        def executorService = Executors.newFixedThreadPool(1)
        def factory = new CloudImageStorageFactoryImpl(new DOClientServiceFactoryTestImpl())
        def storage = factory.createStorage(executorService, "test")

        storage.waitInitialization();

        assertNotNull(storage.getImageById(IMAGE_ID.toString()))
    }


    @Test
    public void getImagesTest() {
        def executorService = Executors.newFixedThreadPool(1)
        def factory = new CloudImageStorageFactoryImpl(new DOClientServiceFactoryTestImpl())
        def storage = factory.createStorage(executorService, "test")

        storage.waitInitialization();

        def map = storage.getImagesList()

        assertNotNull(map)
        assertTrue(map.size() > 0)
    }

    @Test
    public void instancesCountTest() {
        def executorService = Executors.newFixedThreadPool(1)
        def factory = new CloudImageStorageFactoryImpl(new DOClientServiceFactoryTestImpl())
        def storage = factory.createStorage(executorService, "test")

        storage.waitInitialization();

        def count = storage.getInstancesCount()

        assertEquals(count, 1)
    }

    public static class DOClientServiceFactoryTestImpl implements DOClientServiceFactory {
        @Override
        DOClientService createClient(String token) {

            return [getImages: {
                def doImage = new Image(id: CloudImageStorageTestCase.IMAGE_ID, name: "test")
                def image = new DOCloudImage(doImage)

                image.addInstance(new DOCloudInstance(image, "123", "test"))

                return [image]
            }] as DOClientService

        }
    }
}
