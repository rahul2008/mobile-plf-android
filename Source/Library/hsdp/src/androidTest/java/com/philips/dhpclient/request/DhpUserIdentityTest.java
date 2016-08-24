package com.philips.dhpclient.request;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class DhpUserIdentityTest extends InstrumentationTestCase {

    DhpUserIdentity mDhpUserIdentity ;
    DhpUserIdentity.Address primaryAddress;
    Map<String,Object> mRawResponse;
    List<DhpUserIdentity.Photo> photos;
    DhpUserIdentity.Profile profile;
    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
         primaryAddress = new DhpUserIdentity.Address("country");
         photos = new ArrayList<DhpUserIdentity.Photo>();
        double height =12234;
        double weight=12344;
        profile = new DhpUserIdentity.Profile("givenName","middleName","familyName","birthday","currentLocation","displayName",
                "locale","gender","timeZone","preferredLanguage",height,weight,primaryAddress,photos);
        mDhpUserIdentity = new DhpUserIdentity("loginId","password", profile);
        mRawResponse = new HashMap<String,Object>();
    }

    @Test
    public void testDhpUserIdentitty(){
        assertNotNull(mDhpUserIdentity);

        mDhpUserIdentity.equals(mRawResponse);
        assertNotNull(mDhpUserIdentity.hashCode());
        assertNotNull(mDhpUserIdentity.toString());


    }
    @Test
    public void testAddress()
    {
        assertFalse(primaryAddress.equals(mRawResponse));
        assertNotNull(primaryAddress.hashCode());
        assertNotNull(primaryAddress.toString());
    }

    @Test
    public void testPhoto()
    {
        assertFalse(photos.equals(mRawResponse));
        assertNotNull(photos.hashCode());
        assertNotNull(photos.toString());
    }

    @Test
    public void testProfile()
    {
        assertFalse(profile.equals(mRawResponse));
        assertNotNull(profile.hashCode());
        assertNotNull(profile.toString());
    }

}