package com.philips.platform.pif.DataInterface.USR.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

public interface RefreshSessionListener {
    void refreshSessionSuccess();

    void refreshSessionFailed(Error error);

    void forcedLogout();
}
