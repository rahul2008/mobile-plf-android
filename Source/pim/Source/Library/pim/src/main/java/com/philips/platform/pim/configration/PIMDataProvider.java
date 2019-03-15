package com.philips.platform.pim.configration;

import android.content.Context;

import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pif.DataInterface.USR.listeners.HSDPAuthenticationListener;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutListener;
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshListener;
import com.philips.platform.pif.DataInterface.USR.listeners.UserDetailsListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PIMDataProvider implements UserDataInterface {
    public PIMDataProvider(Context context) {
    }

    @Override
    public HashMap<String, Object> getUserDetails(ArrayList<String> detailKeys) throws Exception {
        return null;
    }

    @Override
    public String getJanrainUUID() {
        return null;
    }

    @Override
    public String getJanrainAccessToken() {
        return null;
    }

    @Override
    public String getHSDPAccessToken() {
        return null;
    }

    @Override
    public String getHSDPUUID() {
        return null;
    }

    @Override
    public boolean isUserLoggedIn(Context context) {
        return false;
    }

    @Override
    public UserLoggedInState getUserLoggedInState() {
        return null;
    }

    @Override
    public void authorizeHsdp(HSDPAuthenticationListener hsdpAuthenticationListener) {

    }

    @Override
    public void refreshLoginSession(RefreshListener refreshListener) {

    }

    @Override
    public void logOut(LogoutListener logoutListener) {

    }

    @Override
    public void refetch(UserDetailsListener userDetailsListener) {

    }

    @Override
    public void updateMarketingOptInConsent(UserDetailsListener userDetailsListener) {

    }
}
