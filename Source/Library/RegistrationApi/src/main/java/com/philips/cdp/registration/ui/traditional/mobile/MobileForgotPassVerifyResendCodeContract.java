package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.*;

import com.philips.cdp.registration.*;

public interface MobileForgotPassVerifyResendCodeContract {


    HttpClientServiceReceiver getClientServiceRecevier();

    ComponentName startService(Intent intent);

    Intent getServiceIntent();

    void enableResendButton();

    void netWorkStateOnlineUiHandle();

    void hideProgressSpinner();

    void netWorkStateOfflineUiHandle();

    void showSmsSendFailedError();

    void enableResendButtonAndHideSpinner();

    void showSmsResendTechincalError(String errorCodeString);

    void trackMultipleActionsOnMobileSuccess();

    void trackActionStatus(String state, String key, String value);

    void showSMSSentNotification();

    void showSMSSpecifedError(String errorId);

}
