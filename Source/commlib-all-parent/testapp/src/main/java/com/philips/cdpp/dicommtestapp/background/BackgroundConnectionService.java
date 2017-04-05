package com.philips.cdpp.dicommtestapp.background;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdpp.dicommtestapp.DiCommTestApp;
import com.philips.cdpp.dicommtestapp.appliance.GenericApplianceFactory;
import com.philips.cdpp.dicommtestapp.strategy.CommStrategy;

public class BackgroundConnectionService extends Service {
    private static final String TAG = "BgConnectionService";
    private CommCentral commCentral;
    private CommStrategy commStrategy;
    private final IBinder binder = new BackgroundBinder();

    public class BackgroundBinder extends Binder {
        public BackgroundConnectionService getService(){
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
        if(commCentral != null) {
            // Remove existing one
            commCentral.stopDiscovery();
            commCentral = null;
        }

        // Create new factory
        this.commStrategy = strategy;
        DiCommTestApp app = getApp();
        GenericApplianceFactory factory = app.getApplianceFactory();
        factory.setTransportContext(commStrategy.getTransportContext());

        commCentral = new CommCentral(factory, commStrategy.getTransportContext());
        return commCentral;
    }

    public CommCentral getCommCentral() {
        return commCentral;
    }

    private DiCommTestApp getApp() {
        return (DiCommTestApp) getApplication();
    }

    public CommStrategy getCommStrategy() {
        return commStrategy;
    }
}