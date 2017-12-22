/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.timesync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.SntpClient;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.R;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The UTC time Sync Class.
 * This provides API's to retrieve and refresh the server time .
 */
public class TimeSyncSntpClient implements TimeInterface {

    private static final long serialVersionUID = -5777846591881357200L;
    public static final String DATE_FORMAT = "yyyy-MM-dd'T' K mm:ss.SSS Z";
    public static final String UTC = "UTC";
    private static final String OFFSET = "offset";
    private static final String SERVERTIME_PREFERENCE = "timeSync";
    private static final String OFFLINE_REFRESH_PREFERENCE = "offline_refresh_timeSync";
    private static final int MAX_SERVER_TIMEOUT_IN_MSEC = 30000;
    private static final int REFRESH_INTERVAL_IN_HOURS = 24;
    private static final int FAILED_REFRESH_DELAY_IN_MINUTES = 5;
    private static String[] serverPool;
    private final ReentrantLock mRefreshInProgressLock;
    private AppInfra mAppInfra;
    private transient SharedPreferences mSharedPreferences;
    private long mOffset;
    private Calendar mNextRefreshTime;
    private boolean isSynchronized = false;
    final transient Handler responseHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            isSynchronized = (boolean) msg.obj;
        }
    };


    public TimeSyncSntpClient(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        mRefreshInProgressLock = new ReentrantLock();
        init();
        registerReciever();
    }

    private void refreshIfNeeded() {
        final Calendar now = Calendar.getInstance();
        if (!mRefreshInProgressLock.isLocked() && now.after(mNextRefreshTime) && getOffset()==0L) {
            refreshTime();
        }
    }

    private synchronized void init() {
        mSharedPreferences = mAppInfra.getAppInfraContext().getSharedPreferences(SERVERTIME_PREFERENCE, Context.MODE_PRIVATE);
        // mOffset = getOffset();
        mNextRefreshTime = Calendar.getInstance();
    }

    private String[] getServerPool() {
        ArrayList<String> timeSyncServerList = getTimeSyncServerPoolFromConfig();
        if (null != timeSyncServerList && !timeSyncServerList.isEmpty()) {
            serverPool = timeSyncServerList.toArray(new String[timeSyncServerList.size()]);
        } else {
            serverPool = mAppInfra.getAppInfraContext().getResources().getStringArray(R.array.server_pool);
        }


        if (serverPool == null || serverPool.length == 0) {
            throw new IllegalArgumentException("NTP server pool string array asset missing");
        }

        return serverPool;
    }

    private void saveOffset(final long pOffset) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(OFFSET, pOffset);
        editor.apply();
        //Log.i(AppInfraLogEventID.AI_TIME_SYNC, "Successfully saved Offset");
    }

    private long getOffset() {
        return mSharedPreferences.getLong(OFFSET, 0L);
    }

    private void refreshOffset() {
        final boolean lockAcquired = mRefreshInProgressLock.tryLock();
        if (lockAcquired) {
            Message msg = new Message();
            msg.obj = false;
            isSynchronized=false;
            responseHandler.sendMessage(msg);

            boolean offsetUpdated = false;
            long offsetOfLowestRoundTrip = 0;
            long lowestRoundTripDelay = Long.MAX_VALUE;
            long[] offSets;
            long[] roundTripDelays;


            if (serverPool == null || serverPool.length == 0) {
                serverPool = getServerPool();
            }

            offSets = new long[serverPool.length];
            roundTripDelays = new long[serverPool.length];
            SntpClient sntpClient = new SntpClient();

            for (int i = 0; i < serverPool.length; i++) {

                if (sntpClient.requestTime(serverPool[i], MAX_SERVER_TIMEOUT_IN_MSEC)) {
                    long deviceTime = System.currentTimeMillis();
                    offSets[i] = sntpClient.getNtpTime() - deviceTime;
                    roundTripDelays[i] = sntpClient.getRoundTripTime();
                } else {
                    roundTripDelays[i] = Long.MAX_VALUE;
                }
                if (roundTripDelays[i] < lowestRoundTripDelay) {
                    lowestRoundTripDelay = roundTripDelays[i];
                    offsetOfLowestRoundTrip = offSets[i];
                    offsetUpdated = true;
                }
            }


            if (offsetUpdated) {
                Message flagMsg = new Message();
                flagMsg.obj = true;
                isSynchronized=true;
                responseHandler.sendMessage(flagMsg);
                mNextRefreshTime.add(Calendar.HOUR, REFRESH_INTERVAL_IN_HOURS);
                mOffset = offsetOfLowestRoundTrip;
                saveOffset(mOffset);
            } else {
                mNextRefreshTime.add(Calendar.MINUTE, FAILED_REFRESH_DELAY_IN_MINUTES);
            }
            mRefreshInProgressLock.unlock();
        } else {
            // another refresh is already in progress
            Message msg = new Message();
            msg.obj = false;
            isSynchronized=false;
            responseHandler.sendMessage(msg);
            throw new IllegalArgumentException("TimeSync--another refresh is already in progress");

        }
    }

    /**
     * Method to fetch the TimeSync ServerPool from the config.
     *
     * @return Arraylist list of TimeSync ServerPool.
     */
    private ArrayList<String> getTimeSyncServerPoolFromConfig() {
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                .AppConfigurationError();
        if (mAppInfra.getConfigInterface() != null) {
            try {
                Object mServerPool = mAppInfra.getConfigInterface().getPropertyForKey
                        ("timesync.ntp.hosts", "appinfra", configError);
                if (mServerPool != null) {
                    if (mServerPool instanceof ArrayList<?>) {
                        ArrayList<String> mServerPoolList = new ArrayList<>();
                        ArrayList<?> list = (ArrayList<?>) mServerPool;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i) instanceof String) {
                                mServerPoolList.add((String) list.get(i));
                            } else {
                                throw new IllegalArgumentException("Server Pool should be array of strings" +
                                        " in AppConfig.json file");
                            }
                        }
                        return mServerPoolList;
                    } else {
                        throw new IllegalArgumentException("Server Pool should be array of strings" +
                                " in AppConfig.json file");
                    }
                }
            } catch (IllegalArgumentException exception) {
                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_TIME_SYNC,"illegal argument when getting T-sync config pool"+exception.getMessage());
            }
        }
        return null;
    }


    @Override
    public Date getUTCTime() {
        Date date;
        try {
            refreshIfNeeded();
            date = new Date(getOffset() + System.currentTimeMillis());
            return date;
        } catch (Exception e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_TIME_SYNC, "T-Error get U-time"+e.getMessage());
        }
        return null;
    }

    @Override
    public void refreshTime() {

        if (null != mAppInfra.getRestClient() && mAppInfra.getRestClient().isInternetReachable()) {
            if (!mRefreshInProgressLock.isLocked()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            refreshOffset();
                        } catch (IllegalArgumentException e) {
                            if (mAppInfra != null && mAppInfra.getAppInfraLogInstance() != null)
                                mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_TIME_SYNC, "T-Error refresh time"+e.getMessage());
                        }
                    }
                }).start();
            }
        } else {
          //  Log.e(AppInfraLogEventID.AI_TIME_SYNC, "Network connectivity not found");
            isSynchronized = false;
        }
    }


    @Override
    public boolean isSynchronized() {
        return isSynchronized;
    }

    private void registerReciever() {
        final DateTimeChangedReceiver receiver = new DateTimeChangedReceiver();
        final IntentFilter registeReceiver = new IntentFilter();
        registeReceiver.addAction("android.intent.action.DATE_CHANGED");
        registeReceiver.addAction("android.intent.action.TIME_SET");
        registeReceiver.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registeReceiver.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registeReceiver.addAction("android.net.wifi.STATE_CHANGE");
        mAppInfra.getAppInfraContext().registerReceiver(receiver, registeReceiver);
    }

    public class DateTimeChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (null != mAppInfra.getRestClient() && !mAppInfra.getRestClient().isInternetReachable()) {
                //Log.e(AppInfraLogEventID.AI_TIME_SYNC, "Network connectivity not found");
                isSynchronized = false;
            } else {
                refreshTime();
            }

        }
    }

}
