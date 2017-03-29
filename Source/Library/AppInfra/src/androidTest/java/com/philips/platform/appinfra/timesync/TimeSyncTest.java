package com.philips.platform.appinfra.timesync;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
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


    public String testNtpJson() {
       String testJsonString = "{\n" +
                "  \"UR\": {\n" +
                "\n" +
                "    \"DEVELOPMENT\": \"ad7nn99y2mv5berw5jxewzagazafbyhu\",\n" +
                "    \"TESTING\": \"xru56jcnu3rpf8q7cgnkr7xtf9sh8pp7\",\n" +
                "    \"EVALUATION\": \"4r36zdbeycca933nufcknn2hnpsz6gxu\",\n" +
                "    \"STAGING\": \"f2stykcygm7enbwfw2u9fbg6h6syb8yd\",\n" +
                "    \"PRODUCTION\": \"mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3\"\n" +
                "\n" +
                "  },\n" +
                "  \"AI\": {\n" +
                "    \"MICROSITEID\": 77001,\n" +
                "    \"REGISTRATIONENVIRONMENT\": \"Staging\",\n" +
                "    \"NL\": [\"googleplus\", \"facebook\"  ],\n" +
                "    \"US\": [\"facebook\",\"googleplus\" ],\n" +
                "    \"MAP\": {\"one\": \"123\", \"two\": \"123.45\"},\n" +
                "    \"EE\": [123,234 ]\n" +
                "  }, \n" +
                " \"APPINFRA\": { \n" +
                "   \"APPIDENTITY.MICROSITEID\" : \"77000\",\n" +
                "  \"APPIDENTITY.SECTOR\"  : \"B2C\",\n" +
                " \"APPIDENTITY.APPSTATE\"  : \"Staging\",\n" +
                "\"APPIDENTITY.SERVICEDISCOVERYENVIRONMENT\"  : \"Staging\",\n" +
                "\"RESTCLIENT.CACHESIZEINKB\"  : 1024, \n" +
                " \"TAGGING.SENSITIVEDATA\": [\"bundleId, language\"] ,\n" +
                "  \"ABTEST.PRECACHE\":[\"philipsmobileappabtest1content\",\"philipsmobileappabtest1success\"],\n" +
                "    \"CONTENTLOADER.LIMITSIZE\":100,\n" +
                "    \"SERVICEDISCOVERY.PLATFORMMICROSITEID\":\"77000\",\n" +
                "    \"SERVICEDISCOVERY.PLATFORMENVIRONMENT\":\"production\",\n" +
                "    \"APPCONFIG.CLOUDSERVICEID\":\" appinfra.appconfigdownload\",\n" +
                "  \"TIMESYNC.NTP.HOSTS\":[\"0.pool.ntp.org\",\"1.pool.ntp.org\",\"2.pool.ntp.org\",\"3.pool.ntp.org\",\"0.cn.pool.ntp.org\"]\n" +
                "  }\n" +
                "}\n";

        assertNotNull(testJsonString);
        return testJsonString;
    }




    public void testNtpHostsFromAppConfigJson() throws IllegalArgumentException {
        try {
            AppConfigurationInterface mConfigInterface = new AppConfigurationManager(mAppInfra) {
                @Override
                protected JSONObject getMasterConfigFromApp() {
                    JSONObject result = null;
                    try {
                        String testJson = testNtpJson();
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


}
