package com.philips.cdp.registration.errors;

import android.content.Context;

import com.philips.cdp.registration.ui.utils.RLog;

/**
 * Created by philips on 4/17/18.
 */

public class URError {

    private Context context;

    private final String TAG = "URError";

    public URError(Context context) {
        this.context = context;
    }

    public String getLocalizedError(ErrorType errorType, int errorCode) {
        RLog.i(TAG, "getLocalizedError: ErrorType :" + errorType + " : errorCode "
                + errorCode + "LocalizedError is :" + getString(errorType, errorCode));
        return getString(errorType, errorCode);
    }

    private String getString(ErrorType errorType, int errorCode) {
        switch (errorType) {
            case HSDP:
                return HSDPErrorEnum.getLocalizedError(context, errorCode);
            case URX:
                return URXErrorEnum.getLocalizedError(context, errorCode);
            case NETWOK:
                return NetworkErrorEnum.getLocalizedError(context, errorCode); //As for all Network error ,Message will be always same
            case JANRAIN:
                return JanrainErrorEnum.getLocalizedError(context, errorCode);
            default:
                return "";
        }

    }



}
