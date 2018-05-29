package com.philips.cdp.registration.ui.traditional.mobile;

import com.philips.cdp.registration.ui.utils.SMSBroadCastReceiver;

public interface MobileForgotPassVerifyCodeContract extends SMSBroadCastReceiver.ReceiveAndRegisterOTPListener{

    void netWorkStateOnlineUiHandle();

    void netWorkStateOfflineUiHandle();
}
