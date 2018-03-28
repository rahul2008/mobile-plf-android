package com.philips.platform.ths.sdkerrors;

import android.content.Context;

public interface THSErrorHandlerInterface {
    String getErrorMessage(Context context);
    boolean validate(String sdkErrorReason, Context context);
}
