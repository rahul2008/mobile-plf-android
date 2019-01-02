package com.philips.cdp.registration.ui.traditional.mobile;

import com.android.volley.VolleyError;
import com.philips.cdp.registration.ui.utils.SMSBroadCastReceiver;

public interface MobileVerifyCodeContract{

    void enableVerifyButton();

    void disableVerifyButton();

    void netWorkStateOnlineUiHandle();

    void netWorkStateOfflineUiHandle();

    void refreshUserOnSmsVerificationSuccess();

    void hideProgressSpinner();

    void setOtpErrorMessageFromJson(int errorCode);

    void storePreference(String emailOrMobileNumber);

    void onSuccessResponse(String response);

    void onErrorResponse(VolleyError error);
}
