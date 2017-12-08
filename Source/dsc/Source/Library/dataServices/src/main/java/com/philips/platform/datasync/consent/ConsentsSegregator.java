/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
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
        DataServicesManager.getInstance().getAppComponent().injectConsentsSegregator(this);
    }

    public Map<Class, List<?>> putConsentForSync(Map<Class, List<?>> dataToSync) {
        List<? extends ConsentDetail> consentList = null;
        try {
            consentList = (List<? extends ConsentDetail>) dbFetchingInterface.fetchNonSyncConsentDetails();
        } catch (SQLException e) {
            //Debug Log
        }
        dataToSync.put(ConsentDetail.class, consentList);
        return dataToSync;
    }

}
