package com.philips.platform.pim.manager;

import com.philips.platform.pim.configration.PIMHSDPConfigration;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.integration.PIMUserDataInterface;
import com.philips.platform.pim.listeners.LoginHSDPListener;
import com.philips.platform.pim.listeners.PIMListener;
import com.philips.platform.pim.listeners.PIMOIDCLoginListener;
import com.philips.platform.pim.logins.HSDPLogin;
import com.philips.platform.pim.logins.OIDCLogin;
import com.philips.platform.pim.models.PIMOIDCUserProfile;

//TODO : initilize instance and call login methods from Fragment
public class PIMLoginManager {
    private LoginHSDPListener hsdpListener;

    public void oidcLogin(PIMOIDCConfigration mPimoidcConfigration, PIMListener pimListener) {
        OIDCLogin odOidcLogin = new OIDCLogin(mPimoidcConfigration, pimListener);
        odOidcLogin.login(new PIMOIDCLoginListener() {
            @Override
            public void onSuccess(PIMOIDCUserProfile pimoidcUserProfile) {
                PIMHSDPConfigration mPimhsdpConfigration = new PIMHSDPConfigration();
                hsdpLogin(mPimhsdpConfigration, pimoidcUserProfile, hsdpListener);
            }

            @Override
            public void onFailure(PIMUserDataInterface.PIMError pimError) {
//
            }

        });
    }

    private void hsdpLogin(PIMHSDPConfigration mPimhsdpConfigration, PIMOIDCUserProfile pimoidcUserProfile, LoginHSDPListener hsdpListener) {
        HSDPLogin hsdpLogin = new HSDPLogin(mPimhsdpConfigration, pimoidcUserProfile,hsdpListener);
        hsdpLogin.login(hsdpListener);
    }


}

