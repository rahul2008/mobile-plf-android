/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.datatypes.UserCharacteristics;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.sql.SQLException;

import javax.inject.Inject;

@SuppressWarnings({"rawtypes", "unchecked"})
public class UserCharacteristicsSegregator {
    @Inject
    DBFetchingInterface dbFetchingInterface;
    @Inject
    DBUpdatingInterface dbUpdatingInterface;

    public UserCharacteristicsSegregator() {
        DataServicesManager.getInstance().getAppComponant().injectUserCharacteristicsSegregator(this);
    }


    public boolean processCharacteristicsReceivedFromDataCore(UserCharacteristics userCharacteristics,
                                                              DBRequestListener dbRequestListener) {
        try {
            UserCharacteristics dbUc = dbFetchingInterface.fetchUCByCreatorId(userCharacteristics.getCreatorId());
            if (dbUc != null) {
                if (!dbUc.isSynchronized()) {
                    return false;
                }
            }
        } catch (SQLException e) {
            dbFetchingInterface.postError(e, dbRequestListener);
        }
        return true;
    }
}
