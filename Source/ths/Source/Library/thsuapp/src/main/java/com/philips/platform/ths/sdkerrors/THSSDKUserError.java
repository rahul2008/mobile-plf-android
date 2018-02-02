package com.philips.platform.ths.sdkerrors;

import android.content.Context;

import com.americanwell.sdk.entity.SDKErrorReason;
import com.philips.platform.ths.utility.THSConstants;

public class THSSDKUserError implements THSErrorHandlerInterface{

    @Override
    public String getErrorMessage(Context context) {
        return THSConstants.THS_GENERIC_USER_ERROR;
    }

    public boolean validate(SDKErrorReason sdkErrorReason) {
        if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_REQ_PARAM_MISSING.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.AUTH_ACCESS_DENIED.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.AUTH_ACCOUNT_NOT_ENROLLED.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.AUTH_ACCOUNT_LOCKED.name())){
            return true;
        }else {
            return false;
        }
    }
}
