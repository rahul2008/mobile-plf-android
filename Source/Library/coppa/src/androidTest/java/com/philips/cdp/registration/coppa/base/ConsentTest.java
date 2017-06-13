/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.base;


import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


public class ConsentTest extends RegistrationApiInstrumentationBase {

     Consent mConsent;

    @Override
    public void setUp() throws Exception {

        super.setUp();
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

    public void testStoredAt(){
        mConsent.setStoredAt("22222222");
        assertEquals("22222222",mConsent.getStoredAt());
    }
    public void testConfirmationCommunicationToSendAt(){
        mConsent.setConfirmationCommunicationToSendAt("22222222");
        assertEquals("22222222",mConsent.getConfirmationCommunicationToSendAt());
    }












}
