package com.philips.platform.ths.activity;

import android.app.Application;

import com.philips.platform.ths.utility.THSNetworkStateListener;

public class THSApplication extends Application {

    private static THSApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized THSApplication getInstance() {
        if(null == mInstance) {
            mInstance = new THSApplication();
        }
        return mInstance;
    }

    public void setConnectionListener(THSNetworkStateListener.ConnectionReceiverListener listener) {
        THSNetworkStateListener.connectionReceiverListener = listener;
    }
}
