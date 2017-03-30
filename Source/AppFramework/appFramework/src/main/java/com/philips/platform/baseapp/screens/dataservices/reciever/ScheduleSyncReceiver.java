package com.philips.platform.baseapp.screens.dataservices.reciever;

import android.content.Context;

import com.philips.platform.baseapp.screens.dataservices.utility.Utility;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import org.joda.time.DateTimeConstants;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ScheduleSyncReceiver {

    public static final long DATA_FETCH_FREQUENCY = 30 * DateTimeConstants.MILLIS_PER_SECOND;

    public static final String ACTION_USER_DATA_FETCH = "ACTION_USER_DATA_FETCH";

    DataServicesManager mDataServices;

    Utility mUtility;

    @Inject
    public ScheduleSyncReceiver() {
        mUtility = new Utility();
    }

    public void onReceive(final Context context) {
        mDataServices = DataServicesManager.getInstance();

        //TODO: review changing connection
        if (mUtility.isOnline(context)) {
            DSLog.i(DSLog.LOG,"START SYNC FROM REC");
            mDataServices.synchronize();
        }
    }
}