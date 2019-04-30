package com.philips.platform.pif.DataInterface.USR.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.PIMError;

public interface RefreshSessionListener {
    void refreshSessionSuccess();

    void refreshSessionFailed(PIMError error);

    void forcedLogout();
}
