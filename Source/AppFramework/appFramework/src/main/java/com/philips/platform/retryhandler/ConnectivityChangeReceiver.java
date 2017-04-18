package com.philips.platform.retryhandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.philips.platform.baseapp.screens.utility.BaseAppUtil;


/**
 * Created by philips on 18/04/17.
 */

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    private static final String TAG=ConnectivityChangeReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"Network changed occur");
        if(BaseAppUtil.isNetworkAvailable(context)){
            Log.d(TAG,"Network available");
            Bundle extras = intent.getExtras();
            if (extras != null) {
                for (String key: extras.keySet()) {
                    Log.d(TAG, "key [" + key + "]: " +
                            extras.get(key));
                }
            }

        }

    }
}
