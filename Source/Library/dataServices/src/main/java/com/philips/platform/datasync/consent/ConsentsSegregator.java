/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.consent;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.trackers.DataServicesManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ConsentsSegregator {

    @Inject
    DBFetchingInterface dbFetchingInterface;

    public ConsentsSegregator() {
        DataServicesManager.getInstance().getAppComponant().injectConsentsSegregator(this);
    }

    public Map<Class, List<?>> putConsentForSync(Map<Class, List<?>> dataToSync) throws SQLException {
        List<? extends ConsentDetail> consentList = (List<? extends ConsentDetail>) dbFetchingInterface.fetchNonSyncConsentDetails();
        dataToSync.put(ConsentDetail.class, consentList);
        return dataToSync;
    }

}
