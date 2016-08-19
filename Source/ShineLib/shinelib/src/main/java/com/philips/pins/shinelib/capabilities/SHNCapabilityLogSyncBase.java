/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.BuildConfig;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.datatypes.SHNDataType;
import com.philips.pins.shinelib.datatypes.SHNLog;
import com.philips.pins.shinelib.datatypes.SHNLogItem;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.Collection;
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

    protected List<SHNResult> resultsThatDoNotCauseFailure;

    private static final String TAG = SHNCapabilityLogSyncBase.class.getSimpleName();
    private static final int MAX_STORED_MEASUREMENTS = 50;

    private List<SHNLogItem> shnLogItems;
    private State state;
    protected SHNCapabilityLogSynchronizationListener shnCapabilityLogSynchronizationListener;

    public SHNCapabilityLogSyncBase() {
        this.state = State.Idle;

        resultsThatDoNotCauseFailure = new ArrayList<>();
        resultsThatDoNotCauseFailure.add(SHNResult.SHNOk);
        resultsThatDoNotCauseFailure.add(SHNResult.SHNAborted);
    }

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
            notifyListenerWithProgress(0.0f);
            setupToReceiveMeasurements();
        } else {
            SHNLogger.w(TAG, "Unable to start synchronization; Already running!");
        }
    }

    @Override
    public void abortSynchronization() {
        if (state == State.Synchronizing) {
            stop(SHNResult.SHNAborted);
        } else {
            SHNLogger.w(TAG, "Unable to abort synchronization, already in an idle state!");
        }
    }

    @Override
    public void getValueForOption(Option option, SHNIntegerResultListener shnResultListener) {
        shnResultListener.onActionCompleted(0, SHNResult.SHNErrorUnsupportedOperation);
    }

    @Override
    public void setValueForOption(int value, Option option, SHNResultListener shnResultListener) {
        shnResultListener.onActionCompleted(SHNResult.SHNErrorUnsupportedOperation);
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
        if (SHNResult.SHNOk != result) {
            stop(result);
        }
    }

    protected void onMeasurementReceived(SHNLogItem shnLogItem) {
        if (state == State.Synchronizing) {
            if (shnLogItems == null) {
                shnLogItems = new ArrayList<>();
            }
            shnLogItems.add(shnLogItem);

            int count = shnLogItems.size();
            float progress = Math.min((float) count / MAX_STORED_MEASUREMENTS, 1.0f);
            notifyListenerWithProgress(progress);
            notifyListenerWithLogItem(shnLogItem);
        } else {
            SHNLogger.w(TAG, "Received measurement but is in an inconsistent state!");
        }
    }

    protected void stop(SHNResult result) {
        finishLoggingResult(result);
        setState(State.Idle);
    }

    private void finishLoggingResult(SHNResult result) {
        if (BuildConfig.DEBUG && state == State.Synchronizing) throw new AssertionError();

        teardownReceivingMeasurements();
        notifyListenerWithProgress(1.0f);

        if (shnCapabilityLogSynchronizationListener != null) {
            if (resultsThatDoNotCauseFailure.contains(result) || shnLogItems != null) {
                SHNLog log = createLog();
                shnCapabilityLogSynchronizationListener.onLogSynchronized(this, log, result);
            } else {
                shnCapabilityLogSynchronizationListener.onLogSynchronizationFailed(this, result);
            }
        }
        shnLogItems = null;
    }

    @NonNull
    private SHNLog createLog() {
        if (shnLogItems == null) {
            return new SHNLog(null, null, "", new ArrayList<SHNLogItem>(), new HashSet<SHNDataType>());
        }
        Collections.sort(shnLogItems, new SHNLogItemsComparator());

        return createLog(shnLogItems);
    }

    @NonNull
    private SHNLog createLog(final Collection<SHNLogItem> shnLogItems) {
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

    private void notifyListenerWithProgress(float progress) {
        if (shnCapabilityLogSynchronizationListener != null)
            shnCapabilityLogSynchronizationListener.onProgressUpdate(this, progress);
    }

    private void notifyListenerWithLogItem(final SHNLogItem logItem) {
        if (shnCapabilityLogSynchronizationListener != null) {
            Set<SHNLogItem> singleton = Collections.singleton(logItem);
            shnCapabilityLogSynchronizationListener.onIntermediateLogSynchronized(this, createLog(singleton));
        }
    }

    protected abstract void setupToReceiveMeasurements();

    protected abstract void teardownReceivingMeasurements();
}
