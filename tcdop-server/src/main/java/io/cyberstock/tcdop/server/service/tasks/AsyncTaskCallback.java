package io.cyberstock.tcdop.server.service.tasks;

import io.cyberstock.tcdop.model.error.DOError;

/**
 * Created by beolnix on 18/06/15.
 */
public interface AsyncTaskCallback<ResultType, ErrorType extends DOError> {
    void onSuccess(ResultType result);
    void onFailure(ErrorType error);
}
