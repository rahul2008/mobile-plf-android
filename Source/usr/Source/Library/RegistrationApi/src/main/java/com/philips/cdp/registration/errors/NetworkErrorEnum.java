package com.philips.cdp.registration.errors;

import com.philips.cdp.registration.R;

/**
 * Created by philips on 4/17/18.
 */

public enum NetworkErrorEnum {

    NETWORK_ERROR(ErrorCodes.NETWORK_ERROR, R.string.reg_Generic_Network_ErrorMsg),
    NO_NETWORK(ErrorCodes.NO_NETWORK, R.string.reg_Network_ErrorMsg);

    int errorCode;
    int stringId;

    NetworkErrorEnum(int errorCode, int stringId) {
        this.errorCode = errorCode;
        this.stringId = stringId;
    }

    public static int getStringId(int errorCode) {

        if (errorCode == ErrorCodes.NO_NETWORK) {
            return NetworkErrorEnum.NO_NETWORK.stringId;
        } else {
            return NetworkErrorEnum.NETWORK_ERROR.stringId;
        }
    }


}
