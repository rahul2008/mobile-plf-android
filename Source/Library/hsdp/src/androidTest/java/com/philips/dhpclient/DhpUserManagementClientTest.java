package com.philips.dhpclient;

import android.test.InstrumentationTestCase;

import com.philips.dhpclient.request.DhpUserIdentity;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/21/2016.
 */
public class DhpUserManagementClientTest extends InstrumentationTestCase{
    DhpApiClientConfiguration mDhpApiClientConfiguration;
    DhpUserManagementClient mDhpUserManagementClient;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();

        mDhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl","dhpApplicationName","signingKey","signingSecret");
    }

    @Test
    public void testDhpUserManagementClient(){
        mDhpUserManagementClient = new DhpUserManagementClient(mDhpApiClientConfiguration);
        DhpUserIdentity.Address primaryAddress = new DhpUserIdentity.Address("country");
        List<DhpUserIdentity.Photo> photos = new ArrayList<DhpUserIdentity.Photo>();
        double d = 123;

        DhpUserIdentity.Profile profile = new DhpUserIdentity.Profile("givenName", "middleName", "familyName", "birthday", "currentLocation","displayName",
                "locale","gender","timeZone","preferredLanguage",d,d, primaryAddress, photos);
        DhpUserIdentity dhpUserIdentity = new DhpUserIdentity("loginId","password",profile);
//        mDhpUserManagementClient.registerUser(dhpUserIdentity);

//        mDhpUserManagementClient.retrieveProfile("sample","sample");
        mDhpUserManagementClient.changePassword("loginId", "currentPassword"," newPassword","accessToken");
//        mDhpUserManagementClient.resetPassword("loginId");
        mDhpUserManagementClient.updateProfile("userId", profile,"accessToken");
//        mDhpUserManagementClient.resendConfirmation("email") ;
        assertNotNull(mDhpApiClientConfiguration);
        assertNotNull(mDhpUserManagementClient);

    }
}