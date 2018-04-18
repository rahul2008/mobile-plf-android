package com.philips.cdp.registration.errors;

import android.content.Context;

/**
 * Created by philips on 4/17/18.
 */

public class URError {

    ErrorType errorType;
    Context context;

    public URError(ErrorType errorType, Context context) {
        this.errorType = errorType;
        this.context = context;
    }

    public String getLocalizedError(int errorCode) {
        return context.getString(getStringID(errorType, errorCode));
    }

    private int getStringID(ErrorType errorType, int errorCode) {

        switch (errorType) {
            case HSDP:
                return HSDPErrorEnum.getStringId(errorCode);
            case URX:
                return URXErrorEnum.getStringId(errorCode);
            case NETWOK:
                return NetworkErrorEnum.getStringId(errorCode);
            case SERVICEDISCOVERY:
                return ServiceDiscoveryErrorEnum.getStringId(errorCode);
            case UIVALIDATION:
                return UIValidationErrorEnum.getStringId(errorCode);
            case JANRAIN:
                return JanrainErrorEnum.getStringId(errorCode);
            case UNKNOWN:
                return UnknownErrorEnum.getStringId(errorCode);
            default:
                return UnknownErrorEnum.getStringId(errorCode);
        }

    }

}
