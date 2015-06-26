package io.cyberstock.tcdop.server.integration.digitalocean;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;

/**
 * Created by beolnix on 27/06/15.
 */
public class DOClientServiceFactory {

    public DOClientService createClient(String token) {
        DigitalOceanClient doClient = new DigitalOceanClient(token);
        DOClientService clientService = new DOClientService(doClient);
        return clientService;
    }
}
