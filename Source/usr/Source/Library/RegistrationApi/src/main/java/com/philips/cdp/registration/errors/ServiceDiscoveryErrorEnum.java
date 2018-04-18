package com.philips.cdp.registration.errors;

import com.philips.cdp.registration.R;

/**
 * Created by philips on 4/17/18.
 */

public enum ServiceDiscoveryErrorEnum {

    HSDP_NOT_WORKING(100,R.string.google_client_id);

    private static final int STRING_ID_NOT_FOUND = -1;
    int errorCode;
    int stringId;

    private int getStringId() {
        return stringId;
    }

    ServiceDiscoveryErrorEnum(int errorCode, int stringId){
        this.errorCode = errorCode;
        this.stringId = stringId;
    }

    public static int getStringId(int errorCode){

        for (ServiceDiscoveryErrorEnum hSDPErrorEnum : ServiceDiscoveryErrorEnum.values()){

            if(errorCode == hSDPErrorEnum.errorCode){
                return hSDPErrorEnum.getStringId();
            }
        }
        return STRING_ID_NOT_FOUND;
    }


}
