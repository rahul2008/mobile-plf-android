/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.core.monitors;

import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class ErrorMonitor extends EventMonitor {

    private ErrorHandlingInterface mErrorHandlingInterface;

    @Inject
    UserRegistrationInterface userRegistrationInterface;

    @Inject
    SynchronisationManager synchronisationManager;

    int UNKNOWN = -1;

    //TODO: String to be passed instead of status codes
    //String and int to be held in Object and object to be sent to verticals
    public ErrorMonitor(ErrorHandlingInterface errorHandlingInterface) {
        DataServicesManager.getInstance().getAppComponent().injectErrorMonitor(this);
        mErrorHandlingInterface = errorHandlingInterface;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final BackendDataRequestFailed momentSaveRequestFailed) {
        RetrofitError exception = momentSaveRequestFailed.getException();
        postError(exception);
    }

    protected void postError(RetrofitError exception) {
        if (exception == null) {
            unknownError();
            return;
        }
        Response response = exception.getResponse();
        if (response == null) {
            unknownError();
            return;
        }
        int status = response.getStatus();
        reportError(status);
    }

    protected void reportError(int status) {
        if (status == HttpURLConnection.HTTP_UNAUTHORIZED || status == HttpURLConnection.HTTP_BAD_REQUEST) {
            //No need to pass to vertical as Library will take care of doing it
            return;
        }
        mErrorHandlingInterface.syncError(status);
    }

    private void unknownError() {
        mErrorHandlingInterface.syncError(UNKNOWN);
        return;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final BackendResponse error) {
        RetrofitError exception = error.getCallException();
        postError(exception);
    }
}
