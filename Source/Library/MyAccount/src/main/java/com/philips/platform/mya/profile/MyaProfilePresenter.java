/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.profile;


import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.mya.R;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.base.mvp.MyaBasePresenter;

import java.util.ArrayList;
import java.util.TreeMap;


class MyaProfilePresenter extends MyaBasePresenter<MyaProfileContract.View> implements MyaProfileContract.Presenter {

    private MyaProfileContract.View view;

    MyaProfilePresenter(MyaProfileContract.View view) {
        this.view = view;
    }

    @Override
    public void getProfileItems(Context context, AppInfraInterface appInfra) {
        view.showProfileItems(getProfileList(context, MyaInterface.getMyaDependencyComponent().getAppInfra().getConfigInterface()));
    }

    private TreeMap<String,String> getProfileList(Context context, AppConfigurationInterface appConfigurationManager) {
        String profileItems = "profile.menuItems";
        try {
            final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                    .AppConfigurationError();
            ArrayList propertyForKey = (ArrayList) appConfigurationManager.getPropertyForKey
                    (profileItems, "mya", configError);
            return getLocalisedList(context,propertyForKey);
        } catch (IllegalArgumentException exception) {
            // TODO: Deepthi, use TLA while logging
            exception.getMessage();
        }
        return null;
    }

    private TreeMap<String, String> getLocalisedList(Context context, ArrayList propertyForKey) {
        TreeMap<String, String> profileList = new TreeMap<>();
        if (propertyForKey != null && propertyForKey.size() != 0) {
            for (int i = 0; i < propertyForKey.size(); i++) {
                String profileKey = (String) propertyForKey.get(i);
                String stringResourceByName = getStringResourceByName(context, profileKey);
                profileList.put(profileKey, stringResourceByName);
            }
        } else {
            profileList.put("MYA_My_details", context.getResources().getString(R.string.MYA_My_details));
        }
        return profileList;
    }

    private String getStringResourceByName(Context context, String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        try {
            return context.getString(resId);
        } catch (Exception exception) {
            return null;
        }
    }
}
