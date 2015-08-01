package io.cyberstock.tcdop.server.integration.teamcity.web

import com.myjeeva.digitalocean.pojo.Image
import io.cyberstock.tcdop.model.DOConfigConstants
import io.cyberstock.tcdop.model.DOSettings
import io.cyberstock.tcdop.model.DropletSize
import io.cyberstock.tcdop.model.WebConstants
import io.cyberstock.tcdop.model.error.DOError
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientServiceFactory
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudInstance
import org.testng.annotations.Test


public class ConfigurationValidatorTestCase {

    ConfigurationValidator successValidator = new ConfigurationValidator(new SuccessClientFactory())
    ConfigurationValidator emptyValidator = new ConfigurationValidator(new EmptyClientFactory())
    ConfigurationValidator failureValidator = new ConfigurationValidator(new FailureClientFactory())

    @Test
    public void testTokenCheckPositive() {
        def result = emptyValidator.validateToken("test")
        assert result.size() == 0
    }

    @Test
    public void testTokenCheckNegative() {
        def result = failureValidator.validateToken("test")
        assert result.size() == 1
    }

    @Test
    public void testImagesNegative() {
        def result = failureValidator.validateImage("test", DropletSize.M1GB, "test")
        assert result.size() == 1
    }

    @Test
    public void testImagesNegativeWrongDiskSize() {
        def result = successValidator.validateImage("test", DropletSize.M16GB, "test")
        assert result.size() == 1
    }

    @Test
    public void testImagesPositive() {
        def result = successValidator.validateImage("test", DropletSize.M32GB, "test")
        assert result.size() == 0
    }

    @Test
    public void testCheckNotNull() {
        def result = emptyValidator.checkNotNull(getParametersMap(), WebConstants.DO_INTEGRATION_MODE)
        assert result.size() == 0
    }

    @Test
    def void testCheckNotNull2() {
        def result = emptyValidator.checkNotNull(getEmptyParametersMap(), WebConstants.DO_INTEGRATION_MODE)

        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.DO_INTEGRATION_MODE
    }

    @Test
    def void testCheckInstancesLimitFormat() {
        def result = emptyValidator.checkInstancesLimitFormat(getParametersMap())
        assert result.size() == 0
    }

    @Test
    def void testCheckInstancesLimitFormat2() {
        def result = emptyValidator.checkInstancesLimitFormat(getEmptyParametersMap())

        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.INSTANCES_COUNT_LIMIT
    }

    @Test
    def void testCheckInstancesLimitFormat3() {
        def result = emptyValidator.checkInstancesLimitFormat(getWrongParametersMap())

        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.INSTANCES_COUNT_LIMIT
    }

    static class SuccessClientFactory implements DOClientServiceFactory {

        private DOCloudImage cloudImage = new DOCloudImage(
                new Image(id: 123, slug:"test", minDiskSize: 320)
        )

        @Override
        DOClientService createClient(String token) {
            return new DOClientService() {
                @Override
                List<DOCloudImage> getImages() {
                    return Collections.singleton(cloudImage)
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
                    return cloudImage;
                }
            }
        }
    }

    static class EmptyClientFactory implements DOClientServiceFactory {
        @Override
        DOClientService createClient(String token) {
            return new DOClientService() {
                @Override
                List<DOCloudImage> getImages() {
                    return Collections.emptyList()
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

    static class FailureClientFactory implements DOClientServiceFactory {
        @Override
        DOClientService createClient(String token) {
            return new DOClientService() {
                @Override
                List<DOCloudImage> getImages() {
                    throw new DOError("Test")
                }

                @Override
                void waitInstanceInitialization(DOCloudInstance cloudInstance) {
                    throw new DOError("Test")
                }

                @Override
                void restartInstance(DOCloudInstance cloudInstance) {
                    throw new DOError("Test")
                }

                @Override
                void terminateInstance(DOCloudInstance cloudInstance) {
                    throw new DOError("Test")
                }

                @Override
                DOCloudInstance createInstance(DOCloudImage cloudImage, DOSettings doSettings) throws DOError {
                    throw new DOError("Test")
                }

                @Override
                void accountCheck() throws DOError {
                    throw new DOError("Test")
                }

                @Override
                DOCloudImage findImageByName(String imageName) throws DOError {
                    throw new DOError("test")
                }
            }
        }
    }

    def getParametersMap() {
        def params = [:]
        params[WebConstants.DO_INTEGRATION_MODE] = DOConfigConstants.PREPARED_IMAGE_MODE_CODE
        params[WebConstants.INSTANCES_COUNT_LIMIT] = '5'
        params
    }

    def getWrongParametersMap() {
        def params = [:]
        params[WebConstants.DO_INTEGRATION_MODE] = 'test'
        params[WebConstants.INSTANCES_COUNT_LIMIT] = '-1'
        params
    }

    def getEmptyParametersMap() {
        [:]
    }

}