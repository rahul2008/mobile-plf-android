package com.philips.platform.dscdemo.utility;

import android.content.Context;

import com.philips.platform.core.trackers.DataServicesManager;

import org.joda.time.DateTimeConstants;

import javax.inject.Inject;

class ScheduleSyncReceiver {
    static final long DATA_FETCH_FREQUENCY = 30 * DateTimeConstants.MILLIS_PER_SECOND;
    private Utility mUtility;

    @Inject
    public ScheduleSyncReceiver() {
        mUtility = new Utility();
    }

    void onReceive(final Context context) {
        DataServicesManager mDataServicesManager = DataServicesManager.getInstance();
        mDataServicesManager.clearExpiredMoments(null);
        if (mUtility.isOnline(context)) {
            mDataServicesManager.synchronize();
        }
    }
}