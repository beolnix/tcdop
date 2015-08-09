package io.cyberstock.tcdop.server.integration.digitalocean.impl;

import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientServiceFactory;
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.DOAdapter;
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.impl.DOAdapterImpl;

/**
 * Created by beolnix on 27/06/15.
 */
public class DOClientServiceFactoryImpl implements DOClientServiceFactory {

    // state
    private Integer actionResultCheckInterval = 2 * 1000;
    private Long actionWaitThreshold = 20 * 60 * 1000L;

    public DOClientServiceFactoryImpl() {

    }

    public DOClientService createClient(String token) {
        DigitalOcean doClient = new DigitalOceanClient(token);
        DOAdapter doAdapter = new DOAdapterImpl(doClient, actionResultCheckInterval, actionWaitThreshold);
        DOClientServiceImpl clientService = new DOClientServiceImpl(doAdapter);
        return clientService;
    }

    public void setActionResultCheckInterval(Integer actionResultCheckInterval) {
        this.actionResultCheckInterval = actionResultCheckInterval;
    }

    public void setActionWaitThreshold(Long actionWaitThreshold) {
        this.actionWaitThreshold = actionWaitThreshold;
    }
}
