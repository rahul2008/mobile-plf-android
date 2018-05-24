package com.philips.cdp2.commlib.core.store;

import android.content.Context;
import android.support.annotation.Nullable;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.platform.appinfra.AppInfraInterface;

import java.io.File;

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

        final NetworkNodeDatabase database = new NetworkNodeDatabase(dbHelper);

        if (runtimeConfiguration != null) {
            migrate(runtimeConfiguration.getContext(), database);
        }
        return database;
    }

    private static void migrate(Context context, NetworkNodeDatabase newDatabase) {
        File oldDatabaseFile = context.getDatabasePath(OpenNetworkNodeDatabaseHelper.DB_NAME);

        if (oldDatabaseFile.exists()) {
            OpenNetworkNodeDatabaseHelper oldDBHelper = new OpenNetworkNodeDatabaseHelper();
            final NetworkNodeDatabase oldDatabase = new NetworkNodeDatabase(oldDBHelper);
            for (NetworkNode networkNode : oldDatabase.getAll()) {
                newDatabase.save(networkNode);
            }

            oldDatabaseFile.delete();
        }
    }
}
