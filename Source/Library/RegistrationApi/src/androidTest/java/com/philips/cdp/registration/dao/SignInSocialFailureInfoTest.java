package com.philips.cdp.registration.dao;

import android.test.InstrumentationTestCase;

import org.junit.Test;

/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */
public class SignInSocialFailureInfoTest extends InstrumentationTestCase {
    SignInSocialFailureInfo signInSocialFailureInfo;

    @Override
    protected void setUp() throws Exception {
        signInSocialFailureInfo = new SignInSocialFailureInfo();
    }

    public void testDisplayNameErrorMessage() {
        signInSocialFailureInfo.setDisplayNameErrorMessage("DisplayNameErrorMessage");
        assertEquals("DisplayNameErrorMessage", signInSocialFailureInfo.getDisplayNameErrorMessage());
    }

    public void testEmailErrorMessage() {
        signInSocialFailureInfo.setEmailErrorMessage("EmailErrorMessage");
        assertEquals("EmailErrorMessage", signInSocialFailureInfo.getEmailErrorMessage());
    }

    public void testErrorCode() {
        signInSocialFailureInfo.setErrorCode(0);
        assertEquals(0, signInSocialFailureInfo.getErrorCode());
    }

    @Test
    public void TestError(SignInSocialFailureInfo signInError, SignInSocialFailureInfo inError) {
        assertEquals(signInError.getError(), inError.getError());
    }

    @Test
    public SignInSocialFailureInfo testSetMetadataObject(final SignInSocialFailureInfo signInSocialFailureInfo) {
        SignInSocialFailureInfo signInSocialFailureInfo1 = new SignInSocialFailureInfo();
        signInSocialFailureInfo1.setError(signInSocialFailureInfo.getError());
        return signInSocialFailureInfo1;
    }
}
