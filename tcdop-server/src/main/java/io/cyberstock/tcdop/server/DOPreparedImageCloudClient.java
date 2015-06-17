package io.cyberstock.tcdop.server;

import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.service.tasks.*;
import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by beolnix on 24/05/15.
 */
public class DOPreparedImageCloudClient extends DOCloudClient {

    private List<DOCloudInstance> instanceList = new LinkedList<DOCloudInstance>();
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    private Boolean readyFlag = false;
    private HashMap<String, DOCloudImage> cloudImageMap;
    private CloudErrorInfo cloudErrorInfo = null;


    public DOPreparedImageCloudClient(@NotNull DOSettings settings) {
        super(settings);
        executorService.execute(new ClientInitializationTask(doClient, new AsyncTaskCallback<HashMap<String, DOCloudImage>, DOError>() {
            public void onSuccess(HashMap<String, DOCloudImage> result) {
                cloudImageMap = result;
                readyFlag = true;
            }

            public void onFailure(DOError error) {
                readyFlag = false;
            }
        }));
    }

    @NotNull
    public CloudInstance startNewInstance(@NotNull CloudImage cloudImage, @NotNull CloudInstanceUserData cloudInstanceUserData) throws QuotaException {
        DOCloudInstance instance = new DOCloudInstance(new DOCloudImage(cloudImage), cloudInstanceUserData);
        instanceList.add(instance);
        executorService.execute(new LaunchNewInstanceTask(instance, doClient));
        return instance;
    }

    public void restartInstance(@NotNull CloudInstance cloudInstance) {
        executorService.execute(new RestartInstanceTask((DOCloudInstance)cloudInstance, doClient));
    }

    public void terminateInstance(@NotNull CloudInstance cloudInstance) {
        executorService.execute(new TerminateTask((DOCloudInstance)cloudInstance, doClient));
    }

    public void dispose() {
        executorService.shutdown();
    }

    public boolean isInitialized() {
        return readyFlag;
    }

    @Nullable
    public CloudImage findImageById(@NotNull String s) throws CloudException {
        return null;
    }

    @Nullable
    public CloudInstance findInstanceByAgent(@NotNull AgentDescription agentDescription) {
        return null;
    }

    @NotNull
    public Collection<? extends CloudImage> getImages() throws CloudException {
        return null;
    }

    @Nullable
    public CloudErrorInfo getErrorInfo() {
        return cloudErrorInfo;
    }

    public boolean canStartNewInstance(@NotNull CloudImage cloudImage) {
        return false;
    }

    @Nullable
    public String generateAgentName(@NotNull AgentDescription agentDescription) {
        return null;
    }
}
