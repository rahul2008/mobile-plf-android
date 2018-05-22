package com.philips.cdp2.commlib.core.store;

import android.support.annotation.Nullable;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.platform.appinfra.AppInfraInterface;

public final class NetworkNodeDatabaseFactory {

    private NetworkNodeDatabaseFactory() {
    }

    public static NetworkNodeDatabase create(final @Nullable RuntimeConfiguration runtimeConfiguration) {
        NetworkNodeDBHelper dbHelper = null;

        if (runtimeConfiguration != null) {
            AppInfraInterface appInfraInterface = runtimeConfiguration.getAppInfraInterface();

            if (appInfraInterface != null) {
                dbHelper = new SecureNetworkNodeDatabaseHelper(appInfraInterface);
            }
        }

        if (dbHelper == null) {
            dbHelper = new OpenNetworkNodeDatabaseHelper();
        }

        return new NetworkNodeDatabase(dbHelper);
    }
}
