/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.profile;


import android.content.Context;
import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.util.MYALog;
import com.philips.platform.mya.util.mvp.MyaBasePresenter;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.mya.MyaConstants.MY_ACCOUNTS;


class MyaProfilePresenter extends MyaBasePresenter<MyaProfileContract.View> implements MyaProfileContract.Presenter {

    private MyaProfileContract.View view;

    MyaProfilePresenter(MyaProfileContract.View view) {
        this.view = view;
    }

    @Override
    public void getProfileItems(Context context, AppInfraInterface appInfra) {
        view.showProfileItems(getProfileList(context, MyaInterface.getMyaDependencyComponent().getAppInfra().getConfigInterface()));
    }

    private List<String> getProfileList(Context context, AppConfigurationInterface appConfigurationManager) {
        String profileItems = "profile.menuItems";
        try {
            final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                    .AppConfigurationError();
            ArrayList propertyForKey = (ArrayList) appConfigurationManager.getPropertyForKey
                    (profileItems, "mya", configError);
            return getLocalisedList(context, propertyForKey);
        } catch (IllegalArgumentException exception) {
            MYALog.e(MY_ACCOUNTS, " Error in reading profile menu items ");
        }
        return null;
    }

    private ArrayList<String> getLocalisedList(Context context, ArrayList propertyForKey) {
        ArrayList<String> localizedStrings = new ArrayList<>();
        if (propertyForKey != null && propertyForKey.size() != 0) {
            for (int i = 0; i < propertyForKey.size(); i++) {
                String profileKey = (String) propertyForKey.get(i);
                String stringResourceByName = getStringResourceByName(context, profileKey);
                if (!TextUtils.isEmpty(stringResourceByName))
                    localizedStrings.add(stringResourceByName);
                else
                    localizedStrings.add(profileKey);
            }
        }
        return localizedStrings;
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
