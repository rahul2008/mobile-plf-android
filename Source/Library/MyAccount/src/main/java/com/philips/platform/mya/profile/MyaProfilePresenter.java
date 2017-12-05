/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.profile;


import android.content.Context;
import android.os.Bundle;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.mvp.MyaBasePresenter;
import com.philips.platform.mya.details.MyaDetailsFragment;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;
import com.philips.platform.myaplugin.uappadaptor.UserDataModel;
import com.philips.platform.myaplugin.user.UserDataModelProvider;

import java.util.ArrayList;
import java.util.TreeMap;

import static com.philips.platform.mya.launcher.MyaInterface.USER_PLUGIN;


class MyaProfilePresenter extends MyaBasePresenter<MyaProfileContract.View> implements MyaProfileContract.Presenter {

    private MyaProfileContract.View view;

    MyaProfilePresenter(MyaProfileContract.View view) {
        this.view = view;
    }

    @Override
    public void getProfileItems(AppInfraInterface appInfra) {
        view.showProfileItems(getProfileList(appInfra.getConfigInterface()));
    }

    @Override
    public void setUserName(Bundle bundle) {
        UserDataModelProvider userDataModelProvider = (UserDataModelProvider) bundle.getSerializable(USER_PLUGIN);
        if (userDataModelProvider != null) {
            UserDataModel userDataModel = (UserDataModel) userDataModelProvider.getData(DataModelType.USER);
            setUserModel(userDataModel);
        }

    }

    @Override
    public boolean handleOnClickProfileItem(String profileItem, Bundle bundle) {
        if (profileItem.equals(view.getContext().getString(R.string.MYA_My_details)) || profileItem.equalsIgnoreCase("MYA_My_details")) {
            MyaDetailsFragment myaDetailsFragment = new MyaDetailsFragment();
            myaDetailsFragment.setArguments(bundle);
            view.showPassedFragment(myaDetailsFragment, MyaHelper.getInstance().getFragmentLauncher());
            return true;
        }
        return false;
    }

    private void setUserModel(UserDataModel userDataModel) {
        if (userDataModel != null && userDataModel.getGivenName() != null) {
            view.setUserName(userDataModel.getGivenName());
        }
    }

    private TreeMap<String,String> getProfileList(AppConfigurationInterface appConfigurationManager) {
        String profileItems = "profile.menuItems";
        try {
            final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                    .AppConfigurationError();
            ArrayList propertyForKey = (ArrayList) appConfigurationManager.getPropertyForKey
                    (profileItems, "mya", configError);
            return getLocalisedList(propertyForKey);
        } catch (IllegalArgumentException exception) {
            // TODO: Deepthi, use TLA while logging
            exception.getMessage();
        }
        return null;
    }

    private TreeMap<String, String> getLocalisedList(ArrayList propertyForKey) {
        TreeMap<String, String> profileList = new TreeMap<>();
        if (propertyForKey != null && propertyForKey.size() != 0) {
            for (int i = 0; i < propertyForKey.size(); i++) {
                String profileKey = (String) propertyForKey.get(i);
                String stringResourceByName = getStringResourceByName(profileKey);
                profileList.put(profileKey, stringResourceByName);
            }
        } else {
            profileList.put("MYA_My_details", view.getContext().getResources().getString(R.string.MYA_My_details));
        }
        return profileList;
    }

    private String getStringResourceByName(String aString) {
        Context context = view.getContext();
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        try {
            return context.getString(resId);
        } catch (Exception exception) {
            return null;
        }
    }
}
