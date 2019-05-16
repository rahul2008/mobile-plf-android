package com.philips.platform.pim;

import android.content.Context;

import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pif.DataInterface.USR.UserDataInterfaceException;
import com.philips.platform.pif.DataInterface.USR.UserDetailConstants;
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pif.DataInterface.USR.listeners.HSDPAuthenticationListener;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutSessionListener;
import com.philips.platform.pif.DataInterface.USR.listeners.RefetchUserDetailsListener;
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshSessionListener;
import com.philips.platform.pif.DataInterface.USR.listeners.UserDataListener;
import com.philips.platform.pim.manager.PIMUserManager;
import com.philips.platform.pim.models.PIMOIDCUserProfile;

import java.util.ArrayList;
import java.util.HashMap;

public class PIMDataImplementation implements UserDataInterface {
    private PIMUserManager pimUserManager;
    private Context mContext;

    //TODO: Do we need context here?
    public PIMDataImplementation(Context context, PIMUserManager pimUserManager) {
        mContext = context;
        this.pimUserManager = pimUserManager;
    }

    @Override
    public void logoutSession(LogoutSessionListener logoutSessionListener) {
        if (pimUserManager != null)
            pimUserManager.logoutSession(logoutSessionListener);
    }

    @Override
    public void refreshSession(RefreshSessionListener refreshSessionListener) {

    }

    @Override
    public void refetchUserDetails(RefetchUserDetailsListener userDetailsListener) {

    }

    //TODO: Shashi, Discuss with Deepthi that what action will take id detailskey is null
    @Override
    public HashMap<String, Object> getUserDetails(ArrayList<String> detailKeys) throws UserDataInterfaceException {
        if (pimUserManager == null || pimUserManager.getUserLoggedInState() == UserLoggedInState.USER_NOT_LOGGED_IN || pimUserManager.getUserProfile() == null || detailKeys == null) {
            //log error
            return null;
        }

        PIMOIDCUserProfile pimoidcUserProfile = pimUserManager.getUserProfile();

        if (detailKeys.size() == 0) {
            ArrayList<String> allValidKeys = getAllValidUserDetailsKeys();
            return pimoidcUserProfile.fetchUserDetails(allValidKeys);
        } else if (detailKeys.size() > 0) {
            ArrayList<String> validDetailKey = fillOnlyReqestedValidKeyToKeyList(detailKeys);
            return pimoidcUserProfile.fetchUserDetails(validDetailKey);
        }

        return null;
    }

    private ArrayList<String> fillOnlyReqestedValidKeyToKeyList(ArrayList<String> detailskey) {
        ArrayList<String> allValidKeys = getAllValidUserDetailsKeys();
        ArrayList<String> validDetailsKey = new ArrayList<>();
        for (String key : detailskey) {
            if (allValidKeys.contains(key))
                validDetailsKey.add(key);
        }
        return validDetailsKey;
    }

    private ArrayList<String> getAllValidUserDetailsKeys() {
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(UserDetailConstants.GIVEN_NAME);
        keyList.add(UserDetailConstants.FAMILY_NAME);
        keyList.add(UserDetailConstants.GENDER);
        keyList.add(UserDetailConstants.EMAIL);
        keyList.add(UserDetailConstants.MOBILE_NUMBER);
        keyList.add(UserDetailConstants.BIRTHDAY);
        keyList.add(UserDetailConstants.RECEIVE_MARKETING_EMAIL);
        keyList.add(UserDetailConstants.UUID);
        keyList.add(UserDetailConstants.ACCESS_TOKEN);
        return keyList;
    }

    /**
     * Get the HSDP access token
     *
     * @return returns the HSDP access token
     * @since 2018.1.0
     */
    @Override
    public String getHSDPAccessToken() {
        return null;
    }

    /**
     * Get the HSDP UUID
     *
     * @return returns the HSDP UUID
     * @since 2018.1.0
     */
    @Override
    public String getHSDPUUID() {
        return null;
    }

    @Override
    public UserLoggedInState getUserLoggedInState() {
        if (pimUserManager != null)
            return pimUserManager.getUserLoggedInState();
        return UserLoggedInState.USER_NOT_LOGGED_IN;
    }

    /**
     * {@code authorizeHSDP} method authorize a user is log-in in HSDP Backend
     * TODO :Need to remove
     *
     * @param hsdpAuthenticationListener
     * @since 1804.0
     */
    @Override
    public void authorizeHsdp(HSDPAuthenticationListener hsdpAuthenticationListener) {

    }

    @Override
    public void addUserDataInterfaceListener(UserDataListener listener) {

    }

    @Override
    public void removeUserDataInterfaceListener(UserDataListener listener) {

    }
}
