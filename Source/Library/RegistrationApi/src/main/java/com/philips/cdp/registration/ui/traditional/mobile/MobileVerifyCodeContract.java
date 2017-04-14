package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.ComponentName;
import android.content.Intent;

import com.philips.cdp.registration.HttpClientServiceReceiver;

public interface MobileVerifyCodeContract {


    HttpClientServiceReceiver getClientServiceRecevier();

    ComponentName startService(Intent intent);

    Intent getServiceIntent();

    void enableResendButton();

    void updateResendTimer(String timeRemaining);

    void enableVerifyButton();

    void hideErrorMessage();

    void disableVerifyButton();

    void showNoNetworkErrorMessage();

    void showSmsSendFailedError();

    void refreshUserOnSmsVerificationSuccess();

    void smsVerificationResponseError();

    void hideProgressSpinner();

    void setOtpInvalidErrorMessage();

    void showOtpInvalidError();

    void setOtpErrorMessageFromJson(String errorDescription);

    void enableResendButtonAndHideSpinner();

    void showSmsResendTechincalError(String errorCodeString);
}
