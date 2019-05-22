package com.philips.platform.pim.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

public interface PIMLoginListener {
    void onLoginSuccess();
    void onLoginFailed(Error error);
}
