/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

/**
 * Created by 310202337 on 11/27/2015.
 */
public class DIUserProfileTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    DIUserProfile diUserProfile = new DIUserProfile();

    public DIUserProfileTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());


    }

    public void test_getHsdpUUID(){
        diUserProfile.setHsdpUUID("TestUUID");
        assertEquals("TestUUID", diUserProfile.getHsdpUUID());
    }

    public void test_getHsdpAccessToken(){
        diUserProfile.setHsdpAccessToken("TestHsdpToken");
        assertEquals("TestHsdpToken", diUserProfile.getHsdpAccessToken());
    }

    public void test_getLanguageCode(){
        diUserProfile.setLanguageCode("en");
        assertEquals("en", diUserProfile.getLanguageCode());
    }

    public void test_getCountryCode(){
        diUserProfile.setCountryCode("US");
        assertEquals("US", diUserProfile.getCountryCode());
    }

    public void test_getEmail(){
        diUserProfile.setEmail("test@test.com");
        assertEquals("test@test.com",diUserProfile.getEmail());
    }

    public void test_getPassword(){
        diUserProfile.setPassword("@#$%^");
        assertEquals("@#$%^",diUserProfile.getPassword());
    }

    public void test_getGivenName(){
        diUserProfile.setGivenName("TestName");
        assertEquals("TestName",diUserProfile.getGivenName());
    }

    public void test_getOlderThanAgeLimit(){
        diUserProfile.setOlderThanAgeLimit(true);
        assertEquals(true,diUserProfile.getOlderThanAgeLimit());
    }

    public void test_getReceiveMarketingEmail(){
        diUserProfile.setReceiveMarketingEmail(true);
        assertEquals(true,diUserProfile.getReceiveMarketingEmail());
    }

    public void test_getDisplayName(){
        diUserProfile.setDisplayName("TestDisplayName");
        assertEquals("TestDisplayName",diUserProfile.getDisplayName());
    }

    public void test_getFamilyName(){
        diUserProfile.setFamilyName("TestFamilyName");
        assertEquals("TestFamilyName", diUserProfile.getFamilyName());
    }

    public void test_getJanrainUUID(){
        diUserProfile.setJanrainUUID("TestJanrainID");
        assertEquals("TestJanrainID",diUserProfile.getJanrainUUID());
    }

}
