package com.philips.cdp.registration.errors;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.RegConstants;

/**
 * Created by philips on 4/17/18.
 */

public enum URXErrorEnum {

    URX_SMS_INVALID_NUMBER(ErrorCodes.URX_INVALID_PHONENUMBER, R.string.reg_URX_SMS_Invalid_PhoneNumber),
    URX_SMS__UNAVAILABLE_NUMBER(ErrorCodes.URX_PHONENUMBER_UNAVAILABLE, R.string.reg_URX_SMS_PhoneNumber_UnAvail_ForSMS),
    URX_SMS_UNSUPPORTED_COUNTRY(ErrorCodes.URX_UNSUPPORTED_COUNTRY, R.string.reg_URX_SMS_UnSupported_Country_ForSMS),
    URX_SMS_LIMIT_REACHED(ErrorCodes.URX_SMS_LIMIT_REACHED, R.string.reg_URX_SMS_Limit_Reached),
    URX_SMS_INTERNAL_SERVER_ERROR(ErrorCodes.URX_SMS_INTERNAL_SERVER_ERROR, R.string.reg_URX_SMS_InternalServerError),
    URX_SMS_NO_INFO(ErrorCodes.URX_NO_INFO_AVAILABLE, R.string.reg_URX_SMS_NoInformation_Available),
    URX_SMS_NOT_SENT(ErrorCodes.URX_SMS_NOT_SENT, R.string.reg_URX_SMS_Not_Sent),
    URX_SMS_ALREADY_VERIFIED(ErrorCodes.URX_SMS_ACCOUNT_ALREADY_VERIFIED, R.string.reg_URX_SMS_Already_Verified),
    URX_SMS_FAILURE_CASE(ErrorCodes.URX_MOBILE_ACCOUNT_FAIURE, R.string.reg_URX_SMS_Failure_case),
    URX_INVALID_VERIFICATION_CODE(ErrorCodes.URX_INVALID_VERIFICATION_CODE, R.string.reg_Mobile_Verification_Invalid_Code);

    int errorCode;
    int stringId;

    private int getStringId() {
        return stringId;
    }

    URXErrorEnum(int errorCode, int stringId) {
        this.errorCode = errorCode;
        this.stringId = stringId;
    }

    public static int getStringId(int errorCode) {

        for (URXErrorEnum urxErrorEnum : URXErrorEnum.values()) {

            if (errorCode == urxErrorEnum.errorCode) {
                return urxErrorEnum.getStringId();
            }
        }
        return RegConstants.UNKNOWN_ERROR_ID;
    }


}
