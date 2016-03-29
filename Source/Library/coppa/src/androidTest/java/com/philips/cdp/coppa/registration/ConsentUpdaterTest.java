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

}
