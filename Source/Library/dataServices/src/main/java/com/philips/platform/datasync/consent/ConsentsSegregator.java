package com.philips.platform.datasync.consent;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by 310218660 on 1/10/2017.
 */

public class ConsentsSegregator {

    @Inject
    DBFetchingInterface dbFetchingInterface;

    public ConsentsSegregator(){
        DataServicesManager.getInstance().mAppComponent.injectConsentsSegregator(this);
    }

    public Map<Class, List<?>> putConsentForSync(Map<Class, List<?>> dataToSync) {
        List<? extends Consent> consentList = null;
        try {
            consentList = (List<? extends Consent>) dbFetchingInterface.fetchNonSyncConsentDetails();
            dataToSync.put(Consent.class, consentList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataToSync;
    }

}
