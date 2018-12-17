
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient.request;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class DhpUserIdentityTest {

    @Mock
    private DhpUserIdentity mDhpUserIdentity;

    @Mock
    private DhpUserIdentity.Address primaryAddress;

    @Mock
    private Map<String, Object> mRawResponse;

    @Mock
    private List<DhpUserIdentity.Photo> photos;

    @Mock
    private DhpUserIdentity.Profile profile;

    @Mock
    private DhpUserIdentity.Profile profile1;

    private double height = 12234;

    private double weight = 12344;

    @Before
    public void setUp() throws Exception {
        primaryAddress = new DhpUserIdentity.Address("country");
        photos = new ArrayList<>();

        profile = new DhpUserIdentity.Profile("givenName", "middleName", "familyName", "birthday", "currentLocation", "displayName",
                "locale", "gender", "timeZone", "preferredLanguage", height, weight, primaryAddress, photos);
        mDhpUserIdentity = new DhpUserIdentity("loginId", "password", profile);
        mRawResponse = new HashMap<>();
    }

    @Test
    public void testDhpUserIdentity() {
        assertNotNull(mDhpUserIdentity);
        DhpUserIdentity mDhpUserIdentity1 = new DhpUserIdentity("loginId", "password", profile);

        assertEquals(mDhpUserIdentity, mDhpUserIdentity1);
        assertEquals(mDhpUserIdentity, mDhpUserIdentity);
        assertNotEquals(mDhpUserIdentity, mRawResponse);
        assertNotNull(mDhpUserIdentity.toString());
    }

    @Test
    public void testAddress() {
        DhpUserIdentity.Address primaryAddress1 = new DhpUserIdentity.Address("country");
        assertEquals(primaryAddress, primaryAddress);
        assertEquals(primaryAddress, primaryAddress1);
        assertNotEquals(primaryAddress, mRawResponse);
        assertNotNull(primaryAddress.toString());
    }

    @Test
    public void testPhoto() {
        DhpUserIdentity.Photo photo;
        DhpUserIdentity.Photo photo1;
        photo = new DhpUserIdentity.Photo("type", "value");
        photo1 = new DhpUserIdentity.Photo("type", "value");

        assertEquals(photo, photo);
        assertEquals(photo, photo1);
        assertNotEquals(photo, mRawResponse);
        assertNotNull(photo.toString());
    }

    @Test
    public void testProfile() {
        profile1 = new DhpUserIdentity.Profile("givenName", "middleName", "familyName", "birthday", "currentLocation", "displayName",
                "locale", "gender", "timeZone", "preferredLanguage", height, weight, primaryAddress, photos);
        assertEquals(profile, profile);
        assertEquals(profile, profile1);
        assertNotEquals(profile, mRawResponse);
        assertNotNull(profile.toString());
    }
}
