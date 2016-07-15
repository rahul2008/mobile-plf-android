package com.philips.platform.appinfra.timesync;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

/**
 * Created by 310238655 on 7/4/2016.
 */
public class TimeSyncTest extends MockitoTestCase {

    TimeInterface mTimeSyncInterface=null;
    // Context context = Mockito.mock(Context.class);

    private Context context;
    AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra =  new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mTimeSyncInterface = mAppInfra.getTimeSync();
        assertNotNull(mTimeSyncInterface);
    }
    public void testUTCTimeHappyPath()throws Exception {
        TimeSyncSntpClient mTimeSyncSntpClient = new TimeSyncSntpClient(mAppInfra);
        assertNotNull(mTimeSyncSntpClient);
        assertNotNull(mTimeSyncSntpClient.getCurrentTime());
        assertNotNull(mTimeSyncSntpClient.getCurrentUTCTimeWithFormat("dd/MM/yyyy hh:mm:ss.SSS"));
        assertNotNull(mTimeSyncSntpClient.getNtpTime());
        assertNotNull(mTimeSyncSntpClient.getNtpTimeReference());
        assertNotNull(mTimeSyncSntpClient.getRoundTripTime());
        assertNotNull(mTimeSyncInterface.getUTCTime());

    }
}
