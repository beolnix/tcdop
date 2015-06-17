package io.cyberstock.tcdop.adapter.dioc;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import io.cyberstock.tcdop.model.DOSettings;

/**
 * Created by beolnix on 18/06/15.
 */
public class DOClientAdapter {

    private final DOSettings settings;
    private final DigitalOceanClient doClient;

    public DOClientAdapter(DOSettings settings) {
        this.settings = settings;
        this.doClient = new DigitalOceanClient(settings.getToken());
    }
}
