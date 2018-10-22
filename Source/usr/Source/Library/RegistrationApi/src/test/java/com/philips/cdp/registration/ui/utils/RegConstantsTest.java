package com.philips.cdp.registration.ui.utils;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RegConstantsTest extends TestCase {
    private RegConstants mRegConstants;

    @Override

    public void setUp() throws Exception {

        super.setUp();

        mRegConstants = new RegConstants();
    }

    @Test
    public void testFirstName() {
        assertEquals("traditionalRegistration_firstName", RegConstants.TRADITIONAL_REGISTRATION_FIRST_NAME);
    }

    @Test
    public void testPassword() {
        assertEquals("traditionalRegistration_password", RegConstants.TRADITIONAL_REGISTRATION_PASSWORD);
    }

    @Test
    public void testEmailAddress() {
        assertEquals("traditionalRegistration_emailAddress", RegConstants.TRADITIONAL_REGISTRATION_EMAIL_ADDRESS);
    }

    @Test
    public void testSuchAccount() {
        assertEquals("no_such_account", RegConstants.NO_SUCH_ACCOUNT);
    }

    @Test
    public void testForgotPasswordForm() {
        assertEquals("forgotPasswordForm", RegConstants.FORGOT_PASSWORD_FORM);
    }

    @Test
    public void testResendVerificationForm() {
        assertEquals("resendVerificationForm", RegConstants.RESEND_VERIFICATION_FORM);
    }

    @Test

    public void testUserInformationForm() {
        assertEquals("userInformationForm", RegConstants.USER_INFORMATION_FORM);
    }

    @Test
    public void testJanrinSuccess() {
        assertEquals("JANRAIN_SUCCESS", RegConstants.JANRAIN_INIT_SUCCESS);
    }

    @Test
    public void testJainainFailure() {
        assertEquals("JANRAIN_FAILURE", RegConstants.JANRAIN_INIT_FAILURE);
    }

    @Test
    public void testGivenName() {
        assertEquals("givenName", RegConstants.REGISTER_GIVEN_NAME);
    }

    @Test
    public void testDisplayName() {
        assertEquals("displayName", RegConstants.REGISTER_DISPLAY_NAME);
    }

    @Test
    public void testfamilyname() {
        assertEquals("familyName", RegConstants.REGISTER_FAMILY_NAME);
    }

    @Test
    public void testrmail() {
        assertEquals("email", RegConstants.REGISTER_EMAIL);
    }

    @Test
    public void testsocialprovider() {
        assertEquals("SOCIAL_PROVIDER", RegConstants.SOCIAL_PROVIDER);
    }

    @Test
    public void testconflicting() {
        assertEquals("CONFLICTING_SOCIAL_PROVIDER", RegConstants.CONFLICTING_SOCIAL_PROVIDER);
    }

    @Test
    public void testsociatwo() {
        assertEquals("SOCIAL_TWO_STEP_ERROR", RegConstants.SOCIAL_TWO_STEP_ERROR);
    }

    @Test
    public void testregistrationtoken() {
        assertEquals("SOCIAL_REGISTRATION_TOKEN", RegConstants.SOCIAL_REGISTRATION_TOKEN);
    }

    @Test
    public void testempty() {
        assertEquals("", RegConstants.SOCIAL_BLANK_CHARACTER);
    }

    @Test
    public void testtoken() {
        assertEquals("SOCIAL_MERGE_TOKEN", RegConstants.SOCIAL_MERGE_TOKEN);
    }

    @Test
    public void testsocialRegistration_displayName() {
        assertEquals("socialRegistration_displayName", RegConstants.SOCIAL_REGISTRATION_DISPLAY_NAME);
    }

    @Test
    public void testsocialRegistration_emailAddress() {
        assertEquals("socialRegistration_emailAddress", RegConstants.SOCIAL_REGISTRATION_EMAIL_ADDRESS);
    }
}