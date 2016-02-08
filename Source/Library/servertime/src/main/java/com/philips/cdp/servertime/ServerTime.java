package com.philips.cdp.servertime;

/**
 * Created by 310202337 on 1/22/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.philips.cdp.servertime.constants.ServerTimeConstants;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ServerTime {

    private static final String TAG = "ServerTime";
    private static final int REFERENCE_TIME_OFFSET = 16;
    private static final int ORIGINTIME_OFFSET = 24;
    private static final int RECEIVETIME_OFFSET = 32;
    private static final int TRANSMIT_TIME_OFFSET = 40;
    private static final int NTP_PACKET_SIZE = 48;

    private static final int NTP_PORT = 123;
    private static final int NTP_CLIENT = 3;
    private static final int NTP_VERSION = 3;

    // Number of seconds between Jan 1, 1900 and Jan 1, 1970
    // 70 years plus 17 leap days
    private static final long OFFSET_BETWEEN_1900_TO_1970 = ((365L * 70L) + 17L) * 24L * 60L * 60L;

    // system time computed from NTP server response
    private static long mNtpTime;

    // value of SystemClock.elapsedRealtime() corresponding to mNtpTime
    private long mNtpTimeReference;

    // round trip time in milliseconds
    private long mRoundTripTime;

    private static Context mContext;

    private static final String SERVERTIME_PREFERENCE = "servertime";

    private static SharedPreferences mSharedPreferences;

    private static ServerTime serverTimeInstance;

    public static void init(final Context pContext){
        mContext = pContext;
        mSharedPreferences = mContext.getSharedPreferences(SERVERTIME_PREFERENCE, Context.MODE_PRIVATE);
    }
    private ServerTime(){


    }

    public static synchronized ServerTime getInstance(){

        if(serverTimeInstance == null){
            serverTimeInstance = new ServerTime();
        }

        return serverTimeInstance;
    }

    private void saveOffset(final long pOffset){

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(ServerTimeConstants.OFFSET, pOffset);
        editor.commit();


    }

    private long getOffset(){
       return  mSharedPreferences.getLong(ServerTimeConstants.OFFSET, 0L);

    }

    private long getElapsedOffset(){
        return  mSharedPreferences.getLong(ServerTimeConstants.OFFSET_ELAPSED, 0L);

    }

    private final String[] serverPool = {"0.asia.pool.ntp.org","1.asia.pool.ntp.org","2.asia.pool.ntp.org","3.asia.pool.ntp.org"};

    private boolean requestTime(String host, int timeout) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(timeout);
            InetAddress address = InetAddress.getByName(host);
            byte[] buffer = new byte[NTP_PACKET_SIZE];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, NTP_PORT);

            // set mode = 3 (client) and version = 3
            // mode is in low 3 bits of first byte
            // version is in bits 3-5 of first byte
            buffer[0] = NTP_CLIENT | (NTP_VERSION << 3);

            // get current time and write it to the request packet3
            long requestTime = System.currentTimeMillis();
            long requestTicks = SystemClock.elapsedRealtime();
            writeTimeStamp(buffer, TRANSMIT_TIME_OFFSET, requestTime);

            socket.send(request);

            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);
            long responseTicks = SystemClock.elapsedRealtime();
            long responseTime = requestTime + (responseTicks - requestTicks);

            long originateTime = readTimeStamp(buffer, ORIGINTIME_OFFSET);
            long receiveTime = readTimeStamp(buffer, RECEIVETIME_OFFSET);
            long transmitTime = readTimeStamp(buffer, TRANSMIT_TIME_OFFSET);
            long roundTripTime = responseTicks - requestTicks - (transmitTime - receiveTime);
            long clockOffset = ((receiveTime - originateTime) + (transmitTime - responseTime))/2;
            mNtpTime = responseTime + clockOffset;
            mNtpTimeReference = responseTicks;
            mRoundTripTime = roundTripTime;
        } catch (Exception e) {

            if (!false) Log.d(TAG, "request time failed: " + e);
            return false;
        } finally {
            if (socket != null) {
                socket.close();
            }
        }

        return true;
    }

    /**
     * Returns the time computed from the NTP transaction.
     *
     * @return time value computed from NTP server response.
     */
    public  long getNtpTime() {
        return mNtpTime;
    }

    private  long mOffset;
    private  long mNTPTime;



    public  String getCurrentTime(){
        final SimpleDateFormat sdf = new SimpleDateFormat(ServerTimeConstants.DATE_FORMAT);
        Date date = new Date(getOffset() + System.currentTimeMillis()+getCurrentTimeZoneDiff());
        sdf.setTimeZone(TimeZone.getTimeZone(ServerTimeConstants.UTC));

        final String utcTime = sdf.format(date);
        return utcTime;
    }

    private long getCurrentElapsedDifference(){
        return System.currentTimeMillis() - SystemClock.elapsedRealtime();
    }

    public  String getCurrentUTCTimeWithFormat(final String pFormat){
        long diffElapsedOffset = getCurrentElapsedDifference() -  getElapsedOffset() ;
        final SimpleDateFormat sdf = new SimpleDateFormat(pFormat);
        Date date = null;
        if(isRefreshInProgress) {
             date = new Date(getOffset() + diffElapsedOffset + System.currentTimeMillis());
        }else{
            date = new Date(getOffset() + System.currentTimeMillis());
        }
        sdf.setTimeZone(TimeZone.getTimeZone(ServerTimeConstants.UTC));
        final String utcTime = sdf.format(date);
        return utcTime;
    }


    private boolean isRefreshInProgress;

    private void saveElapsedOffset(final long offSetMilliseconds){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(ServerTimeConstants.OFFSET_ELAPSED, offSetMilliseconds);
        editor.commit();
    }

    public  void  refreshOffset(){
        isRefreshInProgress = true;
        long elapsedTime = SystemClock.elapsedRealtime();
        long currentTime = System.currentTimeMillis();

        long nowAsPerDeviceTimeZone = 0;
        for(int i=0 ; i < ServerTimeConstants.POOL_SIZE ; i++){
            if (this.requestTime(serverPool[i], 30000)) {
                nowAsPerDeviceTimeZone = getNtpTime();
                break;
            }
        }

        mNTPTime = nowAsPerDeviceTimeZone;
        long deviceTime = System.currentTimeMillis();
        mOffset = mNTPTime - deviceTime ;
        saveOffset(mOffset);
        saveElapsedOffset(currentTime - elapsedTime);
        isRefreshInProgress = false;

    }

    private  long getCurrentTimeZoneDiff(){
        TimeZone timeZoneInDevice = TimeZone.getDefault();
        int differentialOfTimeZones = timeZoneInDevice.getOffset(System.currentTimeMillis());
        return  differentialOfTimeZones;
    }




    /**
     * Reads an unsigned 32 bit big endian number from the given offset in the buffer.
     */
    private long read32(byte[] buffer, int offset) {
        byte b0 = buffer[offset];
        byte b1 = buffer[offset+1];
        byte b2 = buffer[offset+2];
        byte b3 = buffer[offset+3];

        // convert signed bytes to unsigned values
        int i0 = ((b0 & 0x80) == 0x80 ? (b0 & 0x7F) + 0x80 : b0);
        int i1 = ((b1 & 0x80) == 0x80 ? (b1 & 0x7F) + 0x80 : b1);
        int i2 = ((b2 & 0x80) == 0x80 ? (b2 & 0x7F) + 0x80 : b2);
        int i3 = ((b3 & 0x80) == 0x80 ? (b3 & 0x7F) + 0x80 : b3);

        return ((long)i0 << 24) + ((long)i1 << 16) + ((long)i2 << 8) + (long)i3;
    }

    /**
     * Reads the NTP time stamp at the given offset in the buffer and returns
     * it as a system time (milliseconds since January 1, 1970).
     */
    private long readTimeStamp(byte[] buffer, int offset) {
        long seconds = read32(buffer, offset);
        long fraction = read32(buffer, offset + 4);
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
        buffer[offset++] = (byte)(seconds >> 24);
        buffer[offset++] = (byte)(seconds >> 16);
        buffer[offset++] = (byte)(seconds >> 8);
        buffer[offset++] = (byte)(seconds >> 0);

        long fraction = milliseconds * 0x100000000L / 1000L;
        // write fraction in big endian format
        buffer[offset++] = (byte)(fraction >> 24);
        buffer[offset++] = (byte)(fraction >> 16);
        buffer[offset++] = (byte)(fraction >> 8);
        // low order bits should be random data
        buffer[offset++] = (byte)(Math.random() * 255.0);
    }

}
