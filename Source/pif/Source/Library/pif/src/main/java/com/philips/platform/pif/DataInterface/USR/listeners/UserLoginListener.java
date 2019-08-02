package com.philips.platform.pif.DataInterface.USR.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

public interface UserLoginListener {
    void onLoginSuccess();

    void onLoginFailed(Error error);

}
