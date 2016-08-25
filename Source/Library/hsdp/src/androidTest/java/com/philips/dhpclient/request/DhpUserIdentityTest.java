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
    DhpUserIdentity.Profile profile1;
    double height =12234;
    double weight=12344;
    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
         primaryAddress = new DhpUserIdentity.Address("country");
         photos = new ArrayList<DhpUserIdentity.Photo>();



        profile = new DhpUserIdentity.Profile("givenName","middleName","familyName","birthday","currentLocation","displayName",
                "locale","gender","timeZone","preferredLanguage",height,weight,primaryAddress,photos);
        mDhpUserIdentity = new DhpUserIdentity("loginId","password", profile);
        mRawResponse = new HashMap<String,Object>();
    }

    @Test
    public void testDhpUserIdentitty(){
        assertNotNull(mDhpUserIdentity);
        assertTrue(mDhpUserIdentity.equals(mDhpUserIdentity));
        assertFalse(mDhpUserIdentity.equals(mRawResponse));
        assertFalse(mDhpUserIdentity.equals(null));
        assertNotNull(mDhpUserIdentity.hashCode());
        assertNotNull(mDhpUserIdentity.toString());


    }
    @Test
    public void testAddress()
    {
        DhpUserIdentity.Address primaryAddress1 = new DhpUserIdentity.Address("country");
        assertTrue(primaryAddress.equals(primaryAddress));
        assertTrue(primaryAddress.equals(primaryAddress1));
        assertFalse(primaryAddress.equals(mRawResponse));
        assertFalse(primaryAddress.equals(null));
        assertNotNull(primaryAddress.hashCode());
        assertNotNull(primaryAddress.toString());
    }

    @Test
    public void testPhoto()
    {
        DhpUserIdentity.Photo photo;
        DhpUserIdentity.Photo photo1;
        photo = new  DhpUserIdentity.Photo("type", "value");
        photo1 = new  DhpUserIdentity.Photo("type", "value");

        assertTrue(photo.equals(photo));
        assertTrue(photo.equals(photo1));
        assertFalse(photo.equals(mRawResponse));
        assertFalse(photo.equals(null));
        assertNotNull(photo.hashCode());
        assertNotNull(photo.toString());
    }

    @Test
    public void testProfile()
    {
        profile1 = new DhpUserIdentity.Profile("givenName","middleName","familyName","birthday","currentLocation","displayName",
                "locale","gender","timeZone","preferredLanguage",height,weight,primaryAddress,photos);
        assertTrue(profile.equals(profile));
        assertTrue(profile.equals(profile1));
        assertFalse(profile.equals(mRawResponse));
        assertFalse(profile.equals(null));
        assertNotNull(profile.hashCode());
        assertNotNull(profile.toString());
    }

}