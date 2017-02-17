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

@SuppressWarnings({"rawtypes", "unchecked"})
public class SettingsSegregator {

    @Inject
    DBFetchingInterface dbFetchingInterface;

    public SettingsSegregator() {
        DataServicesManager.getInstance().getAppComponant().injectSettingsSegregator(this);
    }

    public Map<Class, List<?>> putSettingsForSync(Map<Class, List<?>> dataToSync) throws SQLException {
        List<? extends Settings> settingsList = (List<? extends Settings>) dbFetchingInterface.fetchNonSyncSettings();
        dataToSync.put(Settings.class, settingsList);
        return dataToSync;
    }


}
