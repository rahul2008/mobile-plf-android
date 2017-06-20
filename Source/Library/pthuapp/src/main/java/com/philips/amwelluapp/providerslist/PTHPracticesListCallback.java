package com.philips.amwelluapp.providerslist;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.practice.Practice;

import java.util.List;

/**
 * Created by philips on 6/19/17.
 */

public interface PTHPracticesListCallback {

    void onPracticesListReceived(List<Practice> practices, SDKError sdkError);
    void onPracticesListFetchError(Throwable throwable);
}
