package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.*;

import com.philips.cdp.registration.HttpClientServiceReceiver;

public interface MobileVerifyCodeContract {


    HttpClientServiceReceiver getClientServiceRecevier();

    ComponentName startService(Intent intent);

    Intent getServiceIntent();

    void enableVerifyButton();


    void disableVerifyButton();
    void netWorkStateOnlineUiHandle();
    void netWorkStateOfflineUiHandle();



    void showSmsSendFailedError();

    void refreshUserOnSmsVerificationSuccess();

    void smsVerificationResponseError();

    void hideProgressSpinner();

    void setOtpInvalidErrorMessage();

    void showOtpInvalidError();

    void setOtpErrorMessageFromJson(String errorDescription);

    void storePreference(String emailOrMobileNumber);
}
