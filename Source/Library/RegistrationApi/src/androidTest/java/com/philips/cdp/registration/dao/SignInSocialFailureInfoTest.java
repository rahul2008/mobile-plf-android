package com.philips.cdp.registration.dao;

import com.janrain.android.Jump;
import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

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
    public void testSetError(){
        signInSocialFailureInfo.setError(error);
        assertEquals(error,signInSocialFailureInfo.getError());
    }

    public void testGetErrorDescription(){
         assertEquals(null,signInSocialFailureInfo.getErrorDescription());
    }

}
