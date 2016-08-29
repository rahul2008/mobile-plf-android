package com.example.cdpp.bluelibreferenceapp;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.utility.SHNLogger;

public class ReferenceApplication extends Application {

    private static final String TAG = "ReferenceApplication";

    private static ReferenceApplication sApplication;

    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private SHNCentral mShnCentral;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;

        // Setup logger
        SHNLogger.registerLogger(new SHNLogger.LogCatLogger());

        // BlueLib handler thread
        mHandlerThread = new HandlerThread("BlueLibThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        // Obtain BlueLib instance
        SHNCentral.Builder builder = new SHNCentral.Builder(this);
        builder.showPopupIfBLEIsTurnedOff(true);
        builder.setHandler(mHandler);

        try {
            mShnCentral = builder.create();
        } catch (SHNBluetoothHardwareUnavailableException e) {
            Log.e(TAG, "Error obtaining BlueLib instance: " + e.getMessage());

            mHandlerThread.quitSafely();
            mHandler = null;
        }
    }

    public static final ReferenceApplication get() {
        if (sApplication == null) {
            throw new RuntimeException("Application not initialized yet.");
        }
        return sApplication;
    }

    public final
    @NonNull
    SHNCentral getShnCentral() {
        return mShnCentral;
    }
}
