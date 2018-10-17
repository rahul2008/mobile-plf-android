package com.philips.cdp.registration.app.tagging;


import com.philips.cdp.registration.errors.ErrorCodes;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AppTagingConstantsTest  extends TestCase {

    @Test
    public void testSenData() {
        assertEquals("sendData", AppTagingConstants.SEND_DATA);
    }

    @Test
    public void testSpecialEvents() {
        assertEquals("specialEvents", AppTagingConstants.SPECIAL_EVENTS);
    }
    @Test
    public void testStatusNotification() {
        assertEquals("statusNotification", AppTagingConstants.STATUS_NOTIFICATION);
    }
    @Test
    public void testMyPhilips() {
        assertEquals("myphilips", AppTagingConstants.MY_PHILIPS);
    }
    @Test
    public void testStartUserRegistration() {
        assertEquals("startUserRegistration", AppTagingConstants.START_USER_REGISTRATION);
    }

    @Test
    public void testSuccessUserCreation() {
        assertEquals("successUserCreation", AppTagingConstants.SUCCESS_USER_CREATION);
    }
    @Test
    public void testSuccessUserRegitration() {
        assertEquals("successUserRegistration", AppTagingConstants.SUCCESS_USER_REGISTRATION);
    }

    @Test
    public void testRemarkOptIN() {
        assertEquals("remarketingOptIn", AppTagingConstants.REMARKETING_OPTION_IN);
    }
    @Test
    public void testTermsandCondaitionIn() {
        assertEquals("termsAndConditionsOptIn", AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
    }
    @Test
    public void testTermsandCondaitionOut() {
        assertEquals("termsAndConditionsOptOut", AppTagingConstants.ACCEPT_TERMS_OPTION_OUT);
    }
    @Test
    public void testRemarketingOptOut() {
        assertEquals("remarketingOptOut", AppTagingConstants.REMARKETING_OPTION_OUT);
    }
    @Test
    public void testSuccessLogin() {
        assertEquals("successLogin", AppTagingConstants.SUCCESS_LOGIN);
    }
    @Test
    public void testStartSocialMerge() {
        assertEquals("startSocialMerge", AppTagingConstants.START_SOCIAL_MERGE);
    }
    @Test
    public void testTechnicalError() {
        assertEquals("technicalError", AppTagingConstants.TECHNICAL_ERROR);
    }
    @Test
    public void testUserError() {
        assertEquals("userError", AppTagingConstants.USER_ERROR);
    }
    @Test
    public void testText() {
        assertEquals("A link is sent to your email to reset the password of your Philips Account", AppTagingConstants.RESET_PASSWORD_SUCCESS);
    }
    @Test
    public void testErrorOne() {
        assertEquals(-101, ErrorCodes.NETWORK_ERROR);
    }
    @Test
    public void testErrorTwo() {
        assertEquals(112, AppTagingConstants.EMAIL_NOT_VERIFIED_CODE);
    }
    @Test
    public void testSignOut() {
        assertEquals("signOut", AppTagingConstants.SIGN_OUT);
    }
    @Test
    public void testVerification() {
        assertEquals("successResendEmailVerification", AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
    }
    @Test
    public void testRest() {
        assertEquals("We have sent an email to your email address to reset your password", AppTagingConstants.RESEND_VERIFICATION_MAIL_LINK_SENT);
    }
    @Test
    public void testPreviously() {
        assertEquals("you have logged in with a social provider previously", AppTagingConstants.ALREADY_SIGN_IN_SOCIAL);
    }
    @Test
    public void testTime() {
        assertEquals("totalTimeInCreateAccount", AppTagingConstants.TOTAL_TIME_CREATE_ACCOUNT);
    }
    @Test
    public void testTimeCreatAccount() {
        assertEquals("totalTimeInCreateAccount", AppTagingConstants.TOTAL_TIME_CREATE_ACCOUNT);
    }

    @Test
    public void testInvalidEmail() {
        assertEquals("Invalid email address", AppTagingConstants.INVALID_EMAIL);
    }
    @Test
    public void testGuidLIne() {
        assertEquals("The password does not follow the password guidelines below.", AppTagingConstants.WRONG_PASSWORD);
    }
    @Test
    public void testFirstNameEmpty() {
        assertEquals("firsName : Field cannot be empty", AppTagingConstants.FIELD_CANNOT_EMPTY_NAME);
    }
    @Test
    public void testEmailEmpty() {
        assertEquals("emailAddress : Field cannot be empty", AppTagingConstants.FIELD_CANNOT_EMPTY_EMAIL);
    }
    @Test
    public void testPasswordEmpty() {
        assertEquals("password : Field cannot be empty", AppTagingConstants.FIELD_CANNOT_EMPTY_PASSWORD);
    }
}