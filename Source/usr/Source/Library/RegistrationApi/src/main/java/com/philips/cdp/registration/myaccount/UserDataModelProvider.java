/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.cdp.registration.myaccount;

import android.content.Context;

import com.philips.cdp.registration.User;

import java.io.Serializable;


public class UserDataModelProvider extends UserInterface implements Serializable {

    private static final long serialVersionUID = -8976502136083301892L;
    private transient UserDataModel userDataModel;
    private final transient User user;

    public UserDataModelProvider(User user) {
        this.user = user;
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

    void fillUserData() {
        userDataModel.setName(user.getDisplayName());
        userDataModel.setBirthday(user.getDateOfBirth());
        userDataModel.setEmail(user.getEmail());
        userDataModel.setAccessToken(user.getDisplayName());
        userDataModel.setGivenName(user.getGivenName());
        userDataModel.setBirthday(user.getDateOfBirth());
        userDataModel.setEmailVerified(user.isEmailVerified());
        userDataModel.setMobileNumber(user.getMobile());
        userDataModel.setMobileVerified(user.isMobileVerified());
        userDataModel.setGender(user.getGender());
//        userDataModel.setVerified(user.isTermsAndConditionAccepted());
        userDataModel.setFamilyName(user.getFamilyName());
    }

}
