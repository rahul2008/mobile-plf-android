package com.philips.platform.pim;

import android.content.Context;

import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pif.DataInterface.USR.listeners.HSDPAuthenticationListener;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutListener;
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshListener;
import com.philips.platform.pif.DataInterface.USR.listeners.UserDetailsListener;
import com.philips.platform.pim.manager.PIMUserManager;
import com.philips.platform.pim.models.PIMOIDCUserProfile;
import com.philips.platform.pim.utilities.PIMUserDetails;

import java.util.ArrayList;
import java.util.HashMap;

public class PIMDataImplementation implements PIMUserDataInterface {
    private PIMUserManager pimUserManager;
    private Context mContext;

    //TODO: Do we need context here?
    public PIMDataImplementation(Context context, PIMUserManager pimUserManager) {
        mContext = context;
        this.pimUserManager = pimUserManager;
    }

    @Override
    public void logoutSession(LogoutSessionListener logoutSessionListener) {

    }

    @Override
    public void refreshSession(RefreshSessionListener refreshSessionListener) {

    }

    @Override
    public void refetchUserDetails(RefetchUserDetailsListener userDetailsListener) {

    }

    //TODO: Shashi, Discuss with Deepthi that what action will take id detailskey is null
    @Override
    public HashMap<String, Object> getUserDetails(ArrayList<String> detailKeys) throws Exception {
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
        keyList.add(PIMUserDetails.FIRST_NAME);
        keyList.add(PIMUserDetails.LAST_NAME);
        keyList.add(PIMUserDetails.GENDER);
        keyList.add(PIMUserDetails.EMAIL);
        keyList.add(PIMUserDetails.MOBILE_NUMBER);
        keyList.add(PIMUserDetails.BIRTHDAY);
        keyList.add(PIMUserDetails.ADDRESS);
        keyList.add(PIMUserDetails.RECEIVE_MARKETING_EMAIL);
        keyList.add(PIMUserDetails.UUID);
        keyList.add(PIMUserDetails.ACCESS_TOKEN);
        return keyList;
    }

    @Override
    public String getJanrainUUID() {
        return null;
    }

    @Override
    public String getJanrainAccessToken() {
        return null;
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

    //TODO: Depreciated. Need to remove later
    @Override
    public boolean isUserLoggedIn(Context context) {
        return false;
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
    public void refreshLoginSession(RefreshListener refreshListener) {

    }

    @Override
    public void logOut(LogoutListener logoutListener) {
        pimUserManager.logout(logoutListener);
    }

    @Override
    public void refetch(UserDetailsListener userDetailsListener) {

    }

    @Override
    public void updateMarketingOptInConsent(UserDetailsListener userDetailsListener) {

    }

    @Override
    public void registerLogOutListener(LogoutListener logoutListener) {

    }

    @Override
    public void unregisterLogOutListener(LogoutListener logoutListener) {

    }

    @Override
    public void addUserDataInterfaceListener(UserDataListener listener) {

    }

    @Override
    public void removeUserDataInterfaceListener(UserDataListener listener) {

    }
}
