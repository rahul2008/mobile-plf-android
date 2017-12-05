/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.csw.ConsentBundleConfig;
import com.philips.platform.csw.CswDependencies;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswLaunchInput;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.mvp.MyaBasePresenter;
import com.philips.platform.myaplugin.user.UserDataModelProvider;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.philips.platform.mya.launcher.MyaInterface.USER_PLUGIN;

class MyaSettingsPresenter extends MyaBasePresenter<MyaSettingsContract.View> implements MyaSettingsContract.Presenter {

    private MyaSettingsContract.View view;

    MyaSettingsPresenter(MyaSettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void getSettingItems(AppInfraInterface appInfra, AppConfigurationInterface.AppConfigurationError error) {
        view.showSettingsItems(getSettingsMap(appInfra, error));
    }

    @Override
    public void onClickRecyclerItem(String key, SettingsModel settingsModel) {
        if (key.equals("MYA_Country")) {
            Context context = view.getContext();
            view.showDialog(context.getString(R.string.MYA_change_country), context.getString(R.string.MYA_change_country_message));
        }
    }

    @Override
    public void logOut(Bundle bundle) {
        UserDataModelProvider userDataModelProvider = (UserDataModelProvider) bundle.getSerializable(USER_PLUGIN);
        if (userDataModelProvider != null) {
            userDataModelProvider.logOut(getLogoutHandler());
        }
    }

    @Override
    public boolean handleOnClickSettingsItem(String key) {
        if (key.equals("Mya_Privacy_Settings")) {
            AppInfraInterface appInfra = MyaHelper.getInstance().getAppInfra();
            getConsentAccessInstance().init(initConsentToolKit(view.getContext(), appInfra));
            CswInterface cswInterface = getCswInterface();
            CswDependencies cswDependencies = new CswDependencies(appInfra);
            UappSettings uappSettings = new UappSettings(view.getContext());
            cswInterface.init(cswDependencies, uappSettings);
            cswInterface.launch(getFragmentLauncher(), buildLaunchInput(true, view.getContext()));
            return true;
        }
        return false;
    }

    ConsentAccessToolKit getConsentAccessInstance() {
        return ConsentAccessToolKit.getInstance();
    }

    CswInterface getCswInterface() {
        return new CswInterface();
    }

    FragmentLauncher getFragmentLauncher() {
        return MyaHelper.getInstance().getFragmentLauncher();
    }

    LogoutHandler getLogoutHandler() {
        return new LogoutHandler() {
            public void onLogoutSuccess() {
                view.handleLogOut();
            }

            public void onLogoutFailure(int responseCode, String message) {
                //TODO - need to discuss with design team and handle on logout failure
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    CswLaunchInput buildLaunchInput(boolean addToBackStack, Context context) {
        ConsentBundleConfig config = new ConsentBundleConfig(MyaHelper.getInstance().getMyaLaunchInput().getConsentDefinitions());
        CswLaunchInput cswLaunchInput = new CswLaunchInput(config, context);
        cswLaunchInput.addToBackStack(addToBackStack);
        return cswLaunchInput;
    }

    CatkInputs initConsentToolKit(Context context, AppInfraInterface appInfra) {
        CatkInputs catkInputs = new CatkInputs();
        catkInputs.setContext(context);
        catkInputs.setAppInfra(appInfra);
        return catkInputs;
    }

    private Map<String, SettingsModel> getSettingsMap(AppInfraInterface appInfraInterface, AppConfigurationInterface.AppConfigurationError error) {
        String profileItems = "settings.menuItems";
        try {
            ArrayList propertyForKey = (ArrayList) appInfraInterface.getConfigInterface().getPropertyForKey(profileItems, "mya", error);
            return getLocalisedList(propertyForKey, appInfraInterface);
        } catch (IllegalArgumentException exception) {
            exception.getMessage();
        }
        return null;
    }

    private LinkedHashMap<String, SettingsModel> getLocalisedList(ArrayList propertyForKey, AppInfraInterface appInfraInterface) {
        LinkedHashMap<String, SettingsModel> profileList = new LinkedHashMap<>();
        if (propertyForKey != null && propertyForKey.size() != 0) {
            for (int i = 0; i < propertyForKey.size(); i++) {
                SettingsModel settingsModel = new SettingsModel();
                String key = (String) propertyForKey.get(i);
                settingsModel.setFirstItem(getStringResourceByName(key));
                if (key.equals("MYA_Country")) {
                    settingsModel.setItemCount(2);
                    settingsModel.setSecondItem(appInfraInterface.getServiceDiscovery().getHomeCountry());
                }
                profileList.put(key, settingsModel);
            }
        } else {
            SettingsModel countrySettingsModel = new SettingsModel();
            countrySettingsModel.setItemCount(2);
            countrySettingsModel.setFirstItem(view.getContext().getString(R.string.MYA_Country));
            countrySettingsModel.setSecondItem(appInfraInterface.getServiceDiscovery().getHomeCountry());
            profileList.put("MYA_Country", countrySettingsModel);
            SettingsModel privacySettingsModel = new SettingsModel();
            privacySettingsModel.setFirstItem(view.getContext().getString(R.string.Mya_Privacy_Settings));
            profileList.put("Mya_Privacy_Settings", privacySettingsModel);
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
