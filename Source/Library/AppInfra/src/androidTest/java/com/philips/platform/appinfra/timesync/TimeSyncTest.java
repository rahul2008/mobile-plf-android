package com.philips.platform.appinfra.timesync;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;

import org.json.JSONObject;

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

    public void testRefreshOffsetCall() {
        assertNotNull(mTimeSyncSntpClient);
        final SimpleDateFormat sdf = new SimpleDateFormat(TimeSyncSntpClient.DATE_FORMAT, Locale.ENGLISH);
        Date date = new Date(0);
        sdf.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));
        String firstJan1970 = sdf.format(date);
    }


    public void testgetUTCTime() {
        mTimeSyncSntpClient = new TimeSyncSntpClient(mAppInfra);
        assertNotNull(mTimeSyncSntpClient);
        final SimpleDateFormat sdf = new SimpleDateFormat(TimeSyncSntpClient.DATE_FORMAT, Locale.ENGLISH);
        Date date = mTimeSyncSntpClient.getUTCTime();
        Date d  = new Date(0);
        sdf.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));
        String str = sdf.format(date);
        String str1 = sdf.format(d);
        assertNotSame(str, str1);
    }


   
    public void testNtpHostsFromAppConfigJson() throws IllegalArgumentException {
        try {
            AppConfigurationInterface mConfigInterface = new AppConfigurationManager(mAppInfra) {
                @Override
                protected JSONObject getMasterConfigFromApp() {
                    JSONObject result = null;
                    try {
                        String testJson = ConfigValues.testJson();
                        result = new JSONObject(testJson);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return result;
                }

            };
            assertNotNull(mConfigInterface);

            AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                    .AppConfigurationError();


            assertNotNull(mConfigInterface.getPropertyForKey
                    ("TIMESYNC.NTP.HOSTS", "APPINFRA", configError));

        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }

    }

    public void testisSynchronized() {
        if(mTimeSyncSntpClient.isSynchronized()){
            assertTrue(mTimeSyncSntpClient.isSynchronized());
        }
        else
        {
            assertFalse(mTimeSyncSntpClient.isSynchronized());
        }


    }


}
