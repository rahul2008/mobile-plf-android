package com.philips.platform.catk;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class CatkConstantsTest {


    @Test
    public void testDefaultTimeOut() {
        assertEquals(30000, new CatkConstants().DEFAULT_TIMEOUT_MS);
    }


    @Test
    public void testEmptyConstant() {
        assertEquals("", new CatkConstants().EMPTY_RESPONSE);
    }

    @Test
    public void testConsentSuccess() {
        assertEquals(0, new CatkConstants().CONSENT_SUCCESS);
    }

    @Test
    public void testConsentFailuer() {
        assertEquals(-1, new CatkConstants().CONSENT_ERROR);
    }

    @Test
    public void testConsentErrorno() {
        assertEquals(2, new CatkConstants().CONSENT_ERROR_NO_CONNECTION);
    }

    @Test
    public void testConnectionTimeout() {
        assertEquals(3, new CatkConstants().CONSENT_ERROR_CONNECTION_TIME_OUT);
    }

    @Test
    public void testAuthenticationFailuer() {
        assertEquals(4, new CatkConstants().CONSENT_ERROR_AUTHENTICATION_FAILURE);
    }

    @Test
    public void testConsentErorServers() {
        assertEquals(5, new CatkConstants().CONSENT_ERROR_SERVER_ERROR);
    }

    @Test
    public void testInsufficentStock() {
        assertEquals(6, new CatkConstants().CONSENT_ERROR_INSUFFICIENT_STOCK_ERROR);
    }

    @Test
    public void testConsentErroUnkown() {
        assertEquals(7, new CatkConstants().CONSENT_ERROR_UNKNOWN);
    }

    @Test
    public void testApplicationName() {
        assertEquals("OneBackend", new CatkConstants().APPLICATION_NAME);
    }

    @Test
    public void testPropositionName() {
        assertEquals("OneBackendProp", new CatkConstants().PROPOSITION_NAME);
    }

    @Test
    public void testBundleKeyName() {
        assertEquals("appName", new CatkConstants().BUNDLE_KEY_APPLICATION_NAME);
    }


    @Test
    public void testBundlePropositionName() {
        assertEquals("propName", new CatkConstants().BUNDLE_KEY_PROPOSITION_NAME);
    }

}