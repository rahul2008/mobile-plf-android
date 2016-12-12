package cdp.philips.com.mydemoapp.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.philips.platform.core.trackers.DataServicesManager;

import org.joda.time.DateTimeConstants;

import javax.inject.Inject;

import cdp.philips.com.mydemoapp.utility.Utility;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BaseAppBroadcastReceiver extends BroadcastReceiver {

    public static final long DATA_FETCH_FREQUENCY = 15 * DateTimeConstants.MILLIS_PER_SECOND;

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
            mDataServices.synchchronize();
        }
    }

}