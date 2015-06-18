package com.philips.pins.shinelib.services.healththermometer;

import android.util.Log;

import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.capabilities.SHNCapabilityLogSynchronization;
import com.philips.pins.shinelib.datatypes.SHNData;
import com.philips.pins.shinelib.datatypes.SHNDataType;
import com.philips.pins.shinelib.datatypes.SHNLog;
import com.philips.pins.shinelib.datatypes.SHNLogItem;
import com.philips.pins.shinelib.framework.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 310188215 on 17/06/15.
 */
public class SHNCapabilityHealthThermometerLogSync implements SHNCapabilityLogSynchronization, SHNServiceHealthThermometer.SHNServiceHealthThermometerListener {

    private static final String TAG = SHNCapabilityHealthThermometerLogSync.class.getSimpleName();
    private static final int MAX_STORED_MEASUREMENTS = 50;

    private final SHNServiceHealthThermometer shnServiceHealthThermometer;

    private List<SHNData> shnTemperatureMeasurements;
    private State state;
    private Listener shnCapabilityListener;
    private Timer timer;

    private final Runnable timeOutRunnable = new Runnable() {
        @Override
        public void run() {
            handleTimeOut();
        }
    };

    public SHNCapabilityHealthThermometerLogSync(SHNServiceHealthThermometer shnServiceHealthThermometer) {
        this.state = State.Idle;
        this.shnServiceHealthThermometer = shnServiceHealthThermometer;
        shnServiceHealthThermometer.setSHNServiceHealthThermometerListener(this);
        timer = Timer.createTimer(timeOutRunnable, 5000L);
    }

    // implements SHNCapabilityLogSynchronization
    @Override
    public void setListener(Listener listener) {
        shnCapabilityListener = listener;
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
            shnServiceHealthThermometer.setReceiveTemperatureMeasurements(true, new SHNResultListener() {
                @Override
                public void onActionCompleted(SHNResult result) {
                    if (result == SHNResult.SHNOk) {
                        setState(State.Synchronizing);
                    } else {
                        finishLoggingResult(result);
                        setState(State.Idle);
                    }
                }
            });
            if (shnCapabilityListener != null) shnCapabilityListener.onProgressUpdate(this, 0.0f);
            timer.restart();
        }
    }

    @Override
    public void abortSynchronization() {
        if (state == State.Synchronizing) {
            shnServiceHealthThermometer.setReceiveTemperatureMeasurements(false, null);
            timer.stop();
            finishLoggingResult(SHNResult.SHNAborted);
            setState(State.Idle);
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

    // implements SHNServiceHealthThermometer.SHNServiceHealthThermometerListener
    @Override
    public void onTemperatureMeasurementReceived(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNTemperatureMeasurement shnTemperatureMeasurement) {
        if (state == State.Synchronizing) {
            timer.restart();
            if (shnTemperatureMeasurement.getTimestamp() == null) {
                Log.w(TAG, "The received temperature measurement does not have a timestamp, cannot save it in the log!");
            } else {

                if (shnTemperatureMeasurements == null) {
                    shnTemperatureMeasurements = new ArrayList<>();
                }
                shnTemperatureMeasurements.add(shnTemperatureMeasurement);

                int count = shnTemperatureMeasurements.size();
                float progress = Math.min((float) count / MAX_STORED_MEASUREMENTS, 1.0f);
                if (shnCapabilityListener != null)
                    shnCapabilityListener.onProgressUpdate(this, progress);
            }
        } else {
            Log.w(TAG, "Received measurement but is in an inconsistent state!");
        }
    }

    @Override
    public void onServiceStateChanged(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNService.State state) {
        if (state == SHNService.State.Unavailable || state == SHNService.State.Error) {
            abortSynchronization();
        }
    }

    @Override
    public void onIntermediateTemperatureReceived(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNTemperatureMeasurement shnTemperatureMeasurement) {

    }

    @Override
    public void onMeasurementIntervalChanged(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNTemperatureMeasurementInterval shnTemperatureMeasurementInterval) {

    }

    private void setState(State state) {
        if (this.state != state) {
            this.state = state;

            if (shnCapabilityListener != null) shnCapabilityListener.onStateUpdated(this);
        }
    }

    private void finishLoggingResult(SHNResult result) {
        assert (state == State.Synchronizing);
        if (shnCapabilityListener != null) shnCapabilityListener.onProgressUpdate(this, 1.0f);
        if (shnTemperatureMeasurements != null && shnTemperatureMeasurements.size() > 0) {

            Collections.sort(shnTemperatureMeasurements, new SHNTemperatureMeasurementComparator());

            List<SHNLogItem> logItems = new ArrayList<>();
            Set<SHNDataType> types = new HashSet<>();
            types.add(SHNDataType.BodyTemperature);

            for (SHNData data : shnTemperatureMeasurements) {
                if (data.getSHNDataType() == SHNDataType.BodyTemperature) {
                    SHNTemperatureMeasurement meas = (SHNTemperatureMeasurement) data;

                    Map<SHNDataType, SHNData> map = new HashMap<>();
                    map.put(SHNDataType.BodyTemperature, meas);
                    SHNLogItem item = new SHNLogItem(meas.getTimestamp(), map.keySet(), map);
                    logItems.add(item);
                }
            }

            Date startDate = logItems.get(0).getTimestamp();
            Date endDate = logItems.get(logItems.size() - 1).getTimestamp();
            SHNLog log = new SHNLog(startDate, endDate, "", logItems, types);
            if (shnCapabilityListener != null)
                shnCapabilityListener.onLogSynchronized(this, log, result);
        } else {
            if (shnCapabilityListener != null)
                shnCapabilityListener.onLogSynchronizationFailed(this, SHNResult.SHNResponseIncompleteError);
        }
    }

    private void handleTimeOut() {
        finishLoggingResult(SHNResult.SHNOk);
        setState(State.Idle);
    }

    private class SHNTemperatureMeasurementComparator implements Comparator<SHNData> {
        @Override
        public int compare(SHNData o1, SHNData o2) {
            if (o1.getSHNDataType() == SHNDataType.BodyTemperature && o2.getSHNDataType() == SHNDataType.BodyTemperature) {
                SHNTemperatureMeasurement meas1 = (SHNTemperatureMeasurement) o1;
                SHNTemperatureMeasurement meas2 = (SHNTemperatureMeasurement) o2;
                return meas1.getTimestamp().compareTo(meas2.getTimestamp());
            }
            return 0;
        }
    }
}
