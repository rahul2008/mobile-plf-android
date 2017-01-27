package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Settings;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SettingsBackendSaveRequest extends Event {

    private final List<Settings> settingsList;
    public SettingsBackendSaveRequest(List<Settings> settingsList) {
        this.settingsList = settingsList;
    }

    public List<Settings> getSettingsList() {
        return settingsList;
    }
}
