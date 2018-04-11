/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.sdkerrors;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKResponseSuggestion;

public class THSSDKError {
    private SDKError sdkError;

    public SDKError getSdkError() {
        return sdkError;
    }

    public void setSdkError(SDKError sdkError) {
        this.sdkError = sdkError;
    }

    public String getSDKErrorReason(){
        return sdkError.getSDKErrorReason();
    }

    public SDKResponseSuggestion getSDKResponseSuggestion(){
        return sdkError.getSDKResponseSuggestion();
    }

    public String getMessage(){
        return sdkError.getMessage();
    }

    public String getCsrPhoneNumber(){
        return sdkError.getCsrPhoneNumber();
    }

    public int getHttpResponseCode(){
        return sdkError.getHttpResponseCode();
    }
}
