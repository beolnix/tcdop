package io.cyberstock.tcdop.server.integration.digitalocean;

import io.cyberstock.tcdop.model.error.DOError;

/**
 * Created by beolnix on 18/06/15.
 */
public interface DOCallback<ResultType, ErrorType extends DOError> {
    void onSuccess(ResultType result);
    void onFailure(ErrorType error);
}
