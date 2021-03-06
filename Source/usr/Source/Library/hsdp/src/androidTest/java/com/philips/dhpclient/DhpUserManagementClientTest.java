
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient;

import com.philips.dhpclient.request.DhpUserIdentity;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by 310243576 on 8/21/2016.
 */
public class DhpUserManagementClientTest {

    private DhpApiClientConfiguration mDhpApiClientConfiguration;
    private DhpUserManagementClient mDhpUserManagementClient;

    @Before
    public void setUp() throws Exception {
        mDhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl", "dhpApplicationName", "signingKey", "signingSecret");
        mDhpUserManagementClient = new DhpUserManagementClient(mDhpApiClientConfiguration);
    }

    @Test
    public void testDhpUserManagementClient() throws Exception {
        DhpUserIdentity.Address primaryAddress = new DhpUserIdentity.Address("country");
        List<DhpUserIdentity.Photo> photos = new ArrayList<DhpUserIdentity.Photo>();
        double d = 123;

        DhpUserIdentity.Profile profile = new DhpUserIdentity.Profile("givenName", "middleName", "familyName", "birthday", "currentLocation", "displayName",
                "locale", "gender", "timeZone", "preferredLanguage", d, d, primaryAddress, photos);
        DhpUserIdentity dhpUserIdentity = new DhpUserIdentity("loginId", "password", profile);
        try {
            mDhpUserManagementClient.registerUser(dhpUserIdentity);
        } catch (Exception e) {
        }

        try {
            mDhpUserManagementClient.retrieveProfile("sample", "sample");
        } catch (Exception e) {
        }
        mDhpUserManagementClient.changePassword("loginId", "currentPassword", " newPassword", "accessToken");
        try {
            mDhpUserManagementClient.resetPassword("loginId");
        } catch (Exception e) {
        }
        mDhpUserManagementClient.updateProfile("userId", profile, "accessToken");
        try {
            mDhpUserManagementClient.resendConfirmation("email");
        } catch (Exception e) {
        }
        assertNotNull(mDhpApiClientConfiguration);
        assertNotNull(mDhpUserManagementClient);
    }

    @Test
    public void testGetUserInstance() {
        Method method = null;
        Double doubleVal = 234.5;
        try {
            method = DhpUserManagementClient.class.getDeclaredMethod("remapZeroOrNegativeToNull", Double.class);
            method.setAccessible(true);
            method.invoke(mDhpUserManagementClient, doubleVal);
            method.invoke(mDhpUserManagementClient, -0.1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}