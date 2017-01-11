package com.philips.platform.datasync.consent;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
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

    ConsentsSegregator(){
        DataServicesManager.getInstance().mAppComponent.injectConsentsSegregator(this);
    }
}
