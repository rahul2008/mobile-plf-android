/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.timesync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.R;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by 310243577 on 6/27/2016.
 * * This provides API's to retrieve and refresh the server time .
 */
public class TimeSyncSntpClient extends BroadcastReceiver  implements TimeInterface {


    private static final String TAG = "TimeSyncSntpClient";

    // Number of seconds between Jan 1, 1900 and Jan 1, 1970
    // 70 years plus 17 leap days
    private static final long OFFSET_BETWEEN_1900_TO_1970 = ((365L * 70L) + 17L) * 24L * 60L * 60L;

    // system time computed from NTP server response
    private static long mNtpTime = 0L;

    // value of SystemClock.elapsedRealtime() corresponding to mNtpTime
    private long mNtpTimeReference;

    // round trip time in milliseconds
    private long mRoundTripTime;

    private Context mContext;

    private static SharedPreferences mSharedPreferences;

    private static final String SERVERTIME_PREFERENCE = "timeSync";

    private static String[] serverPool;

    private boolean isRefreshInProgress;


    public TimeSyncSntpClient(AppInfra aAppInfra) {
        mContext = aAppInfra.getAppInfraContext();
        serverPool = mContext.getResources().getStringArray(R.array.server_pool);
        init();
        refreshTime();
        syncWithDayandDateSettingChange();
    }

    public TimeSyncSntpClient() {
    }


    public synchronized void init() {
        mSharedPreferences = mContext.getSharedPreferences(SERVERTIME_PREFERENCE, Context.MODE_PRIVATE);
    }

