package com.philips.platform.pim.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

import net.openid.appauth.AuthState;

public interface PIMTokenRequestListener {
    void onTokenRequestSuccess();
    void onTokenRequestFailed(Error error);
}
