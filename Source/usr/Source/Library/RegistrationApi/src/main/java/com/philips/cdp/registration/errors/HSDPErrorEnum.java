package com.philips.cdp.registration.errors;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.RegConstants;

/**
 * Created by philips on 4/17/18.
 */

public enum HSDPErrorEnum {

    HSDP_NOT_WORKING(100,R.string.google_client_id);
    int errorCode;
    int stringId;

    private int getStringId() {
        return stringId;
    }

    HSDPErrorEnum(int errorCode, int stringId){
        this.errorCode = errorCode;
        this.stringId = stringId;
    }

    public static int getStringId(int errorCode){

        for (HSDPErrorEnum hSDPErrorEnum : HSDPErrorEnum.values()){

            if(errorCode == hSDPErrorEnum.errorCode){
                return hSDPErrorEnum.getStringId();
            }
        }
        return RegConstants.UNKNOWN_ERROR_ID;
    }


}