    private void saveElapsedOffset(final long offSetMilliseconds) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(TimeConstants.OFFSET_ELAPSED, offSetMilliseconds);
        editor.apply();
    }

    private void saveOffset(final long pOffset) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(TimeConstants.OFFSET, pOffset);
        editor.apply();
    }

    private long getOffset() {
        return mSharedPreferences.getLong(TimeConstants.OFFSET, 0L);
    }

    private long getElapsedOffset() {
        return mSharedPreferences.getLong(TimeConstants.OFFSET_ELAPSED, 0L);
    }

    public synchronized void refreshOffset() {
        long ntpTime;
        final long elapsedTime = SystemClock.elapsedRealtime();
        final long currentTime = System.currentTimeMillis();
        isRefreshInProgress = true;
        long nowAsPerDeviceTimeZone = 0;
        for (int i = 0; i < TimeConstants.POOL_SIZE; i++) {
            if (this.requestTime(serverPool[i], 30000)) {
                nowAsPerDeviceTimeZone = getNtpTime();
                break;
            }
        }
        if (nowAsPerDeviceTimeZone != 0L) {
            ntpTime = nowAsPerDeviceTimeZone;
            final long deviceTime = System.currentTimeMillis();
            final long mOffset = ntpTime - deviceTime;
            saveOffset(mOffset);
        }
        saveElapsedOffset(currentTime - elapsedTime);
        isRefreshInProgress = false;
    }

    public String getCurrentTime() {
        final SimpleDateFormat sdf = new SimpleDateFormat(TimeConstants.DATE_FORMAT, Locale.ENGLISH);
        final Date date = new Date(getOffset() + System.currentTimeMillis() + getCurrentTimeZoneDiff());
        sdf.setTimeZone(TimeZone.getTimeZone(TimeConstants.UTC));

        final String curruntTime = sdf.format(date);
        Log.i("CurrentTime", "" + curruntTime);
        return curruntTime;
    }


    /**
     * Method to synchronize time for every 24 hrs.
     */
    public void syncWithDayandDateSettingChange() {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);

        final Intent intent = new Intent(mContext, TimeSyncSntpClient.class);
        final PendingIntent pendingIntent =
                PendingIntent.getBroadcast(mContext,
                        0, intent, 0);
        final AlarmManager alarmManager =
                (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                cal.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    /**
     * Returns the round trip time of the NTP transaction
     *
     * @return round trip time in milliseconds.
     */
//    public long getRoundTripTime() {
//        return mRoundTripTime;
//    }

    /**
     * To get the timezone difference from the current time.
     *
     * @return timezone difference
     */
    private long getCurrentTimeZoneDiff() {
        final TimeZone timeZoneInDevice = TimeZone.getDefault();
        return timeZoneInDevice.getOffset(System.currentTimeMillis());
    }


    /**
     * Sends an SNTP request to the given host and processes the response.
     *
     * @param host    host name of the server.
     * @param timeout network timeout in milliseconds.
     * @return true if the transaction was successful.
     */
    private boolean requestTime(String host, int timeout) {
        final int ORIGINTIME_OFFSET = 24;
        final int RECEIVE_OFFSET = 32;
        final int TRANSMIT_TIME_OFFSET = 40;
        final int NTP_PACKET_SIZE = 48;

        final int NTP_PORT = 123;
        final int NTP_CLIENT = 3;
        final int NTP_VERSION = 3;

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(timeout);
            final InetAddress address = InetAddress.getByName(host);
            byte[] buffer = new byte[NTP_PACKET_SIZE];
            final DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, NTP_PORT);

            // set mode = 3 (client) and version = 3
            // mode is in low 3 bits of first byte
            // version is in bits 3-5 of first byte
            buffer[0] = NTP_CLIENT | (NTP_VERSION << 3);

            // get current time and write it to the request packet3
            final long requestTime = System.currentTimeMillis();
            final long requestTicks = SystemClock.elapsedRealtime();
            writeTimeStamp(buffer, TRANSMIT_TIME_OFFSET, requestTime);

            socket.send(request);

            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);
            final long responseTicks = SystemClock.elapsedRealtime();
            final long responseTime = requestTime + (responseTicks - requestTicks);

            final long originateTime = readTimeStamp(buffer, ORIGINTIME_OFFSET);
            final long receiveTime = readTimeStamp(buffer, RECEIVE_OFFSET);
            final long transmitTime = readTimeStamp(buffer, TRANSMIT_TIME_OFFSET);
            final long roundTripTime = responseTicks - requestTicks - (transmitTime - receiveTime);
            final long clockOffset = ((receiveTime - originateTime) + (transmitTime - responseTime)) / 2;
            mNtpTime = responseTime + clockOffset;

            mNtpTimeReference = responseTicks;
            mRoundTripTime = roundTripTime;
        } catch (Exception e) {
            Log.i("Exception", "" + e);
            Log.d(TAG, "request time failed: " + e);
            return false;
        } finally {
            if (socket != null) {
                socket.close();
            }
        }

        return true;
    }

    public synchronized String getCurrentUTCTimeWithFormat() {
        long diffElapsedOffset = getCurrentElapsedDifference() - getElapsedOffset();
        final SimpleDateFormat sdf = new SimpleDateFormat(TimeConstants.DATE_FORMAT, Locale.ENGLISH);
        Date date ;
        if (isRefreshInProgress) {
            date = new Date(getOffset() + diffElapsedOffset + System.currentTimeMillis());
        } else {
            date = new Date(getOffset() + System.currentTimeMillis());
        }
        sdf.setTimeZone(TimeZone.getTimeZone(TimeConstants.UTC));
        final String utcTime = sdf.format(date);
        Log.i("CurrentUTCTime", "" + utcTime);
        return utcTime;
    }

    /**
     * Returns the reference clock value (value of SystemClock.elapsedRealtime())
     * corresponding to the NTP time.
     *
     * @return reference clock corresponding to the NTP time.
     */
