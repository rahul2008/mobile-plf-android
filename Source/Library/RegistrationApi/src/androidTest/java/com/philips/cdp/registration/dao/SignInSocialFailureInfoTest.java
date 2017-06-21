package com.philips.cdp.registration.dao;

import com.janrain.android.Jump;
import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;

/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */
public class SignInSocialFailureInfoTest extends RegistrationApiInstrumentationBase {
    SignInSocialFailureInfo signInSocialFailureInfo;
    @Mock
    Jump.SignInResultHandler.SignInError error;

    @Override

    public void setUp() throws Exception {
        signInSocialFailureInfo = new SignInSocialFailureInfo();
    }
    @Test
    public void testDisplayNameErrorMessage() {
        signInSocialFailureInfo.setDisplayNameErrorMessage("DisplayNameErrorMessage");
        assertEquals("DisplayNameErrorMessage", signInSocialFailureInfo.getDisplayNameErrorMessage());
    }
    @Test
    public void testEmailErrorMessage() {
        signInSocialFailureInfo.setEmailErrorMessage("EmailErrorMessage");
        assertEquals("EmailErrorMessage", signInSocialFailureInfo.getEmailErrorMessage());
    }
    @Test
    public void testErrorCode() {
        signInSocialFailureInfo.setErrorCode(0);
        assertEquals(0, signInSocialFailureInfo.getErrorCode());
    }
    @Test
    public void testSetError(){
        signInSocialFailureInfo.setError(error);
        assertEquals(error,signInSocialFailureInfo.getError());
    }

    @Test
    public void testGetErrorDescription(){
         assertEquals(null,signInSocialFailureInfo.getErrorDescription());
    }

}
