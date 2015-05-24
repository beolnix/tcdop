package io.cyberstock.tcdop.server;


import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import io.cyberstock.tcdop.server.service.DOIntegrationService;
import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.BuildServerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Date;

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
