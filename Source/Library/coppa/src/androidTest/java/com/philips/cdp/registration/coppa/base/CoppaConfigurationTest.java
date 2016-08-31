package com.philips.cdp.registration.coppa.base;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.mockito.Mock;

/**
 * Created by 310230979  on 8/30/2016.
 */
public class CoppaConfigurationTest extends InstrumentationTestCase {

    CoppaConfiguration mCoppaConfiguration;

    @Mock
    Consent consent;

    @Before
    public void setUp() throws Exception {

        mCoppaConfiguration = new CoppaConfiguration();
    }

    public void testConfirmation() throws Exception {
        assertEquals("confirmationCommunicationToSendAt", mCoppaConfiguration.CONFIRMATION_COMMUNICATION_TO_SEND_AT);
    }

    public void testid() throws Exception {
        assertEquals("id", mCoppaConfiguration.ID);
    }

    public void testLocale() throws Exception {
        assertEquals("locale", mCoppaConfiguration.LOCALE);
    }

    public void testGiven() throws Exception {
        assertEquals("given", mCoppaConfiguration.GIVEN);
    }

    public void testCommunicatio() throws Exception {
        assertEquals("confirmationCommunicationSentAt", mCoppaConfiguration.CONFIRMATION_COMMUNICATION_SENT_AT);
    }

    public void testConfirmationStore() throws Exception {
        assertEquals("confirmationStoredAt", mCoppaConfiguration.CONFIRMATION_STORED_AT);
    }

    public void testCommunicatioSentAt() throws Exception {
        assertEquals("communicationSentAt", mCoppaConfiguration.COMMUNICATION_SENT_AT);
    }

    public void testMicroSiteID() throws Exception {
        assertEquals("microSiteID", mCoppaConfiguration.MICRO_SITE_ID);
    }

    public void testCommunicatioGien() throws Exception {
        assertEquals("confirmationGiven", mCoppaConfiguration.CONFIRMATION_GIVEN);
    }

    public void testnull() throws Exception {
        assertEquals("null", mCoppaConfiguration.NULL);
    }

    public void testCamaingnid() throws Exception {
        assertEquals("campaignId", mCoppaConfiguration.CAMPAIGN_ID);
    }

    public void testConsents() throws Exception {
        assertEquals("consents", mCoppaConfiguration.CONSENTS);
    }

    public void testCoppaCommuncationSentAt() throws Exception {
        assertEquals("coppaCommunicationSentAt", mCoppaConfiguration.COPPA_COMMUNICATION_SENT_AT);
    }

    public void testIsCampaignIdPresent() {
        boolean result = mCoppaConfiguration.isCampaignIdPresent();
        assertFalse(result);
    }

    public void testConsent() {
        assertEquals(consent,mCoppaConfiguration.getConsent());
    }
    public void testConsentIndex(){
        assertEquals(0,mCoppaConfiguration.consentIndex());
    }


}