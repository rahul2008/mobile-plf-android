package com.philips.platform.ths.sdkerrors;

import com.americanwell.sdk.entity.SDKPasswordError;

import java.util.List;

public class THSSDKPasswordError {
    SDKPasswordError sdkPasswordError;

    public SDKPasswordError getSdkPasswordError() {
        return sdkPasswordError;
    }

    public void setSdkPasswordError(SDKPasswordError sdkPasswordError) {
        this.sdkPasswordError = sdkPasswordError;
    }

    public List<String> getPasswordErrors(){
        return sdkPasswordError.getPasswordErrors();
    }
}
