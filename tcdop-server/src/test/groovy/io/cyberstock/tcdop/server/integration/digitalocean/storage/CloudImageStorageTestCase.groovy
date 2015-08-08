package io.cyberstock.tcdop.server.integration.digitalocean.storage

import com.myjeeva.digitalocean.pojo.Image
import io.cyberstock.tcdop.model.DOSettings
import io.cyberstock.tcdop.model.error.DOError
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService
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
        def storage = factory.getStorage(executorService, "test")

        storage.waitInitialization();

        assertNotNull(storage.getImageById(IMAGE_ID.toString()))
    }


    @Test
    public void getImagesTest() {
        def executorService = Executors.newFixedThreadPool(1)
        def factory = new CloudImageStorageFactoryImpl(new DOClientServiceFactoryTestImpl())
        def storage = factory.getStorage(executorService, "test")

        storage.waitInitialization();

        def map = storage.getImagesList()

        assertNotNull(map)
        assertTrue(map.size() > 0)
    }

    @Test
    public void instancesCountTest() {
        def executorService = Executors.newFixedThreadPool(1)
        def factory = new CloudImageStorageFactoryImpl(new DOClientServiceFactoryTestImpl())
        def storage = factory.getStorage(executorService, "test")

        storage.waitInitialization();

        def count = storage.getInstancesCount()

        assertEquals(count, 1)
    }

    public static class DOClientServiceFactoryTestImpl implements DOClientServiceFactory {
        @Override
        DOClientService createClient(String token) {
            return new DOClientService() {
                @Override
                List<DOCloudImage> getImages() {
                    def doImage = new Image(id: CloudImageStorageTestCase.IMAGE_ID, name: "test")
                    def image = new DOCloudImage(doImage)

                    image.addInstance(new DOCloudInstance(image, "123", "test"))

                    return [image]
                }

                @Override
                void waitInstanceInitialization(DOCloudInstance cloudInstance) {

                }

                @Override
                void restartInstance(DOCloudInstance cloudInstance) {

                }

                @Override
                void terminateInstance(DOCloudInstance cloudInstance) {

                }

                @Override
                DOCloudInstance createInstance(DOCloudImage cloudImage, DOSettings doSettings) throws DOError {
                    return null
                }

                @Override
                void accountCheck() throws DOError {

                }

                @Override
                DOCloudImage findImageByName(String imageName) throws DOError {
                    return null
                }
            }
        }
    }
}
