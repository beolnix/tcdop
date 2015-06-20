package io.cyberstock.tcdop.server.service;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.clouds.CloudClientFactory;
import org.apache.log4j.Level;

/**
 * Created by beolnix on 20/06/15.
 */
public class TCDOPLoggerService {

    // Constants
    public final static String LOGGER_NAME = CloudClientFactory.class.getName();
    private static final Logger LOG = Logger.getInstance(LOGGER_NAME);

    // State
    private Level level;

    public TCDOPLoggerService(String levelStr) {
        this.level = Level.toLevel(levelStr);
        LOG.setLevel(level);
    }
}
