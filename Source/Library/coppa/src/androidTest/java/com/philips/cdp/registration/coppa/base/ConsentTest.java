/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.base;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.coppa.base.Consent;
import com.philips.cdp.registration.coppa.ui.activity.RegistrationCoppaActivity;

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

    public void testCampaignId(){
        mConsent.setCampaignId("COPPA");
        assertEquals("COPPA", mConsent.getCampaignId());
    }

    public void testCommunicationSentAt(){
        mConsent.setCommunicationSentAt("1928-10-30");
        assertEquals("1928-10-30", mConsent.getCommunicationSentAt());

    }

    public void testConfirmationStoredAt(){
        mConsent.setConfirmationStoredAt("1928-10-30");
        assertEquals("1928-10-30",mConsent.getConfirmationStoredAt());
    }

    public void testgetConfirmationCommunicationSentAt(){
        mConsent.setConfirmationCommunicationSentAt("1928-10-30");
        assertEquals("1928-10-30", mConsent.getConfirmationCommunicationSentAt());
    }

    public void testGetLocale(){
        mConsent.setLocale("en-US");
        assertEquals("en-US",mConsent.getLocale());
    }

    public void testId(){
        mConsent.setId("22222222");
        assertEquals("22222222",mConsent.getId());
    }














}
