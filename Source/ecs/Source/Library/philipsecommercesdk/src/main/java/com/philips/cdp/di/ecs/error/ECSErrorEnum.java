package com.philips.cdp.di.ecs.error;

import com.philips.cdp.di.ecs.R;

public enum ECSErrorEnum {

    hybristokenerror("hybristokenerror", R.string.ail_cloud_consent_help, 3001),
    UNKNOWN_ERROR("unknown", R.string.ail_cloud_consent_help, 3002);

    String errorType;
    int resourceID;
    int errorCode;

    public String getErrorType() {
        return errorType;
    }

    public int getResourceID() {
        return resourceID;
    }


    public int getErrorCode() {
        return errorCode;
    }

    ECSErrorEnum(String errorType, int resourceID, int errorCode) {
        this.errorType = errorType;
        this.resourceID = resourceID;
        this.errorCode = errorCode;
    }


    public static ECSErrorEnum getErrorEnumFromType(String errorType) {
        return ECSErrorEnum.valueOf(errorType);
    }
}
