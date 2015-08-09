package io.cyberstock.tcdop.server.integration.digitalocean;

import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudInstance;

/**
 * Created by beolnix on 09/08/15.
 */
public interface DOAsyncClientService {
    void restartInstance(final DOCloudInstance cloudInstance);
    DOCloudInstance initializeInstance(DOCloudImage cloudImage, DOSettings doSettings) throws DOError;
    void terminateInstance(final DOCloudInstance cloudInstance);
}
