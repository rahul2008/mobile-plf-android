/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import com.philips.platform.pif.chi.ConsentError;

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
        assertEquals(0, ConsentError.CONSENT_SUCCESS);
    }

    @Test
    public void testConsentFailuer() {
        assertEquals(-1, ConsentError.CONSENT_ERROR);
    }

    @Test
    public void testConsentErrorno() {
        assertEquals(2, ConsentError.CONSENT_ERROR_NO_CONNECTION);
    }

    @Test
    public void testConnectionTimeout() {
        assertEquals(3, ConsentError.CONSENT_ERROR_CONNECTION_TIME_OUT);
    }

    @Test
    public void testAuthenticationFailuer() {
        assertEquals(4, ConsentError.CONSENT_ERROR_AUTHENTICATION_FAILURE);
    }

    @Test
    public void testConsentErorServers() {
        assertEquals(5, ConsentError.CONSENT_ERROR_SERVER_ERROR);
    }

    @Test
    public void testInsufficentStock() {
        assertEquals(6, ConsentError.CONSENT_ERROR_INSUFFICIENT_STOCK_ERROR);
    }

    @Test
    public void testConsentErroUnkown() {
        assertEquals(7, ConsentError.CONSENT_ERROR_UNKNOWN);
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