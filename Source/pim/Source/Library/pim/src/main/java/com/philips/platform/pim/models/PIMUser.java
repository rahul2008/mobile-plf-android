package com.philips.platform.pim.models;

import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pim.integration.PIMUserDataInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class PIMUser implements PIMUserDataInterface {
    private PIMHsdpUserProfile mHsdpUser;
    private PIMOIDCUserProfile mJanrainUser;

    public PIMUser(PIMHsdpUserProfile mHsdpUser, PIMOIDCUserProfile mJanrainUser) {
        this.mHsdpUser = mHsdpUser;
        this.mJanrainUser = mJanrainUser;
    }

    public PIMHsdpUserProfile getHsdpUser() {
        return mHsdpUser;
    }

    public void setHsdpUser(PIMHsdpUserProfile mHsdpUser) {
        this.mHsdpUser = mHsdpUser;
    }

    public PIMOIDCUserProfile getJanrainUser() {
        return mJanrainUser;
    }

    public void setJanrainUser(PIMOIDCUserProfile mJanrainUser) {
        this.mJanrainUser = mJanrainUser;
    }

    @Override
    public void logoutSession(LogoutSessionListener logoutSessionListener) {

    }

    @Override
    public void refreshSession(ServiceIdentifier serviceIdentifier, RefreshSessionListener refreshSessionListener) {

    }

    @Override
    public void refetchUserDetails(RefetchUserDetailsListener userDetailsListener) {

    }

    @Override
    public HashMap<String, Object> getUserDetails(ServiceIdentifier serviceIdentifiers, ArrayList<String> detailKeys) throws Exception {
        return null;
    }

    @Override
    public boolean authorizeLoginToHSDP(HSDPAuthenticationListener hsdpListener) {
        return false;
    }

    @Override
    public UserLoggedInState getUserLoggedInState() {
        return null;
    }

    @Override
    public void addUserDataInterfaceListener(UserDataListener listener) {

    }

    @Override
    public void removeUserDataInterfaceListener(UserDataListener listener) {

    }
}
