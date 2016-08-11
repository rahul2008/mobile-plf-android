package com.philips.platform.appinfra.timesync;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by 310238655 on 7/4/2016.
 */
public class TimeSyncTest extends MockitoTestCase {

    TimeInterface mTimeSyncInterface = null;
    // Context context = Mockito.mock(Context.class);

    private Context context;
    AppInfra mAppInfra;
    TimeSyncSntpClient mTimeSyncSntpClient;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mTimeSyncInterface = mAppInfra.getTime();
        assertNotNull(mTimeSyncInterface);
        mTimeSyncSntpClient = new TimeSyncSntpClient(mAppInfra);
    }

    public void testUTCTimeHappyPath() throws Exception {
        assertNotNull(mTimeSyncSntpClient);
        assertNotNull(mTimeSyncInterface.getUTCTime());
    }

    public void testgetCurrentTime() {
        assertNotNull(mTimeSyncSntpClient);
        mTimeSyncSntpClient.refreshOffset();
        assertNotNull(mTimeSyncSntpClient.getCurrentTime());
        assertNotNull(mTimeSyncSntpClient.getCurrentUTCTimeWithFormat());
    }

    public void testRefreshOffsetCall() {
        assertNotNull(mTimeSyncSntpClient);
        assertNotNull(mTimeSyncSntpClient.getCurrentTime());
        final SimpleDateFormat sdf = new SimpleDateFormat(TimeConstants.DATE_FORMAT, Locale.ENGLISH);
        Date date = new Date(0);
        sdf.setTimeZone(TimeZone.getTimeZone(TimeConstants.UTC));
        String firstJan1970 = sdf.format(date);
        assertNotSame(firstJan1970, mTimeSyncSntpClient.getCurrentTime());
    }


}
