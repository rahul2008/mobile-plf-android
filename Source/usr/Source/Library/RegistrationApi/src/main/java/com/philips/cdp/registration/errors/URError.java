package com.philips.cdp.registration.errors;

import android.content.Context;

import com.philips.cdp.registration.ui.utils.RLog;

/**
 * Created by philips on 4/17/18.
 */

public class URError {

    private Context context;

    private final String TAG = URError.class.getSimpleName();

    public URError(Context context) {
        this.context = context;
    }

    public String getLocalizedError(ErrorType errorType, int errorCode) {
        RLog.i(TAG, "ErrorType : " + errorType + " : " + "errorCode : " + errorCode);
        return context.getString(getStringID(errorType, errorCode)) + " #" + errorCode;
    }

    private int getStringID(ErrorType errorType, int errorCode) {
        switch (errorType) {
            case HSDP:
                return HSDPErrorEnum.getStringId(errorCode);
            case URX:
                return URXErrorEnum.getStringId(errorCode);
            case NETWOK:
                return NetworkErrorEnum.getStringId(errorCode); //As for all Network error ,Message will be always same
            case JANRAIN:
                return JanrainErrorEnum.getStringId(errorCode);
            default:
                return JanrainErrorEnum.getStringId(errorCode);
        }

    }

}
