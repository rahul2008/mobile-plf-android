/*
 * Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.pif.DataInterface.USR;

import android.content.Context;

import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pif.DataInterface.USR.listeners.HSDPAuthenticationListener;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutSessionListener;
import com.philips.platform.pif.DataInterface.USR.listeners.RefetchUserDetailsListener;
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshSessionListener;
import com.philips.platform.pif.DataInterface.USR.listeners.UserDataListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Data interface for getting the user data
 *
 * @since 2018.1.0
 */
public interface UserDataInterface extends Serializable {


    /**
     * Get the user details hashmap on sending ArrayList of respective valid keys. All details HashMap returned on sending empty ArrayList. Throws exception on sending invalid key.
     *
     * @param detailKeys the list of the keys for which user data is needed
     * @return hashmap of user's details
     * @since 2018.1.0
     */
    HashMap<String, Object> getUserDetails(ArrayList<String> detailKeys) throws Exception;


    /**
     * Get the HSDP access token
     *
     * @return returns the HSDP access token
     * @since 2018.1.0
     */
    @Deprecated
    String getHSDPAccessToken();

    /**
     * Get the HSDP UUID
     *
     * @return returns the HSDP UUID
     * @since 2018.1.0
     */
    @Deprecated
    String getHSDPUUID();

    /**
     * {@code getUserSignInState} method checks a user is logged in state
     *
     * @return UserLoginState
     * @since 1804.0
     */
    UserLoggedInState getUserLoggedInState();

    /**
     * {@code authorizeHSDP} method authorize a user is log-in in HSDP Backend
     *
     * @param hsdpAuthenticationListener
     * @since 1804.0
     */
    @Deprecated
    void authorizeHsdp(HSDPAuthenticationListener hsdpAuthenticationListener);

    /**
     * To refesh the user
     *
     * @param refreshListener listener for user refresh
     * @since 2018.1.0
     */
    void refreshSession(RefreshSessionListener refreshSessionListener);


    /**
     * log out the user
     *
     * @param logoutListener listener for logout
     * @since 2018.1.0
     */
    void logoutSession(LogoutSessionListener logoutSessionListener);

    /**
     * Refetch the details of the user from server
     *
     * @param userDetailsListener listener for refetching the details
     * @since 2018.1.0
     */
    void refetchUserDetails(RefetchUserDetailsListener userDetailsListener);


    void addUserDataInterfaceListener(UserDataListener listener);

    void removeUserDataInterfaceListener(UserDataListener listener);


}
