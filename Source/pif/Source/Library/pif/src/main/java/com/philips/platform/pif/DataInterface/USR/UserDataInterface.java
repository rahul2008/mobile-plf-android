/*
 * Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.pif.DataInterface.USR;

import android.content.Context;

import com.philips.platform.pif.DataInterface.DataInterface;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutListener;
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshListener;
import com.philips.platform.pif.DataInterface.USR.listeners.UserDetailsListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Data interface for getting the user data
 * @since 2018.1.0
 */
public interface UserDataInterface extends DataInterface {


    /**
     * Get the user details hashmap on sending ArrayList of respective valid keys. All details HashMap returned on sending empty ArrayList. Throws exception on sending invalid key.
     * @param detailKeys the list of the keys for which user data is needed
     * @return hashmap of user's details
     * @since 2018.1.0
     */
     HashMap<String,Object> getUserDetails(ArrayList<String> detailKeys) throws Exception;

    /**
     * Get the janrain UUID
     * @return returns the UUID
     * @since 2018.1.0
     */
    String getJanrainUUID();

    /**
     * Get the janrain access token
     * @return returns the access token
     * @since 2018.1.0
     */
    String getJanrainAccessToken();

    /**
     * Get the HSDP access token
     * @return returns the HSDP access token
     * @since 2018.1.0
     */
    String getHSDPAccessToken();

    /**
     * Get the HSDP UUID
     * @return returns the HSDP UUID
     * @since 2018.1.0
     */
    String getHSDPUUID();

    /**
     * Check if the user is logged in
     * @param context Context
     * @return returns true if user is logged in
     */
     boolean isUserLoggedIn(Context context);

    /**
     * To refesh the user
     * @param refreshListener listener for user refresh
     * @since 2018.1.0
     */
     void refreshLoginSession(RefreshListener refreshListener);

    /**
     * log out the user
     * @param logoutListener listener for logout
     * @since 2018.1.0
     */
     void logOut(LogoutListener logoutListener);

    /**
     * Refetch the details of the user from server
     * @param userDetailsListener listener for refetching the details
     * @since 2018.1.0
     */
    void refetch(UserDetailsListener userDetailsListener);

    /**
     * Update the marketing consent of the user
     * @param userDetailsListener listener for updating marketing consent
     * @since 2018.1.0
     */
     void updateMarketingOptInConsent(UserDetailsListener userDetailsListener);

}
