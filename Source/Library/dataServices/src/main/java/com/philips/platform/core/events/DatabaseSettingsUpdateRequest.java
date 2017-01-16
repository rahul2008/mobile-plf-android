package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DatabaseSettingsUpdateRequest extends Event {

    private List<Settings> settingsList;
    private final DBRequestListener dbRequestListener;

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    public DatabaseSettingsUpdateRequest(List<Settings> settingsList,DBRequestListener dbRequestListener) {
        this.settingsList = settingsList;
        this.dbRequestListener = dbRequestListener;

    }

    public List<Settings> getSettingsList() {
        return settingsList;
    }
}
