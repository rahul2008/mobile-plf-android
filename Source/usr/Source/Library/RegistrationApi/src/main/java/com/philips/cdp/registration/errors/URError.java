package com.philips.cdp.registration.errors;

import android.content.Context;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.RLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by philips on 4/17/18.
 */

public class URError {

    private Context context;

    private static final List<Integer> janrainErrorCode = new ArrayList<>();

    private final String TAG = URError.class.getSimpleName();

    static {
        janrainErrorCode.add(100);
        janrainErrorCode.add(200);
        janrainErrorCode.add(201);
        janrainErrorCode.add(205);
        janrainErrorCode.add(221);
        janrainErrorCode.add(222);
        janrainErrorCode.add(223);
        janrainErrorCode.add(224);
        janrainErrorCode.add(226);
        janrainErrorCode.add(233);
        janrainErrorCode.add(234);
        janrainErrorCode.add(310);
        janrainErrorCode.add(320);
        janrainErrorCode.add(330);
        janrainErrorCode.add(340);
        janrainErrorCode.add(341);
        janrainErrorCode.add(342);
        janrainErrorCode.add(360);
        janrainErrorCode.add(361);
        janrainErrorCode.add(362);
        janrainErrorCode.add(390);
        janrainErrorCode.add(402);
        janrainErrorCode.add(403);
        janrainErrorCode.add(413);
        janrainErrorCode.add(420);
        janrainErrorCode.add(480);
        janrainErrorCode.add(540);
    }

    public URError(Context context) {
        this.context = context;
    }

    public String getLocalizedError(ErrorType errorType, int errorCode) {
        RLog.i(TAG, "ErrorType : " + errorType + " : " + "errorCode : " + errorCode);
        if (errorType.equals(ErrorType.HSDP) || errorType.equals(ErrorType.NETWOK)) {
            return String.format(context.getString(getStringID(errorType, errorCode)), errorCode);
        } else if (errorType.equals(ErrorType.JANRAIN) && janrainErrorCode.contains(errorCode)) {
            return getJanrainFormattedError(errorCode);
        } else if (errorType == ErrorType.URX && errorCode == ErrorCodes.URX_UNSUPPORTED_COUNTRY) {
            return getURXFormattedError(errorCode);
        }
        return context.getString(getStringID(errorType, errorCode));

    }

    private String getJanrainFormattedError(int errorCode) {
        if (errorCode == ErrorCodes.JANRAIN_CONNECTION_LOST_ENITY_ALREADY_EXIST)
            return String.format(context.getString(getStringID(ErrorType.JANRAIN, errorCode)), context.getString(R.string.reg_DLS_Email_Phone_Label_Text));
        else if (errorCode == ErrorCodes.JANRAIN_AUTHORIZATION_CODE_EXPIRED) {
            return String.format(context.getString(getStringID(ErrorType.JANRAIN, errorCode)), context.getString(R.string.reg_UnexpectedInternalError_ErrorMsg));
        } else if (errorCode == ErrorCodes.JANRAIN_REDIRECT_URI_MISMATCH || errorCode == ErrorCodes.JANRAIN_ERROR_ON_FLOW) {
            return String.format(context.getString(getStringID(ErrorType.JANRAIN, errorCode)), context.getString(R.string.reg_USR_Error_PleaseTryLater_Txt), errorCode);
        }
        return String.format(context.getString(getStringID(ErrorType.JANRAIN, errorCode)), errorCode);
    }

    private String getURXFormattedError(int errorCode) {

        switch (errorCode) {

            case ErrorCodes.URX_UNSUPPORTED_COUNTRY:
                return String.format(context.getString(URXErrorEnum.getStringId(ErrorCodes.URX_UNSUPPORTED_COUNTRY)), context.getString(URXErrorEnum.getStringId(ErrorCodes.URX_INVALID_PHONENUMBER)));

            case ErrorCodes.URX_SMS_INTERNAL_SERVER_ERROR:
                return String.format(context.getString(URXErrorEnum.getStringId(ErrorCodes.URX_SMS_INTERNAL_SERVER_ERROR)), context.getString(R.string.reg_USR_Error_PleaseTryLater_Txt));

            case ErrorCodes.URX_SMS_LIMIT_REACHED:
                String timelimit = "30";
                return String.format(context.getString(URXErrorEnum.getStringId(ErrorCodes.URX_SMS_LIMIT_REACHED)), timelimit);

            case ErrorCodes.URX_NO_INFO_AVAILABLE:
                return String.format(context.getString(URXErrorEnum.getStringId(ErrorCodes.URX_NO_INFO_AVAILABLE)), context.getString(R.string.reg_USR_Error_PleaseTryLater_Txt));

            case ErrorCodes.URX_SMS_NOT_SENT:
                return String.format(context.getString(URXErrorEnum.getStringId(ErrorCodes.URX_SMS_NOT_SENT)), context.getString(R.string.reg_USR_Error_PleaseTryLater_Txt));

            default:
                return "";

        }

    }

    private int getStringID(ErrorType errorType, int errorCode) {
        switch (errorType) {
            case HSDP:
                return HSDPErrorEnum.getStringId(errorCode);
            case URX:

                return URXErrorEnum.getStringId(errorCode);
            case NETWOK:
                return NetworkErrorEnum.getStringId(errorCode); //As for all Network error ,Message will be always same
            case JANRAIN:
                return JanrainErrorEnum.getStringId(errorCode);
            default:
                return JanrainErrorEnum.getStringId(errorCode);
        }

    }

}
