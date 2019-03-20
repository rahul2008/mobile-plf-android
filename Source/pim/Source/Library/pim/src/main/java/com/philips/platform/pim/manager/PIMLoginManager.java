package com.philips.platform.pim.manager;

import com.philips.platform.PIMListener;
import com.philips.platform.pim.configration.PIMHSDPConfigration;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.logins.HSDPLogin;
import com.philips.platform.pim.logins.OIDCLogin;

//TODO : initilize instance and call login methods from Fragment
public class PIMLoginManager {


    public void oidcLogin(PIMOIDCConfigration mPimoidcConfigration, PIMListener pimListener) {
        OIDCLogin odOidcLogin = new OIDCLogin(mPimoidcConfigration, pimListener);
        odOidcLogin.login(pimListener);
    }

    public void hsdpLogin(PIMHSDPConfigration mPimhsdpConfigration, PIMListener pimListener) {
        HSDPLogin hsdpLogin = new HSDPLogin(mPimhsdpConfigration, pimListener);
        hsdpLogin.login(pimListener);
    }
}

