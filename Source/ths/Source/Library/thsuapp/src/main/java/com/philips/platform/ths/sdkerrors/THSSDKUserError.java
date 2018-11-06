package com.philips.platform.ths.sdkerrors;

import android.content.Context;

import com.americanwell.sdk.entity.SDKErrorReason;
import com.philips.platform.ths.utility.THSConstants;

public class THSSDKUserError implements THSErrorHandlerInterface{

    @Override
    public String getErrorMessage(Context context) {
        return THSConstants.THS_GENERIC_USER_ERROR;
    }

    public boolean validate(String sdkErrorReason, Context context) {
        if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.VALIDATION_REQ_PARAM_MISSING)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.AUTH_ACCESS_DENIED)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.AUTH_ACCOUNT_NOT_ENROLLED)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.AUTH_ACCOUNT_LOCKED)){
            return true;
        }else {
            return false;
        }
    }
}
