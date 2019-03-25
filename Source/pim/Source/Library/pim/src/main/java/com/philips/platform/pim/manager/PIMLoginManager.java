package com.philips.platform.pim.manager;

import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMListener;
import com.philips.platform.pim.listeners.PIMOIDCAuthStateListener;

import net.openid.appauth.AuthState;

//TODO : initilize instance and call login methods from Fragment
public class PIMLoginManager {
    private PIMOIDCConfigration mPimoidcConfigration;
    private PIMAuthManager pimAuthManager;

    public PIMLoginManager(PIMOIDCConfigration mPimoidcConfigration) {
        this.mPimoidcConfigration = mPimoidcConfigration;
        this.pimAuthManager = new PIMAuthManager();
    }

    // TODO: is it required to pass context to launch web page
    public void oidcLogin(PIMListener pimListener) {
        pimAuthManager.loginToOIDC(mPimoidcConfigration, new PIMOIDCAuthStateListener() {
            @Override
            public void onSuccess(AuthState state) {
                PIMSettingManager.getInstance().getPimUserManager().requestUserProfile(state, pimListener);
            }

            @Override
            public void onError() {

            }
        });
    }


}

