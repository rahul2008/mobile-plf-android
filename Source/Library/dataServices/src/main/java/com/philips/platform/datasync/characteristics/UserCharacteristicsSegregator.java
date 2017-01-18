package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.datatypes.UserCharacteristics;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.sql.SQLException;

import javax.inject.Inject;

/**
 * Created by indrajitkumar on 1/18/17.
 */

public class UserCharacteristicsSegregator {
    @Inject
    DBFetchingInterface dbFetchingInterface;
    @Inject
    DBUpdatingInterface dbUpdatingInterface;

    public UserCharacteristicsSegregator() {
        DataServicesManager.getInstance().mAppComponent.injectUserCharacteristicsSegregator(this);
    }


    public boolean processCharacteristicsReceivedFromDataCore(UserCharacteristics userCharacteristics, DBRequestListener dbRequestListener) {
        UserCharacteristics dbUC;
        try {
            dbUC = dbFetchingInterface.fetchUCByCreatorId(userCharacteristics.getCreatorId());
            if (dbUC != null) {
                if (!dbUC.isSynchronized()) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
