package com.philips.platform.pim.manager;

import android.content.Context;
import android.os.Bundle;

import com.philips.platform.pim.configration.PIMOIDCConfigration;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;

//TODO : initilize instance and call login methods from Fragment
public class PIMLoginManager {
    private PIMOIDCConfigration mPimoidcConfigration;
    private PIMAuthManager pimAuthManager;

    public PIMLoginManager(PIMOIDCConfigration pimoidcConfigration) {
        mPimoidcConfigration = pimoidcConfigration;
        pimAuthManager = new PIMAuthManager();
    }

    // TODO:Addressed Deepthi Apr 15 what is this API all about, take  PIMOIDC from init
    public AuthorizationRequest oidcLogin(Context context, Bundle mBundle) {
        return pimAuthManager.makeAuthRequest(context, mPimoidcConfigration,mBundle);
    }

    // TODO: Deepthi Apr 15 This is not needed , check
    public void exchangeAuthorizationCode(Context context, AuthorizationResponse authResponse, AuthorizationService.TokenResponseCallback tokenResponseCallback){
       pimAuthManager.performTokenRequest(context,authResponse,tokenResponseCallback);
    }
}

