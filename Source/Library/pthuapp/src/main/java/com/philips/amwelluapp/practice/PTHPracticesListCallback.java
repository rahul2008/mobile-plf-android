package com.philips.amwelluapp.practice;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.practice.Practice;

import java.util.List;

public interface PTHPracticesListCallback {

    void onPracticesListReceived(PTHPractice practices, SDKError sdkError);
    void onPracticesListFetchError(Throwable throwable);
}
