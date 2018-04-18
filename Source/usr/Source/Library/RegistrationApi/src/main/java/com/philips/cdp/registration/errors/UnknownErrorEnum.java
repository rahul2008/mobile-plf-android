package com.philips.cdp.registration.errors;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.RegConstants;

/**
 * Created by philips on 4/17/18.
 */

public enum UnknownErrorEnum {

    UNKNOWN(100, R.string.jr_provider_unknown);

    int errorCode;
    int stringId;

    private int getStringId() {
        return stringId;
    }

    UnknownErrorEnum(int errorCode, int stringId) {
        this.errorCode = errorCode;
        this.stringId = stringId;
    }

    public static int getStringId(int errorCode) {

        for (UnknownErrorEnum unknownErrorEnum : UnknownErrorEnum.values()) {

            if (errorCode == unknownErrorEnum.errorCode) {
                return unknownErrorEnum.getStringId();
            }
        }
        return RegConstants.UNKNOWN_ERROR_ID;
    }


}
