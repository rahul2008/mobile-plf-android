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

    SettingsConverter(BaseAppDataCreator dataCreator) {
        this.dataCreator = dataCreator;
    }

    public Settings convertUcoreToAppSettings(UCoreSettings uCoreSettings) {
        // TODO: IVD Fill in tha TimeZone dude!
        Settings settings = dataCreator.createSettings(uCoreSettings.getUnitSystem(), uCoreSettings.getLocale(), null);
        return settings;
    }

    public UCoreSettings convertAppToUcoreSettings(Settings settings) {
        UCoreSettings uCoreSettings = new UCoreSettings();
        uCoreSettings.setUnitSystem(settings.getUnit());
        uCoreSettings.setLocale(settings.getLocale());
        return uCoreSettings;
    }
}
