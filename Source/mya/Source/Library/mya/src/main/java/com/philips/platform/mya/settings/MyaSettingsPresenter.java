/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;

import android.os.Bundle;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.MyaLocalizationHandler;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.MyaBasePresenter;
import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class MyaSettingsPresenter extends MyaBasePresenter<MyaSettingsContract.View> implements MyaSettingsContract.Presenter, MyaListener.MyaLogoutListener {

    private MyaSettingsContract.View view;

    MyaSettingsPresenter(MyaSettingsContract.View view) {
        this.view = view;

    }

    @Override
    public void getSettingItems(AppInfraInterface appInfra, AppConfigurationInterface.AppConfigurationError error) {
        appInfra.getServiceDiscovery().getServiceUrlWithLanguagePreference("userreg.landing.myphilips", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                view.setLinkUrl(url.toString());
            }

            @Override
            public void onError(ERRORVALUES error, String message) {

            }
        });
        view.showSettingsItems(getSettingsMap(appInfra));
    }

    @Override
    public void onClickRecyclerItem(String key, SettingsModel settingsModel) {
    }

    @Override
    public void logOut(Bundle bundle) {
        MyaHelper.getInstance().getMyaListener().onLogoutClicked(view.getFragmentActivity(),this);
    }

    @Override
    public boolean handleOnClickSettingsItem(String key, FragmentLauncher fragmentLauncher) {
        return false;
    }

    private Map<String, SettingsModel> getSettingsMap(AppInfraInterface appInfraInterface) {
        List<?> list;
        list = MyaHelper.getInstance().getMyaLaunchInput().getSettingsMenuList();
        return getLocalisedList(list, appInfraInterface);
    }

    private LinkedHashMap<String, SettingsModel> getLocalisedList(List<?> list, AppInfraInterface appInfraInterface) {
        LinkedHashMap<String, SettingsModel> profileList = new LinkedHashMap<>();
        MyaLocalizationHandler myaLocalizationHandler = new MyaLocalizationHandler();
        SettingsModel privacySettingsModel = new SettingsModel();
        privacySettingsModel.setFirstItem(view.getFragmentActivity().getString(R.string.MYA_Privacy_Settings));
        profileList.put("MYA_Privacy_Settings", privacySettingsModel);
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                SettingsModel settingsModel = new SettingsModel();
                String key = (String) list.get(i);
                settingsModel.setFirstItem(myaLocalizationHandler.getStringResourceByName(view.getFragmentActivity(), key));
                if (key.equals("MYA_Country")) {
                    settingsModel.setItemCount(2);
                    settingsModel.setSecondItem(appInfraInterface.getServiceDiscovery().getHomeCountry());
                }
                profileList.put(key, settingsModel);
            }
        }
        return profileList;
    }

    @Override
    public void onLogoutSuccess() {
        view.exitMyAccounts();
    }

    @Override
    public void onLogOutFailure() {
        // is defined for future use
    }
}
