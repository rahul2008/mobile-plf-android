package com.philips.platform.datasync.settings;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.consent.UCoreConsentDetail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sangamesh on 24/01/17.
 */

public class SettingsConverter {

    @Inject
    BaseAppDataCreator dataCreator;

    @Inject
    public SettingsConverter() {
        DataServicesManager.getInstance().getAppComponant().injectSettingsConverter(this);
    }
    public List<Settings> convertUcoreToAppSettings(UCoreSettings uCoreSettings){

        List<Settings> settingsList=new ArrayList<>();
        Settings settingsMetric = dataCreator.createSettings(Settings.METRICS,uCoreSettings.getUnitSystem());
        Settings settingsLocale = dataCreator.createSettings(Settings.LOCALE,uCoreSettings.getLocale());

        settingsList.add(settingsMetric);
        settingsList.add(settingsLocale);

        return settingsList;
    }

    public UCoreSettings convertAppToUcoreSettings(List<Settings> settingsList) {

        UCoreSettings uCoreSettings=new UCoreSettings();

        for(Settings settings:settingsList){

            if(settings.getType().equalsIgnoreCase(Settings.METRICS)){
                uCoreSettings.setUnitSystem(settings.getValue());
            }else {
                uCoreSettings.setLocale(settings.getValue());
            }
        }

        return uCoreSettings;
    }
}
