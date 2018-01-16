/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.myaplugin.user;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.platform.uappframework.uappadaptor.DataModel;
import com.philips.platform.uappframework.uappadaptor.DataModelType;
import com.philips.platform.uappframework.uappadaptor.UserDataInterface;
import com.philips.platform.uappframework.uappadaptor.UserDataModel;
import com.philips.platform.uappframework.uappadaptor.listeners.LogoutListener;
import com.philips.platform.uappframework.uappadaptor.listeners.UpdateMarketingConsentListener;
import com.philips.platform.uappframework.uappadaptor.listeners.UserRefreshListener;

import java.io.Serializable;


public class UserDataModelProvider extends UserDataInterface implements Serializable {

    private static final long serialVersionUID = -8976502136083301892L;
    private transient UserDataModel userDataModel;
    private transient Context context;

    public UserDataModelProvider(Context context) {
        this.context = context;
        if (userDataModel == null) {
            userDataModel = new UserDataModel();
        }
    }

    @Override
    public DataModel getData(DataModelType dataModelType) {
        if (userDataModel == null) {
            userDataModel = new UserDataModel();
        }
        fillUserData();
        return userDataModel;
    }

    @Override
    public boolean isUserLoggedIn(Context context) {
        User user = new User(context);
        return user.isUserSignIn();
    }

    @Override
    public boolean getMarketingOptInConsent(Context context) {
        return false;
    }

    @Override
    public void userRefresh(UserRefreshListener userRefreshListener) {

    }

    @Override
    public void logOut(Context context, LogoutListener logoutListener) {

    }

    @Override
    public void updateMarketingOptInConsent(UpdateMarketingConsentListener updateMarketingConsentListener) {

    }


    private void fillUserData() {
        User user = getUser();
        userDataModel.setUserFirstName(user.getDisplayName());
        userDataModel.setEmailAddress(user.getEmail());
        userDataModel.setAccessToken(user.getDisplayName());
        userDataModel.setUserLastName(user.getGivenName());
        userDataModel.setDateOfBirth(user.getDateOfBirth());
        userDataModel.setMobileNumber(user.getMobile());
        userDataModel.setGender(user.getGender().toString());
    }

    User getUser() {
        return new User(context);
    }


}
