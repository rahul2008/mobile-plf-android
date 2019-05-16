package com.philips.platform.pim.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;

//TODO:Shashi, Change arguments of onLogingFailed to enum, when PIMErrorCode are defined.
//TODO:Shashi, Pass correct error code
public interface PIMLoginListener {
    void onLoginSuccess();
    void onLoginFailed(Error error);
}
