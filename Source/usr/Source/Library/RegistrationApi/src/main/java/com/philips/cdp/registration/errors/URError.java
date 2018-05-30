package com.philips.cdp.registration.errors;

import android.content.Context;

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
        if (errorType.equals(ErrorType.HSDP)) {
            return context.getString(getStringID(errorType, errorCode)) + " #" + errorCode;
        } else if (errorType.equals(ErrorType.JANRAIN) && janrainErrorCode.contains(errorCode)) {
            return context.getString(getStringID(errorType, errorCode)) + " #" + errorCode;
        }

        return context.getString(getStringID(errorType, errorCode));

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
