package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.datatypes.SHNDataType;
import com.philips.pins.shinelib.datatypes.SHNLog;
import com.philips.pins.shinelib.datatypes.SHNLogItem;
import com.philips.pins.shinelib.framework.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class SHNCapabilityLogSyncBase implements SHNCapabilityLogSynchronization { // rename it

    protected List<SHNResult> allowedResults;
    private SHNResult result;

    private static final String TAG = SHNCapabilityLogSyncBase.class.getSimpleName();
    private static final int MAX_STORED_MEASUREMENTS = 50;

    private List<SHNLogItem> shnLogItems;
    private State state;
    protected SHNCapabilityLogSynchronizationListener shnCapabilityLogSynchronizationListener;

    protected Timer timer;

    private final Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            handleTimeout();
        }
    };

    public SHNCapabilityLogSyncBase() {
        this.state = State.Idle;
        timer = Timer.createTimer(timeoutRunnable, 5000L);

        allowedResults = new ArrayList<>();
        allowedResults.add(SHNResult.SHNOk);
    }

    // implements SHNCapabilityLogSynchronization
    @Override
    public void setSHNCapabilityLogSynchronizationListener(SHNCapabilityLogSynchronizationListener SHNCapabilityLogSynchronizationListener) {
        shnCapabilityLogSynchronizationListener = SHNCapabilityLogSynchronizationListener;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public Object getLastSynchronizationToken() {
        return new Object();
    }

    @Override
    public void startSynchronizationFromToken(Object synchronizationToken) {
        if (state == State.Idle) {
            setState(State.Synchronizing);
            timer.restart();
            notifyListenerWithProgress(0.0f);
            setupToReceiveMeasurements();
        } else {
            Log.w(TAG, "Unable to start synchronization; Already running!");
        }
    }

    @Override
    public void abortSynchronization() {
        if (state == State.Synchronizing) {
            stop(SHNResult.SHNAborted);
        } else {
            Log.w(TAG, "Unable to abort synchronization, already in an idle state!");
        }
    }

    @Override
    public void getValueForOption(Option option, SHNIntegerResultListener shnResultListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValueForOption(int value, Option option, SHNResultListener shnResultListener) {
        throw new UnsupportedOperationException();
    }

    protected void setState(State state) {
        if (this.state != state) {
            this.state = state;

            if (shnCapabilityLogSynchronizationListener != null) {
                shnCapabilityLogSynchronizationListener.onStateUpdated(this);
            }
        }
    }

    protected void handleResultOfMeasurementsSetup(SHNResult result) {
        if (!allowedResults.contains(result)) {
            stop(result);
        } else {
            this.result = result;
        }
    }

    protected void onMeasurementReceived(SHNLogItem shnLogItem) {
        if (state == State.Synchronizing) {
            timer.restart();
            if (shnLogItems == null) {
                shnLogItems = new ArrayList<>();
            }
            shnLogItems.add(shnLogItem);

            int count = shnLogItems.size();
            float progress = Math.min((float) count / MAX_STORED_MEASUREMENTS, 1.0f);
            notifyListenerWithProgress(progress);
        } else {
            Log.w(TAG, "Received measurement but is in an inconsistent state!");
        }
    }

    protected void stop(SHNResult result) {
        finishLoggingResult(result);
        setState(State.Idle);
        timer.stop();
    }

    private void finishLoggingResult(SHNResult result) {
        assert (state == State.Synchronizing);
        teardownReceivingMeasurements();
        notifyListenerWithProgress(1.0f);

        if (!allowedResults.contains(result) && shnLogItems == null) {
            if (shnCapabilityLogSynchronizationListener != null) {
                shnCapabilityLogSynchronizationListener.onLogSynchronizationFailed(this, result);
            }
        } else {
            SHNLog log = createLog();
            if (shnCapabilityLogSynchronizationListener != null) {
                shnCapabilityLogSynchronizationListener.onLogSynchronized(this, log, result);
            }
        }
        shnLogItems = null;
    }

    @NonNull
    private SHNLog createLog() {
        if (shnLogItems == null) {
            return null;
        }
        Collections.sort(shnLogItems, new SHNLogItemsComparator());

        List<SHNLogItem> logItems = new ArrayList<>();
        Set<SHNDataType> types = new HashSet<>();

        for (SHNLogItem item : shnLogItems) {
            logItems.add(item);
            types.addAll(item.getContainedDataTypes());
        }

        Date startDate = logItems.get(0).getTimestamp();
        Date endDate = logItems.get(logItems.size() - 1).getTimestamp();
        return new SHNLog(startDate, endDate, "", logItems, types);
    }

    private class SHNLogItemsComparator implements Comparator<SHNLogItem> {
        @Override
        public int compare(SHNLogItem item1, SHNLogItem item2) {
            return item1.getTimestamp().compareTo(item2.getTimestamp());
        }
    }

    private void handleTimeout() {
        assert (result != null);
        stop(result);
    }

    private void notifyListenerWithProgress(float progress) {
        if (shnCapabilityLogSynchronizationListener != null)
            shnCapabilityLogSynchronizationListener.onProgressUpdate(this, progress);
    }

    protected abstract void setupToReceiveMeasurements();

    protected abstract void teardownReceivingMeasurements();
}
