package com.philips.cdp.coppa.registration;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.coppa.base.Consent;
import com.philips.cdp.registration.coppa.ui.Activity.RegistrationCoppaActivity;

/**
 * Created by 310202337 on 3/29/2016.
 */
public class ConsentTest extends ActivityInstrumentationTestCase2<RegistrationCoppaActivity> {

    private Consent mConsent;

    public ConsentTest() {
        super(RegistrationCoppaActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        mConsent = new Consent();
    }


    public void testConfirmationGiven(){
        mConsent.setConfirmationGiven(Boolean.toString(true));
        assertTrue(Boolean.valueOf(mConsent.getConfirmationGiven()));
    }

    public void testMicroSiteID(){
        mConsent.setMicroSiteID("77000");
        assertEquals("77000", mConsent.getMicroSiteID());
    }

    public void testGiven(){
        mConsent.setGiven(Boolean.toString(true));
        assertTrue(Boolean.valueOf(mConsent.getGiven()));

    }








}
