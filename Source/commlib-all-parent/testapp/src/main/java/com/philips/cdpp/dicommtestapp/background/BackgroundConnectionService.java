package com.philips.cdpp.dicommtestapp.background;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdpp.dicommtestapp.DiCommTestApp;
import com.philips.cdpp.dicommtestapp.appliance.GenericAppliance;
import com.philips.cdpp.dicommtestapp.appliance.GenericApplianceFactory;
import com.philips.cdpp.dicommtestapp.strategy.CommStrategy;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundConnectionService extends Service
{
    private static final String TAG = "BackgroundConnectionSer";
    private CommCentral mCommCentral;
    private CommStrategy mCommStrategy;

    public class BackgroundBinder extends Binder {

        public BackgroundConnectionService getService(){
            return BackgroundConnectionService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: someone bound");
        return mBinder;
    }

    private final IBinder mBinder = new BackgroundBinder();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: Someone unbound ");
        return super.onUnbind(intent);
    }

    public CommCentral createCommCentral(CommStrategy strategy)
    {
        if(mCommCentral != null) {
            // Remove existing one
            mCommCentral.stopDiscovery();
            mCommCentral = null;
        }

        // Create new factory
        this.mCommStrategy = strategy;
        DiCommTestApp app = getApp();
        GenericApplianceFactory factory = app.getApplianceFactory();
        factory.setTransportContext(mCommStrategy.getTransportContext());

        mCommCentral = new CommCentral(factory, mCommStrategy.getTransportContext());
        return mCommCentral;
    }

    public CommCentral getCommCentral() {
        return mCommCentral;
    }

    private DiCommTestApp getApp() {
        return (DiCommTestApp) getApplication();
    }

    public CommStrategy getCommStrategy() {
        return mCommStrategy;
    }
}