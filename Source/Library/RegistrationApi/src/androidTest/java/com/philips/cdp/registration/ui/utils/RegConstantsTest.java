package com.philips.cdp.registration.ui.utils;

import android.test.InstrumentationTestCase;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegConstantsTest extends InstrumentationTestCase {

    RegConstants mRegConstants;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mRegConstants = new RegConstants();
    }

    public void testFirstName() throws Exception {
        assertEquals("traditionalRegistration_firstName", mRegConstants.TRADITIONAL_REGISTRATION_FIRST_NAME);
    }

    public void testPassword() throws Exception {
        assertEquals("traditionalRegistration_password", mRegConstants.TRADITIONAL_REGISTRATION_PASSWORD);
    }

    public void testEmailAddress() throws Exception {
        assertEquals("traditionalRegistration_emailAddress", mRegConstants.TRADITIONAL_REGISTRATION_EMAIL_ADDRESS);
    }

    public void testSiginPassword() throws Exception {
        assertEquals("traditionalSignIn_password", mRegConstants.TRADITIONAL_SIGN_IN_PASSWORD);
    }

    public void testSignEmailAddress() throws Exception {
        assertEquals("traditionalSignIn_emailAddress", mRegConstants.TRADITIONAL_SIGN_IN_EMAIL_ADDRESS);
    }

    public void testFields() throws Exception {
        assertEquals("invalid_fields", mRegConstants.INVALID_FIELDS);
    }

    public void testFormFields() throws Exception {
        assertEquals("invalid_form_fields", mRegConstants.INVALID_FORM_FIELDS);
    }

    public void testSuchAccount() throws Exception {
        assertEquals("no_such_account", mRegConstants.NO_SUCH_ACCOUNT);
    }

    public void testForgotPasswordForm() throws Exception {
        assertEquals("forgotPasswordForm", mRegConstants.FORGOT_PASSWORD_FORM);
    }

    public void testResendVerificationForm() throws Exception {
        assertEquals("resendVerificationForm", mRegConstants.RESEND_VERIFICATION_FORM);
    }

    public void testInvalid_credentials() throws Exception {
        assertEquals("invalid_credentials", mRegConstants.INVALID_CREDENTIALS);
    }

    public void testUserInformationForm() throws Exception {
        assertEquals("userInformationForm", mRegConstants.USER_INFORMATION_FORM);
    }

    public void testJanrinSuccess() throws Exception {
        assertEquals("JANRAIN_SUCCESS", mRegConstants.JANRAIN_INIT_SUCCESS);
    }

    public void testJainainFailure() throws Exception {
        assertEquals("JANRAIN_FAILURE", mRegConstants.JANRAIN_INIT_FAILURE);
    }

    public void testParsingCompleted() throws Exception {
        assertEquals("PARSING_COMPLETED", mRegConstants.PARSING_COMPLETED);
    }

    public void testGivenName() throws Exception {
        assertEquals("givenName", mRegConstants.REGISTER_GIVEN_NAME);
    }

    public void testDisplayName() throws Exception {
        assertEquals("displayName", mRegConstants.REGISTER_DISPLAY_NAME);
    }

    public void testfamilyname() throws Exception {
        assertEquals("familyName", mRegConstants.REGISTER_FAMILY_NAME);
    }

    public void testrmail() throws Exception {
        assertEquals("email", mRegConstants.REGISTER_EMAIL);
    }

    public void testerror() throws Exception {
        assertEquals(540, mRegConstants.ONLY_SOCIAL_SIGN_IN_ERROR_CODE);
    }

    public void testsocialprovider() throws Exception {
        assertEquals("SOCIAL_PROVIDER", mRegConstants.SOCIAL_PROVIDER);
    }

    public void testconflicting() throws Exception {
        assertEquals("CONFLICTING_SOCIAL_PROVIDER", mRegConstants.CONFLICTING_SOCIAL_PROVIDER);
    }

    public void testsociatwo() throws Exception {
        assertEquals("SOCIAL_TWO_STEP_ERROR", mRegConstants.SOCIAL_TWO_STEP_ERROR);
    }

    public void testregistrationtoken() throws Exception {
        assertEquals("SOCIAL_REGISTRATION_TOKEN", mRegConstants.SOCIAL_REGISTRATION_TOKEN);
    }

    public void testempty() throws Exception {
        assertEquals("", mRegConstants.SOCIAL_BLANK_CHARACTER);
    }

    public void testtoken() throws Exception {
        assertEquals("SOCIAL_MERGE_TOKEN", mRegConstants.SOCIAL_MERGE_TOKEN);
    }

    public void testsocialRegistration_displayName() throws Exception {
        assertEquals("socialRegistration_displayName", mRegConstants.SOCIAL_REGISTRATION_DISPLAY_NAME);
    }

    public void testsocialRegistration_emailAddress() throws Exception {
        assertEquals("socialRegistration_emailAddress", mRegConstants.SOCIAL_REGISTRATION_EMAIL_ADDRESS);
    }
}