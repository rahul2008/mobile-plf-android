package com.philips.platform.pim.logins;

import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMLoginListener;

public class OIDCLogin {
    private PIMOIDCConfigration pimOidcConfiuration;

    public OIDCLogin(PIMOIDCConfigration pimConfiuration) {
        this.pimOidcConfiuration = pimConfiuration;
    }

    public void login(PIMLoginListener pimLoginListener) {
// Need to login bu AppAuth Manager
    }
}
