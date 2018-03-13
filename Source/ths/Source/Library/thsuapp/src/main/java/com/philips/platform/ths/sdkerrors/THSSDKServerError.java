package com.philips.platform.ths.sdkerrors;

import android.content.Context;

import com.americanwell.sdk.entity.SDKErrorReason;
import com.philips.platform.ths.utility.THSConstants;

public class THSSDKServerError implements THSErrorHandlerInterface{
    @Override
    public String getErrorMessage(Context context) {
        return THSConstants.THS_GENERIC_SERVER_ERROR;
    }

    public boolean validate(String sdkErrorReason, Context context) {

        if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.GENERIC_EXCEPTION)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.AUTH_SCHEDULED_DOWNTIME)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.AUTH_SYSTEM_UNSTABLE)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.AUTH_ACCOUNT_INACTIVE)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.SDK_CONFIGURATION_ERROR)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.AUTH_ACCESS_DENIED)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.VALIDATION_BAD_DATE_FORMAT)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.VALIDATION_BAD_DATE)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.PROVIDER_NOT_FOUND)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.PROVIDER_VIDYO_NOT_ENABLED)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.ENG_INITIATOR_MISMATCH)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.FILE_VIRUS_SCAN_FAILED)){
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.ENG_NOT_FOUND)){
            return true;
        }else {
            return false;
        }
    }
}
