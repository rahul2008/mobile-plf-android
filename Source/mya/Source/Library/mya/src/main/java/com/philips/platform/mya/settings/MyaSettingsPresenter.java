/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;

import android.content.Context;
import android.os.Bundle;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.MyaLocalizationHandler;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.MyaBasePresenter;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class MyaSettingsPresenter extends MyaBasePresenter<MyaSettingsContract.View> implements MyaSettingsContract.Presenter {

    private MyaSettingsContract.View view;

    MyaSettingsPresenter(MyaSettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void getSettingItems(AppInfraInterface appInfra, AppConfigurationInterface.AppConfigurationError error, Bundle arguments) {
        appInfra.getServiceDiscovery().getServiceUrlWithLanguagePreference("userreg.landing.myphilips", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                view.setLinkUrl(url.toString());
            }

            @Override
            public void onError(ERRORVALUES error, String message) {

            }
        });
        view.showSettingsItems(getSettingsMap(appInfra, arguments, error));
    }

    @Override
    public void onClickRecyclerItem(String key, SettingsModel settingsModel) {
    }

    @Override
    public void logOut(Bundle bundle) {
        MyaHelper.getInstance().getMyaListener().onLogoutClicked();
    }

    @Override
    public boolean handleOnClickSettingsItem(String key, FragmentLauncher fragmentLauncher) {
        return false;
    }

    private Map<String, SettingsModel> getSettingsMap(AppInfraInterface appInfraInterface, Bundle arguments, AppConfigurationInterface.AppConfigurationError error) {
        String settingItems = "settings.menuItems";
        List<?> list = null;
        if (arguments != null)
            list = MyaHelper.getInstance().getMyaLaunchInput().getSettingsMenuList();

        if (list == null || list.isEmpty()) {
            try {
                list = (ArrayList<?>) appInfraInterface.getConfigInterface().getPropertyForKey(settingItems, "mya", error);
            } catch (IllegalArgumentException exception) {
                exception.getMessage();
            }
        }
        return getLocalisedList(list, appInfraInterface);
    }

    private LinkedHashMap<String, SettingsModel> getLocalisedList(List<?> propertyForKey, AppInfraInterface appInfraInterface) {
        LinkedHashMap<String, SettingsModel> profileList = new LinkedHashMap<>();
        MyaLocalizationHandler myaLocalizationHandler = new MyaLocalizationHandler();
        SettingsModel privacySettingsModel = new SettingsModel();
        privacySettingsModel.setFirstItem(view.getContext().getString(R.string.Mya_Privacy_Settings));
        profileList.put("Mya_Privacy_Settings", privacySettingsModel);
        if (propertyForKey != null && propertyForKey.size() != 0) {
            for (int i = 0; i < propertyForKey.size(); i++) {
                SettingsModel settingsModel = new SettingsModel();
                String key = (String) propertyForKey.get(i);
                settingsModel.setFirstItem(myaLocalizationHandler.getStringResourceByName(view.getContext(), key));
                if (key.equals("MYA_Country")) {
                    settingsModel.setItemCount(2);
                    settingsModel.setSecondItem(appInfraInterface.getServiceDiscovery().getHomeCountry());
                }
                profileList.put(key, settingsModel);
            }
        }
        return profileList;
    }

    private Context getContext() {
        return view.getContext();
    }

}
