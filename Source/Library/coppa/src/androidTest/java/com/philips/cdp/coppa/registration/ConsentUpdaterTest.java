package com.philips.cdp.coppa.registration;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.coppa.CoppaExtension;
import com.philips.cdp.registration.coppa.interfaces.CoppaConsentUpdateCallback;
import com.philips.cdp.registration.coppa.ui.Activity.RegistrationActivity1;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 310202337 on 3/28/2016.
 */
public class ConsentUpdaterTest extends ActivityInstrumentationTestCase2<RegistrationActivity1> {

    public ConsentUpdaterTest() {
        super(RegistrationActivity1.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
    }

    public void testUpdateConsent(){
        String s = "{\"visitedMicroSites\":[{\"timestamp\":\"2016-03-11 16:08:03 +0000\",\"id\":1130983790,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-11 16:10:15 +0000\",\"id\":1135581468,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-11 16:07:31 +0000\",\"id\":1135930967,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-11 15:24:00 +0000\",\"id\":1135972316,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-11 15:31:44 +0000\",\"id\":1135987074,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-11 16:07:42 +0000\",\"id\":1135990623,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-11 16:07:54 +0000\",\"id\":1135992900,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-11 16:06:42 +0000\",\"id\":1135994114,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-11 17:17:30 +0000\",\"id\":1136391335,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-14 15:26:51 +0000\",\"id\":1145813586,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-14 15:31:01 +0000\",\"id\":1145870688,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-15 11:32:46 +0000\",\"id\":1149958926,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-15 15:14:59 +0000\",\"id\":1150822826,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-15 18:16:52 +0000\",\"id\":1151398339,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-15 18:10:01 +0000\",\"id\":1151473048,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-15 18:14:50 +0000\",\"id\":1151486821,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-15 18:19:49 +0000\",\"id\":1151709284,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-16 07:08:18 +0000\",\"id\":1152451743,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-16 15:10:10 +0000\",\"id\":1152541062,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-16 15:13:30 +0000\",\"id\":1152554217,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-16 15:12:54 +0000\",\"id\":1152575604,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-16 15:11:54 +0000\",\"id\":1152577461,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-16 09:40:02.976 +0000\",\"id\":1152577568,\"microSiteID\":\"81448\"},{\"timestamp\":\"2016-03-16 17:49:30 +0000\",\"id\":1152997894,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-17 17:10:10 +0000\",\"id\":1157444432,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-18 07:57:25 +0000\",\"id\":1159566123,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-18 12:44:39 +0000\",\"id\":1159824721,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-18 15:33:52 +0000\",\"id\":1160588303,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-21 13:00:51 +0000\",\"id\":1170229131,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-21 13:02:54 +0000\",\"id\":1170390034,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-21 13:16:28 +0000\",\"id\":1170406490,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-21 14:49:20 +0000\",\"id\":1170743523,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-23 15:20:44 +0000\",\"id\":1171524589,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-23 15:31:07 +0000\",\"id\":1171524590,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-23 15:03:27 +0000\",\"id\":1177735137,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-23 13:07:57 +0000\",\"id\":1179696406,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-23 15:26:43 +0000\",\"id\":1180736413,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-23 15:11:21 +0000\",\"id\":1180895515,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-23 15:22:39 +0000\",\"id\":1181219119,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-23 15:13:48 +0000\",\"id\":1181229712,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-23 16:30:19 +0000\",\"id\":1181899071,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-23 16:34:44 +0000\",\"id\":1182016597,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-23 16:34:25 +0000\",\"id\":1182038103,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-25 19:59:40 +0000\",\"id\":1189274019,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-25 19:22:12 +0000\",\"id\":1189281682,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-25 19:19:14 +0000\",\"id\":1189284077,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-25 19:22:56 +0000\",\"id\":1189286869,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-25 19:20:40 +0000\",\"id\":1189287632,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-25 19:24:45 +0000\",\"id\":1189287633,\"microSiteID\":\"77000\"},{\"timestamp\":\"2016-03-25 19:45:18 +0000\",\"id\":1189287998,\"microSiteID\":\"77000\"}";
        try {
            JSONObject jsonObject = new JSONObject(s);
            CoppaExtension coppaExtension = new CoppaExtension(getInstrumentation().getTargetContext(), jsonObject);
            coppaExtension.updateCoppaConsentStatus(true, new CoppaConsentUpdateCallback() {
                @Override
                public void onSuccess() {
                    System.out.println("Coppa COnsent Update Success");
                }

                @Override
                public void onFailure(String message) {
                    System.out.println("Coppa COnsent Update Failure " +message);

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
