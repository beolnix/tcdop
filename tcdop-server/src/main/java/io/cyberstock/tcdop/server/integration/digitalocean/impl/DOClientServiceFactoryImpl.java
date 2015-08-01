package io.cyberstock.tcdop.server.integration.digitalocean.impl;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientServiceFactory;

/**
 * Created by beolnix on 27/06/15.
 */
public class DOClientServiceFactoryImpl implements DOClientServiceFactory {

    public DOClientService createClient(String token) {
        DigitalOceanClient doClient = new DigitalOceanClient(token);
        DOClientServiceImpl clientService = new DOClientServiceImpl(doClient);
        return clientService;
    }
}
