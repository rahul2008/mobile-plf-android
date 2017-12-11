package com.philips.platform.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.philips.platform.baseapp.screens.utility.BaseAppUtil;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.referenceapp.PushNotificationManager;


/**
 * Created by philips on 18/04/17.
 */

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    private static final String TAG=ConnectivityChangeReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        RALog.d(TAG,"Network changed occur");
        if(BaseAppUtil.isNetworkAvailable(context)){
            RALog.d(TAG,"Network available");
            PushNotificationManager.getInstance().startPushNotificationRegistration(context);
            //Synchronize database when internet is available
            DataServicesManager.getInstance().synchronize();
        }

    }
}
