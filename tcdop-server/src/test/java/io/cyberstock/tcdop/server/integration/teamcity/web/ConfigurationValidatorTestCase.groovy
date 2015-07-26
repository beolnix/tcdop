package io.cyberstock.tcdop.server.integration.teamcity.web

import io.cyberstock.tcdop.model.DOConfigConstants
import io.cyberstock.tcdop.model.WebConstants
import org.junit.Test


public class ConfigurationValidatorTestCase {

    @Test
    def void testCheckNotNull() {
        def result = ConfigurationValidator.checkNotNull(getParametersMap(), WebConstants.DO_INTEGRATION_MODE)
        assert result.size() == 0
    }

    @Test
    def void testCheckNotNull2() {
        def result = ConfigurationValidator.checkNotNull(getEmptyParametersMap(), WebConstants.DO_INTEGRATION_MODE)

        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.DO_INTEGRATION_MODE
    }

    @Test
    def void testCheckInstancesLimitFormat() {
        def result = ConfigurationValidator.checkInstancesLimitFormat(getParametersMap())
        assert result.size() == 0
    }

    @Test
    def void testCheckInstancesLimitFormat2() {
        def result = ConfigurationValidator.checkInstancesLimitFormat(getEmptyParametersMap())

        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.INSTANCES_COUNT_LIMIT
    }

    @Test
    def void testCheckInstancesLimitFormat3() {
        def result = ConfigurationValidator.checkInstancesLimitFormat(getWrongParametersMap())

        assert result.size() == 1
        assert result.getAt(0).propertyName == WebConstants.INSTANCES_COUNT_LIMIT
    }



    def getParametersMap() {
        def params = [:]
        params."$WebConstants.DO_INTEGRATION_MODE" = DOConfigConstants.PREPARED_IMAGE_MODE_CODE
        params."$WebConstants.INSTANCES_COUNT_LIMIT" = '5'
        params
    }

    def getWrongParametersMap() {
        def params = [:]
        params."$WebConstants.DO_INTEGRATION_MODE" = 'test'
        params."$WebConstants.INSTANCES_COUNT_LIMIT" = '-1'
        params
    }

    def getEmptyParametersMap() {
        [:]
    }

}