package com.philips.cdp.registration.ui.traditional.mobile;

import com.android.volley.VolleyError;
import com.philips.cdp.registration.ui.utils.SMSBroadCastReceiver;

public interface MobileVerifyCodeContract extends SMSBroadCastReceiver.ReceiveAndRegisterOTPListener{

    void enableVerifyButton();

    void disableVerifyButton();

    void netWorkStateOnlineUiHandle();

    void netWorkStateOfflineUiHandle();

//    void showSmsSendFailedError();

    void refreshUserOnSmsVerificationSuccess();

//    void smsVerificationResponseError();

    void hideProgressSpinner();

//    void setOtpInvalidErrorMessage(int errorCode);

//    void showOtpInvalidError(int errorCode);

    void setOtpErrorMessageFromJson(int errorCode);

    void storePreference(String emailOrMobileNumber);

    void onSuccessResponse(String response);

    void onErrorResponse(VolleyError error);
}
