package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.fragment.PIMFragment;

import net.openid.appauth.AuthorizationService;

//TODO : initilize instance and call login methods from Fragment
public class PIMLoginManager {
    private PIMOIDCConfigration mPimoidcConfigration;
    private PIMAuthManager pimAuthManager;

    public PIMLoginManager(PIMOIDCConfigration mPimoidcConfigration) {
        this.mPimoidcConfigration = mPimoidcConfigration;
        this.pimAuthManager = new PIMAuthManager();
    }

    public void oidcLogin(Context context, AuthorizationService authorizationService) {
       // pimAuthManager.makeAuthRequest(context, authorizationService);
    }

    public void makeAuthRequest(PIMFragment pimFragment){
        pimAuthManager.makeAuthRequest(pimFragment);
    }

    public void exchangeAuthorizationCode(Context context, Intent intent, AuthorizationService.TokenResponseCallback tokenResponseCallback){
       pimAuthManager.performTokenRequest(context,intent,tokenResponseCallback);
    }
}

