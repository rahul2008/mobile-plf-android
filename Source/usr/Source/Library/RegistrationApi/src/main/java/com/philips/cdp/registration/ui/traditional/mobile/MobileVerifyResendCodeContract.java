package com.philips.cdp.registration.ui.traditional.mobile;

import com.android.volley.VolleyError;

public interface MobileVerifyResendCodeContract {

    void enableResendButton();

    void enableUpdateButton();

    void netWorkStateOnlineUiHandle();

    void hideProgressSpinner();

    void disableResendButton();

    void netWorkStateOfflineUiHandle();

    void showSmsSendFailedError();

    void enableResendButtonAndHideSpinner();

    void showSmsResendTechincalError(String errorCodeString);

    void showNumberChangeTechincalError(String errorCodeString);

    void refreshUser();

    void onSuccessResponse(int requestCode, String response);

    void onErrorResponse(VolleyError error);
}
