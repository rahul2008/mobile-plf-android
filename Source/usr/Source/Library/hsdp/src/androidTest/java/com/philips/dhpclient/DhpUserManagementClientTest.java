
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient;

import android.support.multidex.MultiDex;

import com.philips.dhpclient.request.DhpUserIdentity;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

/**
 * Created by 310243576 on 8/21/2016.
 */
public class DhpUserManagementClientTest {

    private DhpApiClientConfiguration mDhpApiClientConfiguration;
    private DhpUserManagementClient mDhpUserManagementClient;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

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
        mDhpUserManagementClient.registerUser(dhpUserIdentity);
        mDhpUserManagementClient.retrieveProfile("sample", "sample");
        mDhpUserManagementClient.changePassword("loginId", "currentPassword", " newPassword", "accessToken");
        mDhpUserManagementClient.resetPassword("loginId");
        mDhpUserManagementClient.updateProfile("userId", profile, "accessToken");
        mDhpUserManagementClient.resendConfirmation("email");

        assertNotNull(mDhpApiClientConfiguration);
        assertNotNull(mDhpUserManagementClient);
    }

    @Test
    public void testGetUserInstance() throws Exception {
        Method method = null;
        Double doubleVal = 234.5;

        method = DhpUserManagementClient.class.getDeclaredMethod("remapZeroOrNegativeToNull", Double.class);
        method.setAccessible(true);
        method.invoke(mDhpUserManagementClient, doubleVal);
        method.invoke(mDhpUserManagementClient, -0.1);
    }
}