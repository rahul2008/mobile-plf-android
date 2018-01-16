/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.uappframework.uappadaptor;

import android.content.Context;

import com.philips.platform.uappframework.uappadaptor.listeners.LogoutListener;
import com.philips.platform.uappframework.uappadaptor.listeners.UpdateMarketingConsentListener;
import com.philips.platform.uappframework.uappadaptor.listeners.UserRefreshListener;

import java.io.Serializable;

/**
 * Data interface for getting the user data
 * @since 2018.1.0
 */
public abstract class UserDataInterface implements DataInterface, Serializable {


    private static final long serialVersionUID = -4693715099025274805L;

    /**
     * get the user data model
     * @param dataModelType type of data model, user in this case
     * @return returns user data model
     * @since 2018.1.0
     */
    @Override
    public abstract DataModel getData(DataModelType dataModelType);

    /**
     * Check if the user is logged in
     * @param context Context
     * @return returns true if user is logged in
     */
    public abstract boolean isUserLoggedIn(Context context);

    /**
     * Check the user's consent for marketing opt in
     * @param context Context
     * @return returns true if opted in
     * @since 2018.1.0
     */
    public abstract boolean getMarketingOptInConsent(Context context);

    /**
     * To refesh the user
     * @param userRefreshListener listener for user refresh
     * @since 2018.1.0
     */
    public abstract void userRefresh(UserRefreshListener userRefreshListener);

    /**
     * log out the user
     * @param context Context
     * @param logoutListener listener for logout
     * @since 2018.1.0
     */
    public abstract void logOut(Context context, LogoutListener logoutListener);

    /**
     * Update the marketing consnt of the user
     * @param updateMarketingConsentListener listener for updating marketing consent
     * @since 2018.1.0
     */
    public abstract void updateMarketingOptInConsent(UpdateMarketingConsentListener updateMarketingConsentListener);







}
