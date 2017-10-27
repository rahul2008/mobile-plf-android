package com.philips.platform.ths.sdkerrors;

import com.americanwell.sdk.entity.SDKErrorReason;

public interface THSErrorHandlerInterface {
    String getErrorMessage();
    boolean validate(SDKErrorReason sdkErrorReason);
}
