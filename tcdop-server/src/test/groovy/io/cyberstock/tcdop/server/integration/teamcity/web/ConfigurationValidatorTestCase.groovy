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

    /**
     * POSITIVE: if valid token is provided
     */
    @Test
    public void testTokenCheckPositive() {
        def result = emptyValidator.validateToken("test")
        assert result.size() == 0
    }

    /**
     * NEGATIVE: if wrong token is provided
     */
    @Test
    public void testTokenCheckNegative() {
        def result = failureValidator.validateToken("test")
        assert result.size() == 1
    }

    /**
     * NEGATIVE: if image has not been found
     */
    @Test
    public void testImagesNegative() {
        def result = failureValidator.validateImage("test", DropletSize.M1GB, "test")
        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.IMAGE_NAME
    }

    /**
     * NEGATIVE: if image name provided and image resolved successfully but disk size is less than required
     */
    @Test
    public void testImagesNegativeWrongDiskSize() {
        def result = successValidator.validateImage("test", DropletSize.M16GB, "test")
        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.DROPLET_SIZE
    }

    /**
     * POSITIVE: if image name provided and image resolved successfully with correct disk size
     */
    @Test
    public void testImagesPositive() {
        def result = successValidator.validateImage("test", DropletSize.M32GB, "test")
        assert result.size() == 0
    }

    /**
     * POSITIVE: if Integration mode is provided
     */
    @Test
    public void testCheckNotNull() {
        def result = emptyValidator.checkNotNull(getParametersMap(), WebConstants.DO_INTEGRATION_MODE)
        assert result.size() == 0
    }

    /**
     * NEGATIVE: if integration mode is not provided
     */
    @Test
    def void testCheckNotNull2() {
        def result = emptyValidator.checkNotNull(getEmptyParametersMap(), WebConstants.DO_INTEGRATION_MODE)

        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.DO_INTEGRATION_MODE
    }

    /**
     * POSITIVE: number provided as Instances Limit value should be parsed successfully
     */
    @Test
    def void testCheckInstancesLimitFormat() {
        def result = emptyValidator.checkInstancesLimitFormat(getParametersMap())
        assert result.size() == 0
    }

    /**
     * NEGATIVE: if instances number not provided
     */
    @Test
    def void testCheckInstancesLimitFormat2() {
        def result = emptyValidator.checkInstancesLimitFormat(getEmptyParametersMap())

        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.INSTANCES_COUNT_LIMIT
    }

    /**
     * NEGATIVE: if not number less then 1 provided as instances count
     */
    @Test
    def void testCheckInstancesLimitFormat3() {
        def result = emptyValidator.checkInstancesLimitFormat(getWrongValueParametersMap())

        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.INSTANCES_COUNT_LIMIT
    }

    /**
     * NEGATIVE: if not a number provided as Instances count
     */
    @Test
    def void testCheckInstancesLimitFormat4() {
        def result = emptyValidator.checkInstancesLimitFormat(getWrongFormatParametersMap())

        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.INSTANCES_COUNT_LIMIT
    }

    /**
     * POSITIVE: if token is valid and correct mode is provided
     */
    @Test
    def void testValidateConfigurationValues() {
        def result = successValidator.validateConfigurationValues(SettingsUtils.convertToDOSettings(getParametersMap()))
        assert result.size() == 0
    }


    /**
     * NEGATIVE: should return an error if token is not provided
     */
    @Test
    def void testValidateConfigurationValuesNegative() {
        def result = failureValidator.validateConfigurationValues(SettingsUtils.convertToDOSettings(getEmptyParametersMap()))
        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.TOKEN
    }

    /**
     * NEGATIVE: should return an error if integration mode is not provided
     */
    @Test
    def void testValidateConfigurationValuesNegative2() {
        def result = successValidator.validateConfigurationValues(SettingsUtils.convertToDOSettings(getEmptyParametersMap()))
        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.DO_INTEGRATION_MODE
    }

    /**
     * POSITIVE: should pass if droplet size is not provided
     */
    @Test
    def void testCheckDropletSizePositive() {
        def result = successValidator.checkDropletSize(getParametersMap())
        assert result.size() == 0

    }

    /**
     * NEGATIVE: should fail if droplet size is not provided
     */
    @Test
    def void testCheckDropletSizeNegative() {
        def result = failureValidator.checkDropletSize(getEmptyParametersMap())
        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.DROPLET_SIZE
    }

    /**
     * NEGATIVE: should fail it droplet size is provided in wrong format
     */
    @Test
    def void testCheckDropletSizeNegative2() {
        def result = failureValidator.checkDropletSize(getWrongFormatParametersMap())
        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.DROPLET_SIZE
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
        params[WebConstants.TOKEN] = 'test'
        params[WebConstants.DROPLET_SIZE] = '32GB'
        params[WebConstants.IMAGE_NAME] = 'name'
        params[WebConstants.DROPLET_NAME_PREFIX] = 'prefix'
        params
    }

    def getWrongValueParametersMap() {
        def params = [:]
        params[WebConstants.DO_INTEGRATION_MODE] = 'test'
        params[WebConstants.INSTANCES_COUNT_LIMIT] = '-1'
        params[WebConstants.TOKEN] = 'test'
        params[WebConstants.DROPLET_SIZE] = '1GB'
        params[WebConstants.IMAGE_NAME] = '123'
        params[WebConstants.DROPLET_NAME_PREFIX] = 'prefix'
        params
    }

    def getWrongFormatParametersMap() {
        def params = [:]
        params[WebConstants.DO_INTEGRATION_MODE] = '123'
        params[WebConstants.INSTANCES_COUNT_LIMIT] = 'test'
        params[WebConstants.DROPLET_SIZE] = 'abab'
        params[WebConstants.TOKEN] = 'test'
        params[WebConstants.IMAGE_NAME] = '123'
        params[WebConstants.DROPLET_NAME_PREFIX] = 'prefix'
        params
    }

    def getEmptyParametersMap() {
        [:]
    }

}