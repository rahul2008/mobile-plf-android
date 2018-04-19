package com.philips.cdp.registration.errors;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.RegConstants;

/**
 * Created by philips on 4/17/18.
 */

public enum HSDPErrorEnum {


    HSDP_SYSTEM_ERROR_403(ErrorCodes.HSDP_SYSTEM_ERROR_403, R.string.reg_Generic_Network_Error),

    HSDP_SYSTEM_ERROR_100(ErrorCodes.HSDP_SYSTEM_ERROR_100, R.string.reg_Generic_Network_Error),

    HSDP_CANNOT_LOGIN_NOW_104(ErrorCodes.HSDP_CANNOT_LOGIN_NOW_104, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1008(ErrorCodes.HSDP_INPUT_ERROR_1008, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1009(ErrorCodes.HSDP_INPUT_ERROR_1009, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1114(ErrorCodes.HSDP_INPUT_ERROR_1114, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1151(ErrorCodes.HSDP_INPUT_ERROR_1151, R.string.reg_Generic_Network_Error),

    HSDP_CANNOT_LOGIN_NOW_1149(ErrorCodes.HSDP_CANNOT_LOGIN_NOW_1149, R.string.reg_Generic_Network_Error),

    HSDP_CANNOT_LOGIN_NOW_1150(ErrorCodes.HSDP_CANNOT_LOGIN_NOW_1150, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1165(ErrorCodes.HSDP_INPUT_ERROR_1165, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1166(ErrorCodes.HSDP_INPUT_ERROR_1166, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1167(ErrorCodes.HSDP_INPUT_ERROR_1167, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1175(ErrorCodes.HSDP_INPUT_ERROR_1175, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1251(ErrorCodes.HSDP_INPUT_ERROR_1251, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1252(ErrorCodes.HSDP_INPUT_ERROR_1252, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1253(ErrorCodes.HSDP_INPUT_ERROR_1253, R.string.reg_Generic_Network_Error),

    HSDP_CHECK(ErrorCodes.HSDP_CHECK, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1260(ErrorCodes.HSDP_INPUT_ERROR_1260, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1261(ErrorCodes.HSDP_INPUT_ERROR_1261, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1262(ErrorCodes.HSDP_INPUT_ERROR_1262, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1263(ErrorCodes.HSDP_INPUT_ERROR_1263, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1265(ErrorCodes.HSDP_INPUT_ERROR_1265, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1266(ErrorCodes.HSDP_INPUT_ERROR_1266, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1267(ErrorCodes.HSDP_INPUT_ERROR_1267, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1271(ErrorCodes.HSDP_INPUT_ERROR_1271, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1272(ErrorCodes.HSDP_INPUT_ERROR_1272, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1312(ErrorCodes.HSDP_INPUT_ERROR_1312, R.string.reg_Generic_Network_Error),

    HSDP_INVALID(ErrorCodes.HSDP_INVALID, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1571(ErrorCodes.HSDP_INPUT_ERROR_1571, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_1572(ErrorCodes.HSDP_INPUT_ERROR_1572, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_3055(ErrorCodes.HSDP_INPUT_ERROR_3055, R.string.reg_Generic_Network_Error),

    HSDP_TIME_ERROR(ErrorCodes.HSDP_TIME_ERROR, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_3073(ErrorCodes.HSDP_INPUT_ERROR_3073, R.string.reg_Generic_Network_Error),

    HSDP_INPUT_ERROR_3074(ErrorCodes.HSDP_INPUT_ERROR_3074, R.string.reg_Generic_Network_Error);

    int errorCode;
    int stringId;

    private int getStringId() {
        return stringId;
    }

    HSDPErrorEnum(int errorCode, int stringId){
        this.errorCode = errorCode;
        this.stringId = stringId;
    }

    public static int getStringId(int errorCode){

        for (HSDPErrorEnum hSDPErrorEnum : HSDPErrorEnum.values()){

            if(errorCode == hSDPErrorEnum.errorCode){
                return hSDPErrorEnum.getStringId();
            }
        }
        return RegConstants.UNKNOWN_ERROR_ID;
    }


}
