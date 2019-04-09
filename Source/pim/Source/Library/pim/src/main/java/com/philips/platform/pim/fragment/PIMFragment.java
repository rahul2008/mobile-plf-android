package com.philips.platform.pim.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMListener;
import com.philips.platform.pim.manager.PIMLoginManager;
import com.philips.platform.pim.manager.PIMSettingManager;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceDiscovery;

import org.json.JSONException;
import org.json.JSONObject;

public class PIMFragment extends Fragment implements PIMListener {
    private PIMLoginManager pimLoginManager;
    private PIMOIDCConfigration pimoidcConfigration;
    private AuthorizationService mAuthService;
    private Context mContext;
    private AuthState mAuthState = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pimLoginManager = new PIMLoginManager(pimoidcConfigration);
        //Get from settingManger and inject
        PIMOIDCConfigration piOidcConfigration = PIMSettingManager.getInstance().getPiOidcConfigration();
        pimLoginManager.oidcLogin(getActivity(),this);

    }

    @Override
    public void onSuccess(AuthorizationServiceDiscovery discoveryDoc) {

    }

    @Override
    public void onError() {

    }
}
