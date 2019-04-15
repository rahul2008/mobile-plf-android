package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.Intent;
import android.content.Intent;

import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.fragment.PIMFragment;

import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;

//TODO : initilize instance and call login methods from Fragment
public class PIMLoginManager {
    private PIMOIDCConfigration mPimoidcConfigration;
    private PIMAuthManager pimAuthManager;

    public PIMLoginManager(PIMOIDCConfigration mPimoidcConfigration) {
        this.mPimoidcConfigration = mPimoidcConfigration;
        this.pimAuthManager = new PIMAuthManager();
    }

    // TODO: Deepthi Apr 15 what is this API all about, take  PIMOIDC from init
    public Intent oidcLogin(Context context, PIMOIDCConfigration pimoidcConfigration) {
        return pimAuthManager.makeAuthRequest(context, pimoidcConfigration);
    }

    // TODO: Deepthi Apr 15 This is not needed , check
    public void exchangeAuthorizationCode(Context context, AuthorizationResponse authResponse, AuthorizationService.TokenResponseCallback tokenResponseCallback){
       pimAuthManager.performTokenRequest(context,authResponse,tokenResponseCallback);
    }
}

