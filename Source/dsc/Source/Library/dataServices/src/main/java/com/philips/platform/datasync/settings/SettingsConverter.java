package com.philips.platform.datasync.settings;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.trackers.DataServicesManager;

import javax.inject.Inject;

/**
 * Created by sangamesh on 24/01/17.
 */

public class SettingsConverter {

    @Inject
    BaseAppDataCreator dataCreator;

    @Inject
    public SettingsConverter() {
        DataServicesManager.getInstance().getAppComponent().injectSettingsConverter(this);
    }

    public Settings convertUcoreToAppSettings(UCoreSettings uCoreSettings) {
        Settings settings = dataCreator.createSettings(uCoreSettings.getUnitSystem(), uCoreSettings.getLocale());
        return settings;
    }

    public UCoreSettings convertAppToUcoreSettings(Settings settings) {
        UCoreSettings uCoreSettings = new UCoreSettings();
        uCoreSettings.setUnitSystem(settings.getUnit());
        uCoreSettings.setLocale(settings.getLocale());
        return uCoreSettings;
    }
}
