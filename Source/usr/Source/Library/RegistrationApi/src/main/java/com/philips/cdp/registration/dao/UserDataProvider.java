/*
 * Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.cdp.registration.dao;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pif.DataInterface.USR.UserDetailConstants;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutListener;
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshListener;
import com.philips.platform.pif.DataInterface.USR.listeners.UserDetailsListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class UserDataProvider extends User implements UserDataInterface {
    private static final long serialVersionUID = 1995972602210564L;
    private transient Context context;
    private HashMap<String,Object> userDataMap;

    public UserDataProvider(Context context){
        super(context);
        this.context = context;
    }

    private ArrayList<String> getAllValidKeyNames(){
        return new ArrayList<>(Arrays.asList(
                UserDetailConstants.GIVEN_NAME,
                UserDetailConstants.FAMILY_NAME,
                UserDetailConstants.GENDER,
                UserDetailConstants.EMAIL,
                UserDetailConstants.MOBILE_NUMBER,
                UserDetailConstants.BIRTHDAY,
                UserDetailConstants.RECEIVE_MARKETING_EMAIL));
    }

    private void fillUserData() {
        userDataMap = new HashMap<>();
        userDataMap.put(UserDetailConstants.GIVEN_NAME,getDisplayName());
        userDataMap.put(UserDetailConstants.FAMILY_NAME,getFamilyName());
        userDataMap.put(UserDetailConstants.GENDER,getGender());
        userDataMap.put(UserDetailConstants.EMAIL,getEmail());
        userDataMap.put(UserDetailConstants.MOBILE_NUMBER,getMobile());
        userDataMap.put(UserDetailConstants.BIRTHDAY,getDateOfBirth());
        userDataMap.put(UserDetailConstants.RECEIVE_MARKETING_EMAIL,getReceiveMarketingEmail());
    }

    @Override
    public HashMap<String, Object> getUserDetails(ArrayList<String> detailKeys) throws Exception {
        if(detailKeys.isEmpty()) {
            fillUserData();
            return userDataMap;
        }
        return fillRequiredUserDataMap(detailKeys);
    }


    private HashMap<String,Object> fillRequiredUserDataMap(ArrayList<String> detailKeys) throws Exception {
        fillUserData();
        HashMap<String, Object> requiredUserDataMap = new HashMap<>();
        for(String key: detailKeys){
            if(getAllValidKeyNames().contains(key)){
                requiredUserDataMap.put(key,userDataMap.get(key));
            }
            else{
                throw new Exception("Invalid key : " + key);
            }
        }
        return requiredUserDataMap;
    }

    @Override
    public String getJanrainAccessToken() {
        return getAccessToken();
    }

    @Override
    public String getJanrainUUID() {
        return super.getJanrainUUID();
    }

    @Override
    public String getHSDPAccessToken() {
        return getHsdpAccessToken();
    }

    @Override
    public String getHSDPUUID() {
        return getHsdpUUID();
    }

    @Override
    public boolean isUserLoggedIn(Context context) {
        return isUserSignIn();
    }

    @Override
    public void refreshLoginSession(RefreshListener refreshListener) {
        refreshLoginSession(getRefreshHandler(refreshListener));
    }

    @NonNull
    protected RefreshLoginSessionHandler getRefreshHandler(final RefreshListener refreshListener) {
        return new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                refreshListener.onRefreshSessionSuccess();
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int error) {
                refreshListener.onRefreshSessionFailure(error);
            }

            @Override
            public void onRefreshLoginSessionInProgress(String message) {
                refreshListener.onRefreshSessionInProgress(message);
            }
        };
    }

    @Override
    public void refetch(UserDetailsListener userDetailsListener) {
        refreshUser(getRefetchHandler(userDetailsListener));
    }

    @NonNull
    protected RefreshUserHandler getRefetchHandler(final UserDetailsListener userDetailsListener) {
        return new RefreshUserHandler() {
            @Override
            public void onRefreshUserSuccess() {
                userDetailsListener.onRefetchSuccess();
            }

            @Override
            public void onRefreshUserFailed(int error) {
                userDetailsListener.onRefetchFailure(error);
            }
        };
    }

    @Override
    public void logOut(LogoutListener logoutListener) {
        logout(getLogoutHandler(logoutListener));
    }

    @NonNull
    LogoutHandler getLogoutHandler(final LogoutListener logoutListener) {
        return new LogoutHandler() {
            @Override
            public void onLogoutSuccess() {
                logoutListener.onLogoutSuccess();
            }

            @Override
            public void onLogoutFailure(int responseCode, String message) {
                logoutListener.onLogoutFailure(responseCode,message);
            }
        };
    }

    @Override
    public void updateMarketingOptInConsent(UserDetailsListener userDetailsListener) {
        updateReceiveMarketingEmail(getUpdateReceiveMarketingEmailHandler(userDetailsListener),getReceiveMarketingEmail());

    }

    @NonNull
    protected UpdateUserDetailsHandler getUpdateReceiveMarketingEmailHandler(final UserDetailsListener userDetailsListener) {
        return new UpdateUserDetailsHandler() {
            @Override
            public void onUpdateSuccess() {
                userDetailsListener.onUpdateSuccess();
            }

            @Override
            public void onUpdateFailedWithError(int error) {
                userDetailsListener.onUpdateFailure(error);
            }
        };
    }
}
