package io.cyberstock.tcdop.server.integration.digitalocean.tasks;

import com.myjeeva.digitalocean.pojo.Droplet;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.DOCallback;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudInstance;

/**
 * Created by beolnix on 24/06/15.
 */
public abstract class AsyncTask<Model, Result> implements Runnable {
    protected final DOClientService clientService;
    private final Model model;
    private final DOCallback<Result, DOError> callback;

    public AsyncTask(DOClientService clientService, Model model, DOCallback<Result, DOError> callback) {
        this.clientService = clientService;
        this.model = model;
        this.callback = callback;
    }

    public void run() {
        try {
            Result result = execute(model);
            callback.onSuccess(result);
        } catch (DOError e) {
            callback.onFailure(e);
        }
    }

    abstract protected Result execute(Model model) throws DOError;
}
