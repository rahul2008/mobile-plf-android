package com.philips.platform.pim.manager;

import com.philips.platform.pim.configration.PIMHSDPConfigration;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMLoginListener;
import com.philips.platform.pim.logins.HSDPLogin;
import com.philips.platform.pim.logins.OIDCLogin;

public class PIMLoginManager {
    private PIMLoginListener pimLoginListener;

    public PIMLoginManager(PIMLoginListener loginListener) {
        this.pimLoginListener = loginListener;
    }

    public void oidcLogin(PIMOIDCConfigration mPimoidcConfigration) {
        OIDCLogin odOidcLogin = new OIDCLogin(mPimoidcConfigration);
        odOidcLogin.login(pimLoginListener);
    }

    public void hsdpLogin(PIMHSDPConfigration mPimhsdpConfigration) {
        HSDPLogin hsdpLogin = new HSDPLogin(mPimhsdpConfigration);
        hsdpLogin.login(pimLoginListener);
    }
}

