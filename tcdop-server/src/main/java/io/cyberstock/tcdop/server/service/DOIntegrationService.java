package io.cyberstock.tcdop.server.service;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import org.jetbrains.annotations.NotNull;

/**
 * Created by beolnix on 16/05/15.
 */
public class DOIntegrationService {

    @NotNull
    private final String token;

    @NotNull
    private final DigitalOceanClient doClient;

    public DOIntegrationService(String token) {
        this.token = token;

        this.doClient = new DigitalOceanClient(token);
    }


}
