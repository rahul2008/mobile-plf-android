/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DIUserProfileTest extends TestCase {

    private DIUserProfile diUserProfile;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        diUserProfile = new DIUserProfile();
    }

    @Test
    public void test_getHsdpUUID() {
        diUserProfile.setHsdpUUID("TestUUID");
        assertEquals("TestUUID", diUserProfile.getHsdpUUID());
    }

    @Test
    public void test_getHsdpAccessToken() {
        diUserProfile.setHsdpAccessToken("TestHsdpToken");
        assertEquals("TestHsdpToken", diUserProfile.getHsdpAccessToken());
    }

    @Test
    public void test_getLanguageCode() {
        diUserProfile.setLanguageCode("en");
        assertEquals("en", diUserProfile.getLanguageCode());
    }

    @Test
    public void test_getCountryCode() {
        diUserProfile.setCountryCode("US");
        assertEquals("US", diUserProfile.getCountryCode());
    }

    @Test
    public void test_getEmail() {
        diUserProfile.setEmail("test@test.com");
        assertEquals("test@test.com", diUserProfile.getEmail());
    }

    @Test
    public void test_getPassword() {
        diUserProfile.setPassword("@#$%^");
        assertEquals("@#$%^", diUserProfile.getPassword());
    }

    @Test
    public void test_getGivenName() {
        diUserProfile.setGivenName("TestName");
        assertEquals("TestName", diUserProfile.getGivenName());
    }

    @Test
    public void test_getOlderThanAgeLimit() {
        diUserProfile.setOlderThanAgeLimit(true);
        assertEquals(true, diUserProfile.getOlderThanAgeLimit());
    }

    @Test
    public void test_getReceiveMarketingEmail() {
        diUserProfile.setReceiveMarketingEmail(true);
        assertEquals(true, diUserProfile.getReceiveMarketingEmail());
    }

    @Test
    public void test_getDisplayName() {
        diUserProfile.setDisplayName("TestDisplayName");
        assertEquals("TestDisplayName", diUserProfile.getDisplayName());
    }

    @Test
    public void test_getFamilyName() {
        diUserProfile.setFamilyName("TestFamilyName");
        assertEquals("TestFamilyName", diUserProfile.getFamilyName());
    }

    @Test
    public void test_getJanrainUUID() {
        diUserProfile.setJanrainUUID("TestJanrainID");
        assertEquals("TestJanrainID", diUserProfile.getJanrainUUID());
    }

}
