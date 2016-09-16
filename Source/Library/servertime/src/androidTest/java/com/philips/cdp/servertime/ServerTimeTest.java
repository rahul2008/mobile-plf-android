/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.servertime;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.recievers.DateTimeChangedReceiver;
import com.philips.cdp.servertime.constants.ServerTimeConstants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

//import org.mockito.Mockito;


public class ServerTimeTest extends InstrumentationTestCase {
    private DateTimeChangedReceiver mReceiver;
            Context mContext;
    ServerTime serverTime;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

        mReceiver = new DateTimeChangedReceiver();
        mContext = getInstrumentation()
                .getTargetContext();
        serverTime = ServerTime.getInstance();


    }


    public void testRefreshOffset(){
        ServerTime.init(mContext);
        ServerTime.getInstance().refreshOffset();

        assertNotNull(ServerTime.getInstance().getCurrentTime());
        assertNotNull(ServerTime.getInstance().getCurrentUTCTimeWithFormat("dd-mm-yyyy"));
        mReceiver.onReceive(mContext,null);
    }



    public void testRefreshOffsetCall(){
      //  Intent intent = new Intent(Intent.ACTION_DATE_CHANGED);
       // intent.setAction(Intent.ACTION_DATE_CHANGED);

     //   getInstrumentation().getTargetContext().sendBroadcast(intent);


     //   mReceiver.onReceive(getInstrumentation().getTargetContext(), intent);
        assertNotNull(ServerTime.getInstance().getCurrentTime());
        final SimpleDateFormat sdf = new SimpleDateFormat(ServerTimeConstants.DATE_FORMAT);
        Date date = new Date(0);
        sdf.setTimeZone(TimeZone.getTimeZone(ServerTimeConstants.UTC));
        String firstJan1970 = sdf.format(date);
        assertNotSame(firstJan1970, ServerTime.getInstance().getCurrentTime());

    }

    public void testRead32(){
        byte[] buffer = new byte[20];
        int offset=10;
        Method method = null;
        String s= "";
        try {
            method = ServerTime.class.getDeclaredMethod("read32", new Class[]{byte[].class,int.class});
            method.setAccessible(true);
            method.invoke(serverTime,new Object[]{ buffer, offset});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    public void testReadTimeStamp(){
        byte[] buffer = new byte[20];
        int offset=10;
        Method method = null;
        String s= "";
        try {
            method = ServerTime.class.getDeclaredMethod("readTimeStamp", new Class[]{byte[].class,int.class});
            method.setAccessible(true);
            method.invoke(serverTime,new Object[]{ buffer, offset});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    public void testWriteTimeStamp(){
        byte[] buffer = new byte[20];
        int offset=10;
        long time=8234567893L;
        Method method = null;
        String s= "";
        try {
            method = ServerTime.class.getDeclaredMethod("writeTimeStamp", new Class[]{byte[].class,int.class,long.class});
            method.setAccessible(true);
            method.invoke(serverTime,new Object[]{ buffer, offset,time});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void testRequestTime(){
        String host="0.asia.pool.ntp.org";
        int timeout=30000;
        Method method = null;

        try {
            method = ServerTime.class.getDeclaredMethod("writeTimeStamp", new Class[]{String.class, int.class});
            method.setAccessible(true);
            method.invoke(serverTime,new Object[]{host,timeout});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}