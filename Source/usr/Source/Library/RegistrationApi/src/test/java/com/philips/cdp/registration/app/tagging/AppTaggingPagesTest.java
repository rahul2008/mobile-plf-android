package com.philips.cdp.registration.app.tagging;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AppTaggingPagesTest extends TestCase {

    private AppTaggingPages appTaggingPages;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        appTaggingPages = new AppTaggingPages();
    }

    @Test
    public void testHome() {
        assertEquals("registration:home", AppTaggingPages.HOME);
    }

    @Test
    public void testCreatAccount() {
        assertEquals("registration:createaccount", AppTaggingPages.CREATE_ACCOUNT);

    }
    @Test
    public void testSignIn() {
        assertEquals("registration:signin",AppTaggingPages.SIGN_IN_ACCOUNT);
    }
    @Test
    public void testAccountActivaton() {
        assertEquals("registration:accountactivation",AppTaggingPages.ACCOUNT_ACTIVATION);
    }

    @Test
    public void testAlomastDone() {
        assertEquals("registration:almostdone",AppTaggingPages.ALMOST_DONE);
    }
    @Test
    public void testMergeAccount() {
        assertEquals("registration:mergeaccount",AppTaggingPages.MERGE_ACCOUNT);
    }
    @Test
    public void testUserProfile() {
        assertEquals("registration:userprofile",AppTaggingPages.USER_PROFILE);
    }


    @Test
    public void testMergeSocialAccount() {
        assertEquals("registration:mergesocialaccount",AppTaggingPages.MERGE_SOCIAL_ACCOUNT);
    }
    @Test
    public void testForGotPassword() {
        assertEquals("registration:forgotpassword",AppTaggingPages.FORGOT_PASSWORD);
    }
    @Test
    public void testPhilipsAccount() {
        assertEquals("registration:philipsannouncement",AppTaggingPages.PHILIPS_ANNOUNCEMENT);
    }
    @Test
    public void testFacbook() {
        assertEquals("registration:facebook",AppTaggingPages.FACEBOOK);
    }
    @Test
    public void testGooglePluse() {
        assertEquals("registration:googleplus",AppTaggingPages.GOOGLE_PLUS);
    }
    @Test
    public void testTwitter() {
        assertEquals("registration:twitter",AppTaggingPages.TWITTER);
    }



}