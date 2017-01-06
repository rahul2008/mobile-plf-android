package com.philips.platform.baseapp.screens.dataservices.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.philips.platform.baseapp.screens.dataservices.DataServicesState;
import com.philips.platform.baseapp.screens.dataservices.utility.Utility;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import org.joda.time.DateTimeConstants;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BaseAppBroadcastReceiver extends BroadcastReceiver {

    public static final long DATA_FETCH_FREQUENCY = 30 * DateTimeConstants.MILLIS_PER_SECOND;

    public static final String ACTION_USER_DATA_FETCH = "ACTION_USER_DATA_FETCH";

    DataServicesManager mDataServices;

    Utility mUtility;

    @Inject
    public BaseAppBroadcastReceiver() {
        mUtility = new Utility();
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        mDataServices = DataServicesManager.getInstance();
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        //TODO: review changing connection
        if (mUtility.isOnline(context) && (action.equals(ACTION_USER_DATA_FETCH) || action.equals(ConnectivityManager.CONNECTIVITY_ACTION))) {
            DSLog.i(DataServicesState.TAG,"START SYNC FROM REC");
            mDataServices.synchchronize();
            
        }
    }

}