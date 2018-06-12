package com.philips.cdp.registration.errors;

import android.content.Context;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.RegConstants;

/**
 * Created by philips on 4/17/18.
 */

public enum HSDPErrorEnum {


    HSDP_SYSTEM_ERROR_403(ErrorCodes.HSDP_SYSTEM_ERROR_403, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_SYSTEM_ERROR_100(ErrorCodes.HSDP_SYSTEM_ERROR_100, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_CANNOT_LOGIN_NOW_104(ErrorCodes.HSDP_CANNOT_LOGIN_NOW_104, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1008(ErrorCodes.HSDP_INPUT_ERROR_1008, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1009(ErrorCodes.HSDP_INPUT_ERROR_1009, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1114(ErrorCodes.HSDP_INPUT_ERROR_1114, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1151(ErrorCodes.HSDP_INPUT_ERROR_1151, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_CANNOT_LOGIN_NOW_1149(ErrorCodes.HSDP_CANNOT_LOGIN_NOW_1149, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_CANNOT_LOGIN_NOW_1150(ErrorCodes.HSDP_CANNOT_LOGIN_NOW_1150, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1165(ErrorCodes.HSDP_INPUT_ERROR_1165, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1166(ErrorCodes.HSDP_INPUT_ERROR_1166, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1167(ErrorCodes.HSDP_INPUT_ERROR_1167, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1175(ErrorCodes.HSDP_INPUT_ERROR_1175, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1251(ErrorCodes.HSDP_INPUT_ERROR_1251, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1252(ErrorCodes.HSDP_INPUT_ERROR_1252, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1253(ErrorCodes.HSDP_INPUT_ERROR_1253, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_CHECK(ErrorCodes.HSDP_CHECK, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1260(ErrorCodes.HSDP_INPUT_ERROR_1260, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1261(ErrorCodes.HSDP_INPUT_ERROR_1261, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1262(ErrorCodes.HSDP_INPUT_ERROR_1262, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1263(ErrorCodes.HSDP_INPUT_ERROR_1263, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1265(ErrorCodes.HSDP_INPUT_ERROR_1265, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1266(ErrorCodes.HSDP_INPUT_ERROR_1266, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1267(ErrorCodes.HSDP_INPUT_ERROR_1267, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1271(ErrorCodes.HSDP_INPUT_ERROR_1271, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1272(ErrorCodes.HSDP_INPUT_ERROR_1272, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1312(ErrorCodes.HSDP_INPUT_ERROR_1312, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INVALID_1437(ErrorCodes.HSDP_INVALID_1437, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1571(ErrorCodes.HSDP_INPUT_ERROR_1571, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1572(ErrorCodes.HSDP_INPUT_ERROR_1572, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_3055(ErrorCodes.HSDP_INPUT_ERROR_3055, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_TIME_ERROR_3056(ErrorCodes.HSDP_TIME_ERROR_3056, R.string.HSDP_Signature_Expired_TimeErrorMsg),

    HSDP_INPUT_ERROR_3073(ErrorCodes.HSDP_INPUT_ERROR_3073, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_3074(ErrorCodes.HSDP_INPUT_ERROR_3074, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_3160(ErrorCodes.HSDP_INPUT_ERROR_3160, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_3061(ErrorCodes.HSDP_INPUT_ERROR_3061, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_1112(ErrorCodes.HSDP_INPUT_ERROR_1112, R.string.Janrain_HSDP_ServerErrorMsg),

    HSDP_INPUT_ERROR_3081(ErrorCodes.HSDP_INPUT_ERROR_3081, R.string.Janrain_HSDP_ServerErrorMsg);

    int errorCode;
    int stringId;

    private int getStringId() {
        return stringId;
    }

    HSDPErrorEnum(int errorCode, int stringId) {
        this.errorCode = errorCode;
        this.stringId = stringId;
    }

    public static int getStringId(int errorCode) {

        for (HSDPErrorEnum hSDPErrorEnum : HSDPErrorEnum.values()) {

            if (errorCode == hSDPErrorEnum.errorCode) {
                return hSDPErrorEnum.getStringId();
            }
        }
        return RegConstants.UNKNOWN_ERROR_ID;
    }


    public static String getLocalizedError(Context context, int errorCode) {
        return getHSDPFormattedError(context,errorCode);
    }

    private static String getHSDPFormattedError(Context context, int errorCode) {
        int stringId = getStringId(errorCode);
        if(stringId == RegConstants.UNKNOWN_ERROR_ID ){
            //return context.getString(R.string.reg_Generic_Network_Error) +" "+ "["+errorCode+"]";
            stringId = R.string.JanRain_Server_ConnectionLost_ErrorMsg;
            return String.format(context.getString(stringId),context.getString(R.string.USR_Error_PleaseTryLater_Txt)) + "["+errorCode+"]" +".";
        }
        return String.format(context.getString(getStringId(errorCode)),errorCode);
    }
}
