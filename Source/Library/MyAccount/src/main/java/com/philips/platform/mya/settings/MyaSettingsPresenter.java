/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;


import android.content.Context;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.mvp.MyaBasePresenter;

import java.util.ArrayList;
import java.util.LinkedHashMap;


class MyaSettingsPresenter extends MyaBasePresenter<MyaSettingsContract.View> implements MyaSettingsContract.Presenter {

    private MyaSettingsContract.View view;

    MyaSettingsPresenter(MyaSettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void getSettingItems(Context context, AppInfraInterface appInfra) {
        view.showSettingsItems(getSettingsMap(context, appInfra));
    }

    @Override
    public void onClickRecyclerItem(Context context, String key, SettingsModel settingsModel) {
       if (key.equals("MYA_Country")) {
            view.showDialog(context.getString(R.string.MYA_change_country), context.getString(R.string.MYA_change_country_message));
       }
    }

    @Override
    public void logOut() {
        HsdpUser user = new HsdpUser(view.getContext());
        User mUser = new User(view.getContext());
        if (RegistrationConfiguration.getInstance().isHsdpFlow() && null != user.getHsdpUserRecord()) {
            user.logOut(getLogoutHandler());
        } else {
            mUser.logout(getLogoutHandler());
        }
    }

    private LogoutHandler getLogoutHandler() {
        return new LogoutHandler() {
            public void onLogoutSuccess() {
                view.handleLogOut();
            }

            public void onLogoutFailure(int responseCode, String message) {
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private LinkedHashMap<String, SettingsModel> getSettingsMap(Context context, AppInfraInterface appInfraInterface) {
        String profileItems = "settings.menuItems";
        try {
            final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                    .AppConfigurationError();
            ArrayList propertyForKey = (ArrayList) appInfraInterface.getConfigInterface().getPropertyForKey
                    (profileItems, "mya", configError);
            return getLocalisedList(context, propertyForKey, appInfraInterface);
        } catch (IllegalArgumentException exception) {
            exception.getMessage();
        }
        return null;
    }

    private LinkedHashMap<String, SettingsModel> getLocalisedList(Context context, ArrayList propertyForKey, AppInfraInterface appInfraInterface) {
        LinkedHashMap<String, SettingsModel> profileList = new LinkedHashMap<>();
        if (propertyForKey != null && propertyForKey.size() != 0) {
            for (int i = 0; i < propertyForKey.size(); i++) {
                SettingsModel settingsModel = new SettingsModel();
                String key = (String) propertyForKey.get(i);
                settingsModel.setFirstItem(getStringResourceByName(context, key));
                if (key.equals("MYA_Country")) {
                    settingsModel.setItemCount(2);
                    settingsModel.setSecondItem(appInfraInterface.getServiceDiscovery().getHomeCountry());
                }
                profileList.put(key, settingsModel);
            }
        } else {
            SettingsModel countrySettingsModel = new SettingsModel();
            countrySettingsModel.setFirstItem(context.getResources().getString(R.string.MYA_Country));
            countrySettingsModel.setSecondItem(appInfraInterface.getServiceDiscovery().getHomeCountry());
            profileList.put("MYA_Country", countrySettingsModel);
            SettingsModel privacySettingsModel = new SettingsModel();
            privacySettingsModel.setFirstItem(context.getResources().getString(R.string.Mya_Privacy_Settings));
            profileList.put("Mya_Privacy_Settings", privacySettingsModel);
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
