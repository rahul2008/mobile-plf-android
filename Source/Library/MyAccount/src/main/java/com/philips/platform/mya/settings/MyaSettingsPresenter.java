/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;

import static com.philips.platform.mya.launcher.MyaInterface.USER_PLUGIN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.csw.ConsentBundleConfig;
import com.philips.platform.csw.CswDependencies;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswLaunchInput;
import com.philips.platform.mya.R;
import com.philips.platform.mya.base.mvp.MyaBasePresenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

class MyaSettingsPresenter extends MyaBasePresenter<MyaSettingsContract.View> implements MyaSettingsContract.Presenter {

    private MyaSettingsContract.View view;
    private AppInfraInterface appInfraInterface;

    MyaSettingsPresenter(MyaSettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void getSettingItems(Context context, AppInfraInterface appInfra) {
        this.appInfraInterface = appInfra;
        view.showSettingsItems(getSettingsMap(context, appInfra));
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
            AppInfraInterface appInfra = MyaInterface.getMyaDependencyComponent().getAppInfra();
            String appName = (String) appInfra.getConfigInterface().getPropertyForKey("appName", "hdsp", new AppConfigurationInterface.AppConfigurationError());
            String propName = (String) appInfra.getConfigInterface().getPropertyForKey("propositionName", "hdsp", new AppConfigurationInterface.AppConfigurationError());

            ConsentAccessToolKit.getInstance().init(initConsentToolKit(appName, propName, view.getContext(), appInfra));
            CswInterface cswInterface = new CswInterface();

            CswDependencies cswDependencies = new CswDependencies(appInfra);
            UappSettings uappSettings = new UappSettings(view.getContext());
            cswInterface.init(cswDependencies, uappSettings);
            FragmentLauncher fragmentLauncher = new FragmentLauncher((FragmentActivity) view.getContext(), R.id.mainContainer, null);
            cswInterface.launch(fragmentLauncher, buildLaunchInput(true, view.getContext()));
            return true;
        }
        return false;
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

    private LinkedHashMap<String, SettingsModel> getSettingsMap(AppInfraInterface appInfraInterface) {
        String profileItems = "settings.menuItems";
        try {
            final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
            ArrayList propertyForKey = (ArrayList) appInfraInterface.getConfigInterface().getPropertyForKey(profileItems, "mya", configError);
            return getLocalisedList(propertyForKey, appInfraInterface);
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

    private CswLaunchInput buildLaunchInput(boolean addToBackStack, Context context) {

        ConsentBundleConfig config = new ConsentBundleConfig(createConsentDefinitions(Locale.US));
        CswLaunchInput cswLaunchInput = new CswLaunchInput(config, context);
        cswLaunchInput.addToBackStack(addToBackStack);
        return cswLaunchInput;
    }

    private CatkInputs initConsentToolKit(String applicationName, String propostionName, Context context, AppInfraInterface appInfra) {
        CatkInputs catkInputs = new CatkInputs();
        catkInputs.setContext(context);
        catkInputs.setAppInfra(appInfra);
        catkInputs.setApplicationName(applicationName);
        catkInputs.setPropositionName(propostionName);
        return catkInputs;
    }

    private List<ConsentDefinition> createConsentDefinitions(Locale currentLocale) {
        final List<ConsentDefinition> definitions = new ArrayList<>();
        definitions.add(new ConsentDefinition("I allow Philips to store my data in cloud", "The actual content of the help text here", Collections.singletonList("moment"), 1,
                currentLocale));
        definitions.add(new ConsentDefinition("I allow don't Philips to store my data in cloud", "No one is able to see this text in the app", Collections.singletonList("tnemom"),
                1, currentLocale));
        return definitions;

    }
}
