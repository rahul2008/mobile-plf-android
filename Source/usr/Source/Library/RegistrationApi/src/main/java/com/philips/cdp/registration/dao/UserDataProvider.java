/*
 * Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.cdp.registration.dao;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.UserLoginState;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.listener.HSDPAuthenticationListener;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pif.DataInterface.USR.UserDetailConstants;
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutListener;
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshListener;
import com.philips.platform.pif.DataInterface.USR.listeners.UserDetailsListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserDataProvider extends User implements UserDataInterface {
    private final String TAG = "UserDataProvider";
    private static final long serialVersionUID = 1995972602210564L;
    private transient Context context;
    private HashMap<String, Object> userDataMap;
    private final CopyOnWriteArrayList<LogoutListener> userLogoutListeners;

    public UserDataProvider(Context context) {
        super(context);
        this.context = context;
        userLogoutListeners = new CopyOnWriteArrayList<>();
    }

    private ArrayList<String> getAllValidKeyNames() {
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
        userDataMap.put(UserDetailConstants.GIVEN_NAME, getGivenName());
        userDataMap.put(UserDetailConstants.FAMILY_NAME, getFamilyName());
        userDataMap.put(UserDetailConstants.GENDER, getGender());
        userDataMap.put(UserDetailConstants.EMAIL, getEmail());
        userDataMap.put(UserDetailConstants.MOBILE_NUMBER, getMobile());
        userDataMap.put(UserDetailConstants.BIRTHDAY, getDateOfBirth());
        userDataMap.put(UserDetailConstants.RECEIVE_MARKETING_EMAIL, getReceiveMarketingEmail());
    }

    @Override
    public HashMap<String, Object> getUserDetails(ArrayList<String> detailKeys) throws Exception {
        RLog.d(TAG, "getUserDetails : " + detailKeys);
        if (detailKeys.isEmpty()) {
            fillUserData();
            return userDataMap;
        }
        return fillRequiredUserDataMap(detailKeys);
    }


    private HashMap<String, Object> fillRequiredUserDataMap(ArrayList<String> detailKeys) throws Exception {
        RLog.d(TAG, "fillRequiredUserDataMap : " + detailKeys);
        fillUserData();
        HashMap<String, Object> requiredUserDataMap = new HashMap<>();
        for (String key : detailKeys) {
            if (getAllValidKeyNames().contains(key)) {
                requiredUserDataMap.put(key, userDataMap.get(key));
            } else {
                throw new Exception("Invalid key : " + key);
            }
        }
        return requiredUserDataMap;
    }

    @Override
    public String getJanrainAccessToken() {
        RLog.d(TAG, "getJanrainAccessToken : " + getAccessToken());
        return getAccessToken();
    }

    @Override
    public String getJanrainUUID() {
        String janrainID = super.getJanrainUUID();
        RLog.d(TAG, "getJanrainUUID : " + janrainID);
        return janrainID;
    }

    @Override
    public String getHSDPAccessToken() {
        RLog.d(TAG, "getHSDPAccessToken : " + getHsdpAccessToken());
        return getHsdpAccessToken();
    }

    @Override
    public String getHSDPUUID() {
        RLog.d(TAG, "getHSDPUUID : " + getHsdpUUID());
        return getHsdpUUID();
    }

    @Override
    public boolean isUserLoggedIn(Context context) {
        boolean isLoggedIn = getUserLoginState() == UserLoginState.USER_LOGGED_IN;
        RLog.d(TAG, "isUserLoggedIn :  " + isLoggedIn);
        return isLoggedIn;
    }

    @Override
    public UserLoggedInState getUserLoggedInState() {
        return getState();
    }

    private UserLoggedInState getState() {
        switch (getUserLoginState()) {
            case USER_LOGGED_IN:
                return UserLoggedInState.USER_LOGGED_IN;
            case USER_NOT_LOGGED_IN:
                return UserLoggedInState.USER_NOT_LOGGED_IN;
            case PENDING_VERIFICATION:
                return UserLoggedInState.PENDING_VERIFICATION;
            case PENDING_HSDP_LOGIN:
                return UserLoggedInState.PENDING_HSDP_LOGIN;
            case PENDING_TERM_CONDITION:
                return UserLoggedInState.PENDING_TERM_CONDITION;
        }
        return UserLoggedInState.USER_NOT_LOGGED_IN;
    }


    @Override
    public void authorizeHsdp(com.philips.platform.pif.DataInterface.USR.listeners.HSDPAuthenticationListener hsdpAuthenticationListener) {
        authorizeHSDP(getHsdpAuthenticationHandler(hsdpAuthenticationListener));
    }


    @NonNull
    private HSDPAuthenticationListener getHsdpAuthenticationHandler(com.philips.platform.pif.DataInterface.USR.listeners.HSDPAuthenticationListener hsdpAuthenticationListener) {
        RLog.d(TAG, "getHsdpAuthenticationHandler");
        return new HSDPAuthenticationListener() {
            @Override
            public void onHSDPLoginSuccess() {
                RLog.d(TAG, "onHSDPLoginSuccess");
                hsdpAuthenticationListener.onHSDPLoginSuccess();
            }

            @Override
            public void onHSDPLoginFailure(int errorCode, String msg) {
                RLog.d(TAG, "onHSDPLoginFailure");
                hsdpAuthenticationListener.onHSDPLoginFailure(errorCode, msg);
            }
        };
    }

    @Override
    public void refreshLoginSession(RefreshListener refreshListener) {
        RLog.d(TAG, "refreshLoginSession");
        refreshLoginSession(getRefreshHandler(refreshListener));
    }

    @NonNull
    protected RefreshLoginSessionHandler getRefreshHandler(final RefreshListener refreshListener) {
        RLog.d(TAG, "getRefreshHandler");
        return new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                RLog.d(TAG, "onRefreshLoginSessionSuccess");
                refreshListener.onRefreshSessionSuccess();
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int error) {
                RLog.e(TAG, "getRefreshHandler: onRefreshLoginSessionFailedWithError: " + error);
                refreshListener.onRefreshSessionFailure(error);
            }

            @Override
            public void onRefreshLoginSessionInProgress(String message) {
                RLog.e(TAG, "getRefreshHandler: onRefreshLoginSessionInProgress : " + message);
                refreshListener.onRefreshSessionInProgress(message);
            }

            @Override
            public void onRefreshLoginSessionFailedAndLoggedout() {
                RLog.e(TAG, "getRefreshHandler: onRefreshLoginSessionFailedAndLoggedout : ");
                refreshListener.onForcedLogout();
            }
        };
    }

    @Override
    public void refetch(UserDetailsListener userDetailsListener) {
        RLog.d(TAG, "refetch");
        refreshUser(getRefetchHandler(userDetailsListener));
    }

    @NonNull
    protected RefreshUserHandler getRefetchHandler(final UserDetailsListener userDetailsListener) {
        RLog.d(TAG, "getRefetchHandler");
        return new RefreshUserHandler() {
            @Override
            public void onRefreshUserSuccess() {
                RLog.d(TAG, "onRefreshUserSuccess");
                userDetailsListener.onRefetchSuccess();
            }

            @Override
            public void onRefreshUserFailed(int error) {
                RLog.d(TAG, "onRefreshUserFailed : " + error);
                userDetailsListener.onRefetchFailure(error);
            }

        };
    }

    @Override
    public void logOut(LogoutListener logoutListener) {
        RLog.d(TAG, "logOut");
        logout(getLogoutHandler(logoutListener));
    }

    @NonNull
    LogoutHandler getLogoutHandler(final LogoutListener logoutListener) {
        RLog.d(TAG, "getLogoutHandler");
        return new LogoutHandler() {
            @Override
            public void onLogoutSuccess() {
                RLog.d(TAG, "onLogoutSuccess");
                logoutListener.onLogoutSuccess();
                notifyLogOutSuccess();
            }

            @Override
            public void onLogoutFailure(int responseCode, String message) {
                RLog.d(TAG, "onLogoutFailure : responseCode = " + responseCode + " message = " + message);
                logoutListener.onLogoutFailure(responseCode, message);
                notifyLogoutFailure(responseCode,message);
            }
        };
    }

    private void notifyLogOutSuccess(){
        synchronized (userLogoutListeners) {
            for (LogoutListener eventListener : userLogoutListeners) {
                if (eventListener != null) {
                    eventListener.onLogoutSuccess();
                }
            }
        }
    }

    private void notifyLogoutFailure(int responseCode, String errorMessage){
        synchronized (userLogoutListeners) {
            for (LogoutListener eventListener : userLogoutListeners) {
                if (eventListener != null) {
                    eventListener.onLogoutFailure(responseCode,errorMessage);
                }
            }
        }
    }

    @Override
    public void updateMarketingOptInConsent(UserDetailsListener userDetailsListener) {
        RLog.d(TAG, "updateMarketingOptInConsent");
        updateReceiveMarketingEmail(getUpdateReceiveMarketingEmailHandler(userDetailsListener), getReceiveMarketingEmail());

    }

    @Override
    public void registerLogOutListener(LogoutListener logoutListener) {
        userLogoutListeners.add(logoutListener);
    }

    @Override
    public void unregisterLogOutListener(LogoutListener logoutListener) {
        userLogoutListeners.remove(logoutListener);
    }

    @NonNull
    protected UpdateUserDetailsHandler getUpdateReceiveMarketingEmailHandler(final UserDetailsListener userDetailsListener) {
        RLog.d(TAG, "getUpdateReceiveMarketingEmailHandler");
        return new UpdateUserDetailsHandler() {
            @Override
            public void onUpdateSuccess() {
                RLog.d(TAG, "getUpdateReceiveMarketingEmailHandler : onUpdateSuccess");
                userDetailsListener.onUpdateSuccess();
            }

            @Override
            public void onUpdateFailedWithError(int error) {
                RLog.e(TAG, "getUpdateReceiveMarketingEmailHandler : onUpdateFailedWithError" + error);
                userDetailsListener.onUpdateFailure(error);
            }
        };
    }
}
