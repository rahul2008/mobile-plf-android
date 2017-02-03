package com.philips.platform.datasync.settings;

import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.trackers.DataServicesManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by 310218660 on 1/10/2017.
 */

public class SettingsSegregator {

    @Inject
    DBFetchingInterface dbFetchingInterface;

    public SettingsSegregator(){
        DataServicesManager.getInstance().getAppComponant().injectSettingsSegregator(this);
    }

    public Map<Class, List<?>> putSettingsForSync(Map<Class, List<?>> dataToSync) {
        List<? extends Settings> settingsList = null;
        try {
            settingsList = (List<? extends Settings>) dbFetchingInterface.fetchNonSyncSettings();
            dataToSync.put(Settings.class, settingsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataToSync;
    }


}
