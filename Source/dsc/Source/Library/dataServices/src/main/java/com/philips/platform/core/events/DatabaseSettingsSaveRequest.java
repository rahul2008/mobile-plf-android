package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.DBRequestListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DatabaseSettingsSaveRequest extends Event {

    private  final Settings settings;
    private final DBRequestListener<Settings> dbRequestListener;

    public DBRequestListener<Settings> getDbRequestListener() {
        return dbRequestListener;
    }

    public DatabaseSettingsSaveRequest(Settings settings, DBRequestListener<Settings> dbRequestListener) {
        this.settings = settings;

        this.dbRequestListener = dbRequestListener;

    }

    public Settings getSettings() {
        return settings;
    }
}
