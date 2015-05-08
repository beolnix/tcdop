package io.cyberstock.tcdop.server;

import io.cyberstock.tcdop.api.DOConfigConstants;
import jetbrains.buildServer.clouds.CloudType;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DOCloudTypeTestCase extends ContextAndPropertiesAwareTestCase {
    //TODO: analyze why context is null sometimes. Does @BeforeTest work in a separate thread?

    @Test
    public void canBeAgentOfTypePositiveTest() {
        Map<String, String> cloudConfig = new HashMap<String, String>();
        String key = DOConfigConstants.IDENTITY_KEY;
        String value = DOConfigConstants.IDENTITY_VALUE;
        cloudConfig.put(key, value);

        CloudType DOCloudTypeInstance = this.context.getBean(DOCloudType.class);
        Boolean result = DOCloudTypeInstance.canBeAgentOfType(new DummyAgentDescription(cloudConfig));

        assertTrue(result);
    }

    @Test
    public void canBeAgentOfTypeNegativeTest() {
        Map<String, String> cloudConfig = new HashMap<String, String>();

        CloudType DOCloudTypeInstance = this.context.getBean(DOCloudType.class);
        Boolean result = DOCloudTypeInstance.canBeAgentOfType(new DummyAgentDescription(cloudConfig));

        assertFalse(result);
    }

    @Test
    public void getInitialParameterValuesTest() {
        CloudType DOCloudTypeInstance = context.getBean(DOCloudType.class);
        Map<String, String> initialParameterValues = DOCloudTypeInstance.getInitialParameterValues();
        assertNotNull(initialParameterValues);
    }

    @Test
    public void getPropertiesProcessorTest() {
        CloudType DOCloudTypeInstance = context.getBean(DOCloudType.class);
        PropertiesProcessor propertiesProcessor = DOCloudTypeInstance.getPropertiesProcessor();
        assertNotNull(propertiesProcessor);
    }

    class DummyAgentDescription implements AgentDescription {

        private Map<String, String> cloudConfig;

        public DummyAgentDescription(Map<String, String> cloudConfig) {
            this.cloudConfig = cloudConfig;
        }

        @NotNull
        public List<RunType> getAvailableRunTypes() {
            return null;
        }

        @NotNull
        public List<String> getAvailableVcsPlugins() {
            return null;
        }

        @NotNull
        public String getOperatingSystemName() {
            return null;
        }

        public int getCpuBenchmarkIndex() {
            return -1;
        }

        @NotNull
        public Map<String, String> getAvailableParameters() {
            return null;
        }

        @NotNull
        public Map<String, String> getDefinedParameters() {
            return null;
        }

        @NotNull
        public Map<String, String> getConfigurationParameters() {
            return cloudConfig;
        }

        @NotNull
        public Map<String, String> getBuildParameters() {
            return null;
        }

        public boolean isCaseInsensitiveEnvironment() {
            return false;
        }
    }
}
