package com.philips.amwelluapp.sdkerrors;

import com.americanwell.sdk.entity.SDKPasswordError;

import java.util.List;

public class PTHSDKPasswordError {
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
