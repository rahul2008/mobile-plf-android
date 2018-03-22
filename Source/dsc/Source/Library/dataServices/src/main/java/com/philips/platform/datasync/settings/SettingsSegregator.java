/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.settings;

import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.trackers.DataServicesManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SettingsSegregator {

    @Inject
    DBFetchingInterface dbFetchingInterface;

    public SettingsSegregator() {
        DataServicesManager.getInstance().getAppComponent().injectSettingsSegregator(this);
    }

    public SettingsSegregator(DBFetchingInterface dbFetchingInterface) {
        this.dbFetchingInterface = dbFetchingInterface;
    }

    public Map<Class, List<?>> putSettingsForSync(Map<Class, List<?>> dataToSync) {
        List<? extends Settings> settingsList = null;
        try {
            settingsList = (List<? extends Settings>) dbFetchingInterface.fetchNonSyncSettings();
        } catch (SQLException e) {
        }
        dataToSync.put(Settings.class, settingsList);
        return dataToSync;
    }


}
