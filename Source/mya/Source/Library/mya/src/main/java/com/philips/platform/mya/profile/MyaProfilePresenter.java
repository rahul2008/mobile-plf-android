/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.profile;


import android.os.Bundle;
import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.mya.MyaLocalizationHandler;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.MyaBasePresenter;
import com.philips.platform.mya.details.MyaDetailsFragment;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;
import com.philips.platform.myaplugin.uappadaptor.UserDataModel;
import com.philips.platform.myaplugin.user.UserDataModelProvider;

import java.util.ArrayList;
import java.util.TreeMap;


class MyaProfilePresenter extends MyaBasePresenter<MyaProfileContract.View> implements MyaProfileContract.Presenter{

    private MyaProfileContract.View view;

    MyaProfilePresenter(MyaProfileContract.View view) {
        this.view = view;
    }

    @Override
    public void getProfileItems(AppInfraInterface appInfra) {
        view.showProfileItems(getProfileList(appInfra.getConfigInterface()));
    }

    @Override
    public void setUserName(UserDataModelProvider userDataModelProvider) {
        if (userDataModelProvider != null) {
            UserDataModel userDataModel = (UserDataModel) userDataModelProvider.getData(DataModelType.USER);
            setUserModel(userDataModel);
        }
    }

    @Override
    public boolean handleOnClickProfileItem(String profileItem, Bundle bundle) {
        return false;
    }

    MyaDetailsFragment getMyaDetailsFragment() {
        return new MyaDetailsFragment();
    }

    private void setUserModel(UserDataModel userDataModel) {
        String givenName = userDataModel.getGivenName();
        String familyName = userDataModel.getFamilyName();
        if (!TextUtils.isEmpty(givenName) && !TextUtils.isEmpty(familyName) && !familyName.equalsIgnoreCase("null")) {
            view.setUserName(givenName.concat(" ").concat(familyName));
        } else if (!TextUtils.isEmpty(givenName)) {
            view.setUserName(givenName);
        }
    }

    private TreeMap<String, String> getProfileList(AppConfigurationInterface appConfigurationManager) {
        String profileItems = "profile.menuItems";
        try {
            final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                    .AppConfigurationError();
            ArrayList<?> propertyForKey = (ArrayList<?>) appConfigurationManager.getPropertyForKey
                    (profileItems, "mya", configError);
            TreeMap<String, String> treeMap = new TreeMap<>();
            MyaLocalizationHandler myaLocalizationHandler = new MyaLocalizationHandler();
            if (propertyForKey != null && propertyForKey.size() != 0) {
                myaLocalizationHandler.getLocalisedList(view.getContext(), propertyForKey, treeMap);
            } else {
                treeMap.put("MYA_My_details", view.getContext().getString(R.string.MYA_My_details));
            }
            return treeMap;
        } catch (IllegalArgumentException exception) {
            // TODO: Deepthi, use TLA while logging
            exception.getMessage();
        }
        return null;
    }
}
