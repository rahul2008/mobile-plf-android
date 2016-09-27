package com.philips.cdp.registration.apptagging;

import android.test.InstrumentationTestCase;

import org.junit.Before;

/**
 * Created by 310230979  on 8/30/2016.
 */
public class AppTagingConstantsTest extends InstrumentationTestCase {

    AppTagingConstants appTagingConstants;

    @Before
    public void setUp() throws Exception {

        appTagingConstants = new AppTagingConstants();
    }

    public void testSenData() throws Exception {
        assertEquals("sendData", appTagingConstants.SEND_DATA);
    }

    public void testRegidtrationChannel() throws Exception {
        assertEquals("registrationChannel", appTagingConstants.REGISTRATION_CHANNEL);
    }

    public void testSpecialEvents() throws Exception {
        assertEquals("specialEvents", appTagingConstants.SPECIAL_EVENTS);
    }

    public void testStatusNotification() throws Exception {
        assertEquals("statusNotification", appTagingConstants.STATUS_NOTIFICATION);
    }

    public void testMyPhilips() throws Exception {
        assertEquals("myphilips", appTagingConstants.MY_PHILIPS);
    }

    public void testStartUserRegistration() throws Exception {
        assertEquals("startUserRegistration", appTagingConstants.START_USER_REGISTRATION);
    }

    public void testLoginStart() throws Exception {
        assertEquals("loginStart", appTagingConstants.LOGIN_START);
    }

    public void testLoginChannel() throws Exception {
        assertEquals("loginChannel", appTagingConstants.LOGIN_CHANNEL);
    }

    public void testSuccessUserCreation() throws Exception {
        assertEquals("successUserCreation", appTagingConstants.SUCCESS_USER_CREATION);
    }

    public void testSuccessUserRegitration() throws Exception {
        assertEquals("successUserRegistration", appTagingConstants.SUCCESS_USER_REGISTRATION);
    }

    public void testsuccessSocuialMerge() throws Exception {
        assertEquals("successSocialMerge", appTagingConstants.SUCCESS_SOCIAL_MERGE);
    }

    public void testRemarkOptIN() throws Exception {
        assertEquals("remarketingOptIn", appTagingConstants.REMARKETING_OPTION_IN);
    }

    public void testTermsandCondaitionIn() throws Exception {
        assertEquals("termsAndConditionsOptIn", appTagingConstants.ACCEPT_TERMS_OPTION_IN);
    }

    public void testTermsandCondaitionOut() throws Exception {
        assertEquals("termsAndConditionsOptOut", appTagingConstants.ACCEPT_TERMS_OPTION_OUT);
    }

    public void testRemarketingOptOut() throws Exception {
        assertEquals("remarketingOptOut", appTagingConstants.REMARKETING_OPTION_OUT);
    }

    public void testSuccessLogin() throws Exception {
        assertEquals("successLogin", appTagingConstants.SUCCESS_LOGIN);
    }

    public void testStartSocialMerge() throws Exception {
        assertEquals("startSocialMerge", appTagingConstants.START_SOCIAL_MERGE);
    }

    public void testTechnicalError() throws Exception {
        assertEquals("technicalError", appTagingConstants.TECHNICAL_ERROR);
    }

    public void testUserError() throws Exception {
        assertEquals("userError", appTagingConstants.USER_ERROR);
    }

    public void testText() throws Exception {
        assertEquals("A link is sent to your email to reset the password of your Philips Account", appTagingConstants.RESET_PASSWORD_SUCCESS);
    }

    public void testErrorOne() throws Exception {
        assertEquals(111, appTagingConstants.NETWORK_ERROR_CODE);
    }

    public void testErrorTwo() throws Exception {
        assertEquals(112, appTagingConstants.EMAIL_NOT_VERIFIED);
    }

    public void testSignOut() throws Exception {
        assertEquals("signOut", appTagingConstants.SIGN_OUT);
    }

    public void testVerification() throws Exception {
        assertEquals("successResendEmailVerification", appTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
    }

    public void testRest() throws Exception {
        assertEquals("We have sent an email to your email address to reset your password", appTagingConstants.RESEND_VERIFICATION_MAIL_LINK_SENT);
    }

    public void testPreviously() throws Exception {
        assertEquals("you have logged in with a social provider previously", appTagingConstants.ALREADY_SIGN_IN_SOCIAL);
    }

    public void testTime() throws Exception {
        assertEquals("totalTimeInCreateAccount", appTagingConstants.TOTAL_TIME_CREATE_ACCOUNT);
    }

    public void testTimeCreatAccount() throws Exception {
        assertEquals("totalTimeInCreateAccount", appTagingConstants.TOTAL_TIME_CREATE_ACCOUNT);
    }

    public void testUserAlert() throws Exception {
        assertEquals("userAlert", appTagingConstants.USER_ALERT);
    }

    public void testInvalidEmail() throws Exception {
        assertEquals("Invalid email address", appTagingConstants.INVALID_EMAIL);
    }

    public void testGuidLIne() throws Exception {
        assertEquals("The password does not follow the password guidelines below.", appTagingConstants.WRONG_PASSWORD);
    }

    public void testShowPassword() throws Exception {
        assertEquals("showPassword", appTagingConstants.SHOW_PASSWORD);
    }

    public void testFirstNameEmpty() throws Exception {
        assertEquals("firsName : Field cannot be empty", appTagingConstants.FIELD_CANNOT_EMPTY_NAME);
    }

    public void testEmailEmpty() throws Exception {
        assertEquals("emailAddress : Field cannot be empty", appTagingConstants.FIELD_CANNOT_EMPTY_EMAIL);
    }

    public void testPasswordEmpty() throws Exception {
        assertEquals("password : Field cannot be empty", appTagingConstants.FIELD_CANNOT_EMPTY_PASSWORD);
    }
}