//    private long getNtpTimeReference() {
//        return mNtpTimeReference;
//    }

    /**
     * Returns the time computed from the NTP transaction.
     *
     * @return time value computed from NTP server response.
     */
    private long getNtpTime() {
        return mNtpTime;
    }


    /**
     * Reads an unsigned 32 bit big endian number from the given offset in the buffer.
     */
    private long read32(byte[] buffer, int offset) {
        final byte b0 = buffer[offset];
        final byte b1 = buffer[offset + 1];
        final byte b2 = buffer[offset + 2];
        final byte b3 = buffer[offset + 3];

        // convert signed bytes to unsigned values
        final int i0 = ((b0 & 0x80) == 0x80 ? (b0 & 0x7F) + 0x80 : b0);
        final int i1 = ((b1 & 0x80) == 0x80 ? (b1 & 0x7F) + 0x80 : b1);
        final int i2 = ((b2 & 0x80) == 0x80 ? (b2 & 0x7F) + 0x80 : b2);
        final int i3 = ((b3 & 0x80) == 0x80 ? (b3 & 0x7F) + 0x80 : b3);

        return ((long) i0 << 24) + ((long) i1 << 16) + ((long) i2 << 8) + (long) i3;
    }

    /**
     * Reads the NTP time stamp at the given offset in the buffer and returns
     * it as a system time (milliseconds since January 1, 1970).
     */
    private long readTimeStamp(byte[] buffer, int offset) {
        final long seconds = read32(buffer, offset);
        final long fraction = read32(buffer, offset + 4);
        return ((seconds - OFFSET_BETWEEN_1900_TO_1970) * 1000) + ((fraction * 1000L) / 0x100000000L);
    }

    /**
     * Writes system time (milliseconds since January 1, 1970) as an NTP time stamp
     * at the given offset in the buffer.
     */
    private void writeTimeStamp(byte[] buffer, int offset, long time) {
        long seconds = time / 1000L;
        long milliseconds = time - seconds * 1000L;
        seconds += OFFSET_BETWEEN_1900_TO_1970;

        // write seconds in big endian format
        buffer[offset++] = (byte) (seconds >> 24);
        buffer[offset++] = (byte) (seconds >> 16);
        buffer[offset++] = (byte) (seconds >> 8);
        buffer[offset++] = (byte) (seconds >> 0);

        final long fraction = milliseconds * 0x100000000L / 1000L;
        // write fraction in big endian format
        buffer[offset++] = (byte) (fraction >> 24);
        buffer[offset++] = (byte) (fraction >> 16);
        buffer[offset++] = (byte) (fraction >> 8);
        // low order bits should be random data
        buffer[offset++] = (byte) (Math.random() * 255.0);
    }

    private long getCurrentElapsedDifference() {

        return System.currentTimeMillis() - SystemClock.elapsedRealtime();
    }


    @Override
    public Date getUTCTime() {
        Date ntpDate = null;
        System.out.println("NTPTIME"+" "+mNtpTime);
        DateFormat date_format = new SimpleDateFormat(TimeConstants.DATE_FORMAT, Locale.ENGLISH);
        try {
            if (mNtpTime == 0L) {
                date_format = new SimpleDateFormat(TimeConstants.DATE_FORMAT, Locale.ENGLISH);
                final Date utcDate = new Date(mNtpTime);
                date_format.format(utcDate);
                Log.i("DATE_FORMAT", "" + date_format.format(utcDate));
              //  refreshTime();
                ntpDate = date_format.parse(getCurrentUTCTimeWithFormat());
            } else {
                ntpDate = date_format.parse(getCurrentUTCTimeWithFormat());
            }
        } catch (Exception e) {
            Log.i("Error", "" + e);
        }

        return ntpDate;
    }

    @Override
    public void refreshTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mNtpTime == 0L) {
                    refreshOffset();
                }
            }
        }).start();
    }

    @Override
    public Date getServerTime() {
        Date ntpDate = null;
        DateFormat date_format = new SimpleDateFormat(TimeConstants.DATE_FORMAT, Locale.ENGLISH);
        try {
            if (mNtpTime == 0L) {
                date_format = new SimpleDateFormat(TimeConstants.DATE_FORMAT, Locale.ENGLISH);
                final Date utcDate = new Date(mNtpTime);
                date_format.format(utcDate);
                Log.i("DATE_FORMAT", "" + date_format.format(utcDate));
             //   refreshTime();
                ntpDate = date_format.parse(getCurrentTime());
            } else {
                ntpDate = date_format.parse(getCurrentTime());
            }
        } catch (Exception e) {
            Log.i("Error", "" + e);
        }
        return ntpDate;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mNtpTime = 0L;
        if (AppInfraSingleton.getInstance() != null) {
            refreshTime();
        }
    }
}
