/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.background;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlibexplorer.CommlibExplorerApp;
import com.philips.cdp2.commlibexplorer.appliance.GenericApplianceFactory;
import com.philips.cdp2.commlibexplorer.strategy.CommStrategy;

public class BackgroundConnectionService extends Service {
    private static final String TAG = "BgConnectionService";
    private CommCentral commCentral;
    private CommStrategy commStrategy;
    private final IBinder binder = new BackgroundBinder();

    public class BackgroundBinder extends Binder {
        public BackgroundConnectionService getService() {
            return BackgroundConnectionService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: someone bound");
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: Someone unbound ");
        return super.onUnbind(intent);
    }

    public CommCentral createCommCentral(CommStrategy strategy) {
        if (commCentral != null) {
            // Remove existing one
            commCentral.stopDiscovery();
        }

        // Create new factory
        this.commStrategy = strategy;
        CommlibExplorerApp app = getApp();
        GenericApplianceFactory factory = app.getApplianceFactory();
        factory.setTransportContext(commStrategy.getTransportContext());

        commCentral = new CommCentral(factory, commStrategy.getTransportContext());
        return commCentral;
    }

    public CommCentral getCommCentral() {
        return commCentral;
    }

    private CommlibExplorerApp getApp() {
        return (CommlibExplorerApp) getApplication();
    }

    public CommStrategy getCommStrategy() {
        return commStrategy;
    }
}