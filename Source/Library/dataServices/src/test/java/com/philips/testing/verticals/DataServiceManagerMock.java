package com.philips.testing.verticals;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;

/**
 * Created by indrajitkumar on 13/12/16.
 */

public class DataServiceManagerMock {

    DataServicesManager dataServicesManager;
    private UCoreAccessProvider uCoreAccessProvider;

    public DataServiceManagerMock() {
        this(DataServicesManager.getInstance());
    }

    public DataServiceManagerMock(DataServicesManager dataServicesManager) {
        this.dataServicesManager = dataServicesManager;
    }

    /*public BaseAppDataCreator getDataCreater() {
        return dataServicesManager.getDataCreater();
    }*/

    public UCoreAccessProvider getUCoreAccessProvider() {
        return uCoreAccessProvider;
    }
}
