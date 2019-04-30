package com.philips.platform.pif.DataInterface.USR.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.PIMError;

public interface LogoutSessionListener {

    void logoutSessionSuccess();

    void logoutSessionFailed(PIMError Error);
}
