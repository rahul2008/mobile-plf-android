package com.philips.cdp.di.iap.integration;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

/**
 * Created by 310164421 on 8/24/2016.
 */
public class MockIAPDependencies extends IAPDependencies {
    public MockIAPDependencies(AppInfraInterface appInfra, UserDataInterface userDataInterface) {
        super(appInfra,userDataInterface);
    }

    public MockIAPDependencies() {
        super(null,null);
    }
}
