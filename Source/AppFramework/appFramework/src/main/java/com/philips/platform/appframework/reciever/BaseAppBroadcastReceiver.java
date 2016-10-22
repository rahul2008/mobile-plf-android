package com.philips.platform.appframework.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.core.trackers.Tracker;

import org.joda.time.DateTimeConstants;

import javax.inject.Inject;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BaseAppBroadcastReceiver extends BroadcastReceiver {

    public static final long DATA_FETCH_FREQUENCY = 30 * DateTimeConstants.MILLIS_PER_SECOND;

    public static final String ACTION_USER_DATA_FETCH = "ACTION_USER_DATA_FETCH";

    @Inject
    Tracker mTracker;

    @Inject
    public BaseAppBroadcastReceiver() {

    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        ((AppFrameworkApplication) context.getApplicationContext()).getAppComponent().injectReciever(this);
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        //TODO: review changing connection
        if (isOnline(context) && (action.equals(ACTION_USER_DATA_FETCH) || action.equals(ConnectivityManager.CONNECTIVITY_ACTION))) {
            mTracker.syncData();
        }
    }

    private boolean isOnline(Context context) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
        } catch (Exception ignored) {
            return false;
        }
    }
}