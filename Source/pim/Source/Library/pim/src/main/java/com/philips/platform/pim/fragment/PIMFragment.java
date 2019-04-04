package com.philips.platform.pim.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMListener;
import com.philips.platform.pim.manager.PIMLoginManager;
import com.philips.platform.pim.manager.PIMSettingManager;

import net.openid.appauth.AuthorizationServiceDiscovery;

public class PIMFragment extends Fragment implements PIMListener {
    PIMLoginManager pimLoginManager;
    PIMOIDCConfigration pimoidcConfigration;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pimLoginManager = new PIMLoginManager(pimoidcConfigration);
        //Get from settingManger and inject
        PIMOIDCConfigration piOidcConfigration = PIMSettingManager.getInstance().getPiOidcConfigration();
        pimLoginManager.oidcLogin(this);
    }

    @Override
    public void onSuccess(AuthorizationServiceDiscovery discoveryDoc) {

    }

    @Override
    public void onError() {

    }
}
