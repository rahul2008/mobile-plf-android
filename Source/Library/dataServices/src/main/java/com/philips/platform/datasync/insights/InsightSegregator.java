/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.insights;

import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.trackers.DataServicesManager;

import javax.inject.Inject;

@SuppressWarnings({"rawtypes", "unchecked"})
public class InsightSegregator {

    @Inject
    DBFetchingInterface dbFetchingInterface;

    public InsightSegregator() {
        DataServicesManager.getInstance().getAppComponant().injectInsightSegregator(this);
    }


}
