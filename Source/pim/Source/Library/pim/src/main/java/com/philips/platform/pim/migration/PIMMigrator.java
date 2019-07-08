package com.philips.platform.pim.migration;

import android.content.Context;
import com.philips.platform.appinfra.logging.LoggingInterface;

public class PIMMigrator {

    private Context context;
    private final String TAG = PIMMigrator.class.getSimpleName();
    private LoggingInterface mLoggingInterface;

    public PIMMigrator(Context context) {
        this.context = context;
    }

    public void migrateUSRToPIM() {
        migrateUser();
    }
    void migrateUser(){
        PIMMigrationManager pimMigrationManager = new PIMMigrationManager(context);
        pimMigrationManager.migrateUser("zz3zekjb3qtgsjf9"); //TODO: Shashi, passing hardcoded accesstoken for further development.Late, need to fetch from JRAccessTokenRefresh.
    }
}
