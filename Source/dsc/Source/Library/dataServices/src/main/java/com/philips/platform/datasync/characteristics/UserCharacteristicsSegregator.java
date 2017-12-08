/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
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
        DataServicesManager.getInstance().getAppComponent().injectUserCharacteristicsSegregator(this);
    }

    public boolean isUCSynced() throws SQLException {
        return dbFetchingInterface.isSynced(SyncType.CHARACTERISTICS.getId());
    }
}
