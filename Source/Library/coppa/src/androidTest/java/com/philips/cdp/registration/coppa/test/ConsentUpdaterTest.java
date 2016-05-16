package com.philips.cdp.registration.coppa.test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.coppa.ui.Activity.RegistrationCoppaActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 310202337 on 3/28/2016.
 */
public class ConsentUpdaterTest extends ActivityInstrumentationTestCase2<RegistrationCoppaActivity> {

    public ConsentUpdaterTest() {
        super(RegistrationCoppaActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
    }



}
