package com.philips.platform.core.monitors;

import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.events.BackendMomentRequestFailed;
import com.philips.platform.core.events.BackendResponse;

import retrofit.RetrofitError;

public class ErrorMonitor extends EventMonitor{

    private ErrorHandlingInterface mErrorHandlingInterface;

    int UNKNOWN = -1;
    //TODO: String to be passed instead of status codes
    //String and int to be held in Object and object to be sent to verticals
    public ErrorMonitor(ErrorHandlingInterface errorHandlingInterface){
        mErrorHandlingInterface = errorHandlingInterface;
    }

    public void onEventBackgroundThread(final BackendMomentRequestFailed momentSaveRequestFailed) {
        Exception exception = momentSaveRequestFailed.getException();
        postError(exception);
    }

    private void postError(Exception exception) {
        if(exception == null){
            mErrorHandlingInterface.syncError(UNKNOWN);
            return;
        }
        RetrofitError retrofitError = null;
        if(exception instanceof RetrofitError){
            retrofitError = (RetrofitError)exception;
        }
        if(retrofitError!=null)
        mErrorHandlingInterface.syncError(retrofitError.getResponse().getStatus());
    }

    public void onEventBackgroundThread(final BackendResponse error) {
        Exception exception = error.getCallException();
        postError(exception);
    }
}
