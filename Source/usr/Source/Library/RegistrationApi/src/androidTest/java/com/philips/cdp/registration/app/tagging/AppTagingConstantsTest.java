package com.philips.cdp.registration.app.tagging;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class AppTagingConstantsTest  extends RegistrationApiInstrumentationBase {

    AppTagingConstants appTagingConstants;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        appTagingConstants = new AppTagingConstants();
    }
    @Test
    public void testSenData() throws Exception {
        assertEquals("sendData", appTagingConstants.SEND_DATA);
    }
    @Test
    public void testRegidtrationChannel() throws Exception {
        assertEquals("registrationChannel", appTagingConstants.REGISTRATION_CHANNEL);
    }
    @Test
    public void testSpecialEvents() throws Exception {
        assertEquals("specialEvents", appTagingConstants.SPECIAL_EVENTS);
    }
    @Test
    public void testStatusNotification() throws Exception {
        assertEquals("statusNotification", appTagingConstants.STATUS_NOTIFICATION);
    }
    @Test
    public void testMyPhilips() throws Exception {
        assertEquals("myphilips", appTagingConstants.MY_PHILIPS);
    }
    @Test
    public void testStartUserRegistration() throws Exception {
        assertEquals("startUserRegistration", appTagingConstants.START_USER_REGISTRATION);
    }
    @Test
    public void testLoginStart() throws Exception {
        assertEquals("loginStart", appTagingConstants.LOGIN_START);
    }
    @Test
    public void testLoginChannel() throws Exception {
        assertEquals("loginChannel", appTagingConstants.LOGIN_CHANNEL);
    }
    @Test
    public void testSuccessUserCreation() throws Exception {
        assertEquals("successUserCreation", appTagingConstants.SUCCESS_USER_CREATION);
    }
    @Test
    public void testSuccessUserRegitration() throws Exception {
        assertEquals("successUserRegistration", appTagingConstants.SUCCESS_USER_REGISTRATION);
    }
    @Test
    public void testsuccessSocuialMerge() throws Exception {
        assertEquals("successSocialMerge", appTagingConstants.SUCCESS_SOCIAL_MERGE);
    }
    @Test
    public void testRemarkOptIN() throws Exception {
        assertEquals("remarketingOptIn", appTagingConstants.REMARKETING_OPTION_IN);
    }
    @Test
    public void testTermsandCondaitionIn() throws Exception {
        assertEquals("termsAndConditionsOptIn", appTagingConstants.ACCEPT_TERMS_OPTION_IN);
    }
    @Test
    public void testTermsandCondaitionOut() throws Exception {
        assertEquals("termsAndConditionsOptOut", appTagingConstants.ACCEPT_TERMS_OPTION_OUT);
    }
    @Test
    public void testRemarketingOptOut() throws Exception {
        assertEquals("remarketingOptOut", appTagingConstants.REMARKETING_OPTION_OUT);
    }
    @Test
    public void testSuccessLogin() throws Exception {
        assertEquals("successLogin", appTagingConstants.SUCCESS_LOGIN);
    }
    @Test
    public void testStartSocialMerge() throws Exception {
        assertEquals("startSocialMerge", appTagingConstants.START_SOCIAL_MERGE);
    }
    @Test
    public void testTechnicalError() throws Exception {
        assertEquals("error", appTagingConstants.TECHNICAL_ERROR);
    }
    @Test
    public void testUserError() throws Exception {
        assertEquals("error", appTagingConstants.USER_ERROR);
    }
    @Test
    public void testText() throws Exception {
        assertEquals("A link is sent to your email to reset the password of your Philips Account", appTagingConstants.RESET_PASSWORD_SUCCESS);
    }
    @Test
    public void testErrorOne() throws Exception {
        assertEquals(111, appTagingConstants.NETWORK_ERROR_CODE);
    }
    @Test
    public void testErrorTwo() throws Exception {
        assertEquals(112, appTagingConstants.EMAIL_NOT_VERIFIED_CODE);
    }
    @Test
    public void testSignOut() throws Exception {
        assertEquals("signOut", appTagingConstants.SIGN_OUT);
    }
    @Test
    public void testVerification() throws Exception {
        assertEquals("successResendEmailVerification", appTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
    }
    @Test
    public void testRest() throws Exception {
        assertEquals("We have sent an email to your email address to reset your password", appTagingConstants.RESEND_VERIFICATION_MAIL_LINK_SENT);
    }
    @Test
    public void testPreviously() throws Exception {
        assertEquals("you have logged in with a social provider previously", appTagingConstants.ALREADY_SIGN_IN_SOCIAL);
    }
    @Test
    public void testTime() throws Exception {
        assertEquals("totalTimeInCreateAccount", appTagingConstants.TOTAL_TIME_CREATE_ACCOUNT);
    }
    @Test
    public void testTimeCreatAccount() throws Exception {
        assertEquals("totalTimeInCreateAccount", appTagingConstants.TOTAL_TIME_CREATE_ACCOUNT);
    }
    @Test
    public void testUserAlert() throws Exception {
        assertEquals("userAlert", appTagingConstants.USER_ALERT);
    }
    @Test
    public void testInvalidEmail() throws Exception {
        assertEquals("Invalid email address", appTagingConstants.INVALID_EMAIL);
    }
    @Test
    public void testGuidLIne() throws Exception {
        assertEquals("The password does not follow the password guidelines below.", appTagingConstants.WRONG_PASSWORD);
    }
    @Test
    public void testShowPassword() throws Exception {
        assertEquals("showPassword", appTagingConstants.SHOW_PASSWORD);
    }
    @Test
    public void testFirstNameEmpty() throws Exception {
        assertEquals("firsName : Field cannot be empty", appTagingConstants.FIELD_CANNOT_EMPTY_NAME);
    }
    @Test
    public void testEmailEmpty() throws Exception {
        assertEquals("emailAddress : Field cannot be empty", appTagingConstants.FIELD_CANNOT_EMPTY_EMAIL);
    }
    @Test
    public void testPasswordEmpty() throws Exception {
        assertEquals("password : Field cannot be empty", appTagingConstants.FIELD_CANNOT_EMPTY_PASSWORD);
    }
}