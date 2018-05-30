package com.philips.cdp.registration.errors;

import com.philips.cdp.registration.R;

/**
 * Created by philips on 4/17/18.
 */

public enum JanrainErrorEnum {

    JANRAIN_UNKNOWN_ERROR(ErrorCodes.UNKNOWN_ERROR, R.string.reg_Janrain_Connection_Lost_No_Invalid_Mismatch),
    JANRAIN_CONNECTION_LOST_NO_ARGUMENT(ErrorCodes.JANRAIN_CONNECTION_LOST_NO_ARGUMENT, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_INVALID_ARGUMENT(ErrorCodes.JANRAIN_CONNECTION_LOST_INVALID_ARGUMENT, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_MISMATCH_ARGUMENT(ErrorCodes.JANRAIN_CONNECTION_LOST_MISMATCH_ARGUMENT, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_INVALID_REQUEST_TYPE(ErrorCodes.JANRAIN_CONNECTION_LOST_INVALID_REQUEST_TYPE, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_WRONG_USERID_PASSWORD(ErrorCodes.JANRAIN_WRONG_USERID_PASSWORD, R.string.reg_Janrain_Invalid_Credentials_ErrorMsg),
    JANRAIN_WRONG_USERID_PASSWORD_SOCIAL(ErrorCodes.JANRAIN_WRONG_USERID_PASSWORD_SOCIAL, R.string.reg_Janrain_Invalid_Credentials_ErrorMsg),
    JANRAIN_EMAIL_ADDRESS_NOT_AVAILABLE(ErrorCodes.JANRAIN_EMAIL_ADDRESS_NOT_AVAILABLE, R.string.reg_Janrain_Email_Address_Not_Available),
    JANRAIN_WRONG_PASSWORD(ErrorCodes.JANRAIN_WRONG_PASSWORD, R.string.reg_Janrain_Invalid_Credentials_ErrorMsg),
    JANRAIN_CONNECTION_LOST_APPID_NOT_EXIST(ErrorCodes.JANRAIN_CONNECTION_LOST_APPID_NOT_EXIST, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_ENTITY_TYPE_NOT_EXIST(ErrorCodes.JANRAIN_CONNECTION_LOST_ENTITY_TYPE_NOT_EXIST, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_ATTRIBUTE_NOT_EXIST(ErrorCodes.JANRAIN_CONNECTION_LOST_ATTRIBUTE_NOT_EXIST, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_NO_APPLICATION(ErrorCodes.JANRAIN_CONNECTION_LOST_NO_APPLICATION, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_MISCONFIGURED_FLOW(ErrorCodes.JANRAIN_CONNECTION_LOST_MISCONFIGURED_FLOW, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_ENITY_ALREADY_EXIST(ErrorCodes.JANRAIN_CONNECTION_LOST_ENITY_ALREADY_EXIST, R.string.reg_Janrain_EntityAlreadyExists_ErrorMsg),
    JANRAIN_CONNECTION_LOST_ATTRIBUTE_ALREADY_EXIST(ErrorCodes.JANRAIN_CONNECTION_LOST_ATTRIBUTE_ALREADY_EXIST, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_MODIFY_ATTRIBUTE(ErrorCodes.JANRAIN_CONNECTION_LOST_MODIFY_ATTRIBUTE, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_CREATE_RECORD_FAILED(ErrorCodes.JANRAIN_CONNECTION_LOST_CREATE_RECORD_FAILED, R.string.reg_Janrain_Connection_Lost_Argument_Not_Match),
    JANRAIN_CONNECTION_LOST_ENTITY_NOT_AVAILABLE(ErrorCodes.JANRAIN_CONNECTION_LOST_ENTITY_NOT_AVAILABLE, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_CREATE_RECORD_ID_FAILED(ErrorCodes.JANRAIN_CONNECTION_LOST_CREATE_RECORD_ID_FAILED, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_ARGUMENT_NOT_MATCHIN(ErrorCodes.JANRAIN_CONNECTION_LOST_ARGUMENT_NOT_MATCHIN, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_JSON_FORMAT_ERROR(ErrorCodes.JANRAIN_CONNECTION_LOST_JSON_FORMAT_ERROR, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_JSON_VALUE_MISMATCH(ErrorCodes.JANRAIN_CONNECTION_LOST_JSON_VALUE_MISMATCH, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_INVALID_DATE(ErrorCodes.JANRAIN_CONNECTION_LOST_INVALID_DATE, R.string.reg_Janrain_HSDP_ServerErrorMsg),
    JANRAIN_CONNECTION_LOST_CONSTRAINT_VIOLATED(ErrorCodes.JANRAIN_CONNECTION_LOST_CONSTRAINT_VIOLATED, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_UNIQUE_CONSTRAINT_VIOLATED(ErrorCodes.JANRAIN_CONNECTION_LOST_UNIQUE_CONSTRAINT_VIOLATED, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_CONNECTION_LOST_EMPTY_OR_NULL_CONSTRAINT(ErrorCodes.JANRAIN_CONNECTION_LOST_EMPTY_OR_NULL_CONSTRAINT, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_ATTRIBUTE_CONSTRAINT_LENGHT_VIOLATION(ErrorCodes.JANRAIN_ATTRIBUTE_CONSTRAINT_LENGHT_VIOLATION, R.string.reg_Janrain_LengthViolation_ErrorMsg),
    JANRAIN_USER_ALREADY_EXIST(ErrorCodes.JANRAIN_USER_ALREADY_EXIST, R.string.reg_Janrain_EmailAddressInUse_ErrorMsg),
    JANRAIN_INVALID_DATA_FOR_VALIDATION(ErrorCodes.JANRAIN_INVALID_DATA_FOR_VALIDATION, R.string.reg_Janrain_HSDP_ServerErrorMsg),
    JANRAIN_WRONG_CLIENT_ID(ErrorCodes.JANRAIN_WRONG_CLIENT_ID, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_UNAUTHORIZED_CLIENT(ErrorCodes.JANRAIN_UNAUTHORIZED_CLIENT, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_UNAUTHORIZED_USER(ErrorCodes.JANRAIN_UNAUTHORIZED_USER, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_ACCESS_TOKEN_EXPIRED(ErrorCodes.JANRAIN_ACCESS_TOKEN_EXPIRED, R.string.reg_Janrain_Access_Token_Expired),
    JANRAIN_AUTHORIZATION_CODE_EXPIRED(ErrorCodes.JANRAIN_AUTHORIZATION_CODE_EXPIRED, R.string.reg_Janrain_AuthorizationCodeExpired_ErrorMsg),
    JANRAIN_VERIFICATION_CODE_EXPIRED(ErrorCodes.JANRAIN_VERIFICATION_CODE_EXPIRED, R.string.reg_Janrain_VerificationCodeExpired_ErrorMsg),
    JANRAIN_CREATION_TOKEN_EXPIRED(ErrorCodes.JANRAIN_CREATION_TOKEN_EXPIRED, R.string.reg_Janrain_CreationTokenExpired_ErrorMsg),
    JANRAIN_REDIRECT_URI_MISMATCH(ErrorCodes.JANRAIN_REDIRECT_URI_MISMATCH, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg),
    JANRAIN_OPERATION_TEMPORARY_UNAVAILABLE(ErrorCodes.JANRAIN_OPERATION_TEMPORARY_UNAVAILABLE, R.string.reg_Janrain_HSDP_ServerErrorMsg),
    JANRAIN_UNEXPECTED_INTERNAL_ERROR(ErrorCodes.JANRAIN_UNEXPECTED_INTERNAL_ERROR, R.string.reg_UnexpectedInternalError_ErrorMsg),
    JANRAIN_API_CALL_LIMIT_REACHED(ErrorCodes.JANRAIN_API_CALL_LIMIT_REACHED, R.string.reg_Janrain_LimitError_ErrorMsg),
    JANRAIN_ERROR_ON_FLOW(ErrorCodes.JANRAIN_ERROR_ON_FLOW, R.string.reg_JanRain_Server_ConnectionLost_ErrorMsg);

    int errorCode;
    int stringId;

    private int getStringId() {
        return stringId;
    }

    JanrainErrorEnum(int errorCode, int stringId) {
        this.errorCode = errorCode;
        this.stringId = stringId;
    }

    public static int getStringId(int errorCode) {
        for (JanrainErrorEnum janrainErrorEnum : JanrainErrorEnum.values()) {
            if (errorCode == janrainErrorEnum.errorCode) {
                return janrainErrorEnum.getStringId();
            }
        }
        return ErrorCodes.UNKNOWN_ERROR;
    }


}
