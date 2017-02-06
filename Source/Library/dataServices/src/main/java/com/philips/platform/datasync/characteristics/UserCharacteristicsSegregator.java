/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.datatypes.OrmTableType;
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
        DataServicesManager.getInstance().getAppComponant().injectUserCharacteristicsSegregator(this);
    }

    public boolean isUCSynced() throws SQLException {
        return dbFetchingInterface.isSynced(OrmTableType.CHARACTERISTICS.getId());
    }
}
