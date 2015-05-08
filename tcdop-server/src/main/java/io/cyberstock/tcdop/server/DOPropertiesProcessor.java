package io.cyberstock.tcdop.server;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by beolnix on 08/05/15.
 */
public class DOPropertiesProcessor implements PropertiesProcessor {
    @NotNull
    public Collection<InvalidProperty> process(@NotNull final Map<String, String> properties) {
        return Collections.emptyList();
    }
}
