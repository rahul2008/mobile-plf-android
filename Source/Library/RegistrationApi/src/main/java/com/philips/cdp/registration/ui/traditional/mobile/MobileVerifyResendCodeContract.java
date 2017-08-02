package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.*;

import com.philips.cdp.registration.HttpClientServiceReceiver;

public interface MobileVerifyResendCodeContract {


    HttpClientServiceReceiver getClientServiceRecevier();

    ComponentName startService(Intent intent);

    Intent getServiceIntent();

    void enableResendButton();

    void enableUpdateButton();

    void updateResendTime(long timeRemaining);

    void hideErrorMessage();
    void hideProgressSpinner();

    void disableResendButton();

    void showNoNetworkErrorMessage();

    void showSmsSendFailedError();

    void enableResendButtonAndHideSpinner();

    void showSmsResendTechincalError(String errorCodeString);
    void showNumberChangeTechincalError(String errorCodeString);


    void refreshUser();

}
