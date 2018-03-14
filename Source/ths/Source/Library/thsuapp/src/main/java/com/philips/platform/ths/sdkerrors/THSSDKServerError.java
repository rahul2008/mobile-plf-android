package com.philips.platform.ths.sdkerrors;

import android.content.Context;

import com.americanwell.sdk.entity.SDKErrorReason;
import com.philips.platform.ths.utility.THSConstants;

public class THSSDKServerError implements THSErrorHandlerInterface{
    @Override
    public String getErrorMessage(Context context) {
        return THSConstants.THS_GENERIC_SERVER_ERROR;
    }

    public boolean validate(SDKErrorReason sdkErrorReason, Context context) {

        if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.GENERIC_EXCEPTION.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.AUTH_SCHEDULED_DOWNTIME.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.AUTH_SYSTEM_UNSTABLE.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.AUTH_ACCOUNT_INACTIVE.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.MEMBER_NOT_FOUND.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.SDK_CONFIGURATION_ERROR.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.MEMBER_NOT_FOUND.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.AUTH_ACCESS_DENIED.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_BAD_DATE_FORMAT.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_BAD_DATE.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.PROVIDER_NOT_FOUND.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.PROVIDER_VIDYO_NOT_ENABLED.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.ENG_INITIATOR_MISMATCH.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.FILE_VIRUS_SCAN_FAILED.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.ENG_NOT_FOUND.name())){
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_BAD_ELIG_INFO.name())){
            return true;
        }else {
            return false;
        }
    }
}
