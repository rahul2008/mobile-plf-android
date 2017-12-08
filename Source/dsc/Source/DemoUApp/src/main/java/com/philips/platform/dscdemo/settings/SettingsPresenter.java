package com.philips.platform.dscdemo.settings;

import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

class SettingsPresenter {

    private final DBRequestListener<Settings> dbRequestListener;

    SettingsPresenter(DBRequestListener<Settings> dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
    }

    void updateSettings(Settings settings) {
        DataServicesManager.getInstance().updateUserSettings(settings, dbRequestListener);
    }

}
