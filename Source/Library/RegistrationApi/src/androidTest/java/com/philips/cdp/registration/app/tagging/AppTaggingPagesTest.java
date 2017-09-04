package com.philips.cdp.registration.app.tagging;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;



public class AppTaggingPagesTest extends RegistrationApiInstrumentationBase {

    AppTaggingPages appTaggingPages;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        appTaggingPages = new AppTaggingPages();
    }

    @Test
    public void testHome() throws  Exception{
        assertEquals("registration:home",appTaggingPages.HOME);
    }

    @Test
    public void testCreatAccount() throws Exception {
        assertEquals("registration:createaccount",appTaggingPages.CREATE_ACCOUNT);

    }
    @Test
    public void testSignIn() throws Exception {
        assertEquals("registration:signin",AppTaggingPages.SIGN_IN_ACCOUNT);
    }
    @Test
    public void testAccountActivaton() throws Exception {
        assertEquals("registration:accountactivation",AppTaggingPages.ACCOUNT_ACTIVATION);
    }

    @Test
    public void testAlomastDone() throws Exception {
        assertEquals("registration:almostdone",AppTaggingPages.ALMOST_DONE);
    }
    @Test
    public void testMergeAccount() throws Exception {
        assertEquals("registration:mergeaccount",AppTaggingPages.MERGE_ACCOUNT);
    }
    @Test
    public void testUserProfile() throws Exception {
        assertEquals("registration:userprofile",AppTaggingPages.USER_PROFILE);
    }


    @Test
    public void testMergeSocialAccount() throws Exception {
        assertEquals("registration:mergesocialaccount",AppTaggingPages.MERGE_SOCIAL_ACCOUNT);
    }
    @Test
    public void testForGotPassword() throws Exception {
        assertEquals("registration:forgotpassword",AppTaggingPages.FORGOT_PASSWORD);
    }
    @Test
    public void testPhilipsAccount() throws Exception {
        assertEquals("registration:philipsannouncement",AppTaggingPages.PHILIPS_ANNOUNCEMENT);
    }
    @Test
    public void testFacbook() throws Exception {
        assertEquals("registration:facebook",AppTaggingPages.FACEBOOK);
    }
    @Test
    public void testGooglePluse() throws Exception {
        assertEquals("registration:googleplus",AppTaggingPages.GOOGLE_PLUS);
    }
    @Test
    public void testTwitter() throws Exception {
        assertEquals("registration:twitter",AppTaggingPages.TWITTER);
    }



}