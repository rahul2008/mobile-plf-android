/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.servertime;

import android.test.InstrumentationTestCase;

import com.philips.cdp.recievers.DateTimeChangedReceiver;
import com.philips.cdp.servertime.constants.ServerTimeConstants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

//import org.mockito.Mockito;


public class ServerTimeTest extends InstrumentationTestCase {
    private DateTimeChangedReceiver mReceiver;
    private TestContext mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mReceiver = new DateTimeChangedReceiver();
        mContext = new TestContext();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

    }


    public void testRefreshOffset(){
        ServerTime.init(getInstrumentation().getTargetContext());
        ServerTime.getInstance().refreshOffset();

        assertNotNull(ServerTime.getInstance().getCurrentTime());
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

    private  long getCurrentTimeZoneDiff(){
        TimeZone timeZoneInDevice = TimeZone.getDefault();
        int differentialOfTimeZones = timeZoneInDevice.getOffset(System.currentTimeMillis());
        return  differentialOfTimeZones;
    }


}