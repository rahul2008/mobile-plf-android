package com.philips.platform.pim.logins;

import com.philips.platform.pim.configration.PIMHSDPConfigration;
import com.philips.platform.pim.listeners.LoginHSDPListener;
import com.philips.platform.pim.models.PIMOIDCUserProfile;

public class HSDPLogin {
    private PIMHSDPConfigration pimHsdpConfiuration;


    public HSDPLogin(PIMHSDPConfigration pimHsdpConfiuration, PIMOIDCUserProfile pimoidcUserProfile, LoginHSDPListener hsdpListener) {
        this.pimHsdpConfiuration = pimHsdpConfiuration;
    }


    public void login(LoginHSDPListener hsdpListener) {

    }


}
