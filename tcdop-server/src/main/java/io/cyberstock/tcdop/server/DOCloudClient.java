package io.cyberstock.tcdop.server;


import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import io.cyberstock.tcdop.model.DOSettings;
import jetbrains.buildServer.clouds.*;
import org.jetbrains.annotations.NotNull;


/**
 * Created by beolnix on 08/05/15.
 */
public abstract class DOCloudClient implements CloudClientEx {

    @NotNull protected final DOSettings settings;
    protected final DigitalOceanClient doClient;

    public DOCloudClient(@NotNull final DOSettings settings) {
        this.settings = settings;
        this.doClient = new DigitalOceanClient(settings.getToken());
    }


}
