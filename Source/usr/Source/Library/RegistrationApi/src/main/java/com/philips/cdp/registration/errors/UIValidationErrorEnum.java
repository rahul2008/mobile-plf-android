package com.philips.cdp.registration.errors;

import com.philips.cdp.registration.R;

/**
 * Created by philips on 4/17/18.
 */

public enum UIValidationErrorEnum {

    HSDP_NOT_WORKING(100,R.string.google_client_id);

    private static final int STRING_ID_NOT_FOUND = -1;
    int errorCode;
    int stringId;

    private int getStringId() {
        return stringId;
    }

    UIValidationErrorEnum(int errorCode, int stringId){
        this.errorCode = errorCode;
        this.stringId = stringId;
    }

    public static int getStringId(int errorCode){

        for (UIValidationErrorEnum uiValidationErrorEnum : UIValidationErrorEnum.values()){

            if(errorCode == uiValidationErrorEnum.errorCode){
                return uiValidationErrorEnum.getStringId();
            }
        }
        return STRING_ID_NOT_FOUND;
    }


}
