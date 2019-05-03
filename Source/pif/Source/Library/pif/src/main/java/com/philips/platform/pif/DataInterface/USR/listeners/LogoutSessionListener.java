package com.philips.platform.pif.DataInterface.USR.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

public interface LogoutSessionListener {

    void logoutSessionSuccess();

    void logoutSessionFailed(Error error);
}
