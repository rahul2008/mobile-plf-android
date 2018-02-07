package com.philips.platform.ths.sdkerrors;

import android.content.Context;

import com.americanwell.sdk.entity.SDKErrorReason;

public interface THSErrorHandlerInterface {
    String getErrorMessage(Context context);
    boolean validate(SDKErrorReason sdkErrorReason, Context context);
}
