package com.philips.platform.pim.listeners;

//TODO:Shashi, Change arguments of onLogingFailed to enum, when PIMErrorCode are defined.
//TODO:Shashi, Pass correct error code
public interface PIMLoginListener {
    void onLoginSuccess();
    void onLoginFailed(int errorCode);
}
