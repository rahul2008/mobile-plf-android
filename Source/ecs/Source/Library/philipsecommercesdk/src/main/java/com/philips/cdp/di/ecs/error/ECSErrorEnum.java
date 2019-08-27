package com.philips.cdp.di.ecs.error;

import com.philips.cdp.di.ecs.R;

public enum ECSErrorEnum {

    hybristokenerror("hybristokenerror", R.string.ail_cloud_consent_help,3001),
    UNKNOWN_ERROR("unknown", R.string.ail_cloud_consent_help,3002);

    String errorType;

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    int resourceID;
    int errorCode;

    ECSErrorEnum(String errorType, int resourceID, int errorCode) {
        this.errorType = errorType;
        this.resourceID = resourceID;
        this.errorCode = errorCode;
    }


    public static ECSErrorEnum getErrorEnumFromType(String errorType){


        for (ECSErrorEnum ecsErrorEnum:ECSErrorEnum.values()){

            if(ecsErrorEnum.getErrorType().equalsIgnoreCase(errorType)){
                return ecsErrorEnum;
            }

        }
        return ECSErrorEnum.UNKNOWN_ERROR;
    }
}
