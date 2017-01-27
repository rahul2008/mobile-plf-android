package com.philips.platform.core.monitors;

import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import retrofit.RetrofitError;

public class ErrorMonitor extends EventMonitor {

    private ErrorHandlingInterface mErrorHandlingInterface;
    @Inject
    UserRegistrationInterface userRegistrationInterface;

    int UNKNOWN = -1;

    //TODO: String to be passed instead of status codes
    //String and int to be held in Object and object to be sent to verticals
    public ErrorMonitor(ErrorHandlingInterface errorHandlingInterface) {
        DataServicesManager.getInstance().mAppComponent.injectErrorMonitor(this);
        mErrorHandlingInterface = errorHandlingInterface;
    }

    public void onEventBackgroundThread(final BackendDataRequestFailed momentSaveRequestFailed) {
        RetrofitError exception = momentSaveRequestFailed.getException();
        postError(exception);
    }

    private void postError(RetrofitError exception) {
        if (exception == null) {
            mErrorHandlingInterface.syncError(UNKNOWN);
            return;
        }
        if (!isTokenExpired(exception)) {
            mErrorHandlingInterface.syncError(exception.getResponse().getStatus());

        }
//        if (retrofitError != null)
//            mErrorHandlingInterface.syncError(retrofitError.getResponse().getStatus());
    }

    public void onEventBackgroundThread(final BackendResponse error) {
        RetrofitError exception = error.getCallException();

        postError(exception);
    }

    private boolean isTokenExpired(RetrofitError e) {
        DSLog.i("UserRegistrationInterfaceImpl", "Check is token valid in MomentDataFetcher");
        int status = -1000;
        if (e != null && e.getResponse() != null) {
            status = e.getResponse().getStatus();
        }

        if (status == HttpURLConnection.HTTP_UNAUTHORIZED ||
                status == HttpURLConnection.HTTP_FORBIDDEN) {
            DSLog.i("UserRegistrationInterfaceImpl", "Call refresh token using work around");
            userRegistrationInterface.refreshAccessTokenUsingWorkAround();
            return true;
        }
        return false;
    }

}
