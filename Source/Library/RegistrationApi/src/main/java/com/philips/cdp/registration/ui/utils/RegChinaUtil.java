package com.philips.cdp.registration.ui.utils;

import android.content.Context;

import com.philips.cdp.registration.R;

/**
 * Created by 310190722 on 10/22/2016.
 */
public class RegChinaUtil {

    public static String getErrorMsgDescription(String errorCodeStr, Context context) {
        int errorCode = Integer.parseInt(errorCodeStr);
        String errorMsg ="";

        switch (errorCode) {
            case RegChinaConstants.URXSMSSuccessCode:
               errorMsg = context.getResources().getString(R.string.URX_SMS_Success);
                break;
            case RegChinaConstants.URXSMSInvalidNumber:
                errorMsg = context.getResources().getString(R.string.URX_SMS_Invalid_PhoneNumber);
                break;
            case RegChinaConstants.URXSMSUnAvailNumber:
                errorMsg = context.getResources().getString(R.string.URX_SMS_PhoneNumber_UnAvail_ForSMS);
                break;
            case RegChinaConstants.URXSMSUnSupportedCountry:
                errorMsg = context.getResources().getString(R.string.URX_SMS_UnSupported_Country_ForSMS);
                break;
            case RegChinaConstants.URXSMSLimitReached:
                errorMsg = context.getResources().getString(R.string.URX_SMS_Limit_Reached);
                break;
            case RegChinaConstants.URXSMSInternalServerError:
                errorMsg = context.getResources().getString(R.string.URX_SMS_InternalServerError);
                break;
            case RegChinaConstants.URXSMSNoInfo:
                errorMsg = context.getResources().getString(R.string.URX_SMS_NoInformation_Available);
                break;
            case RegChinaConstants.URXSMSNotSent:
                errorMsg = context.getResources().getString(R.string.URX_SMS_Not_Sent);
                break;
            case RegChinaConstants.URXSMSAlreadyVerifed:
                errorMsg = context.getResources().getString(R.string.URX_SMS_Already_Verified);
                break;

            default:
                return "";
        }
        return errorMsg;
    }
}
