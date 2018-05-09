/* Copyright (c) Koninklijke Philips N.V., 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.datasync.settings;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.trackers.DataServicesManager;

import javax.inject.Inject;

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
        return dataCreator.createSettings(uCoreSettings.getUnitSystem(), uCoreSettings.getLocale(), uCoreSettings.getTimeZone());
    }

    public UCoreSettings convertAppToUcoreSettings(Settings settings) {
        UCoreSettings uCoreSettings = new UCoreSettings();
        uCoreSettings.setUnitSystem(settings.getUnit());
        uCoreSettings.setLocale(settings.getLocale());
        uCoreSettings.setTimeZone(settings.getTimeZone());
        return uCoreSettings;
    }
}
