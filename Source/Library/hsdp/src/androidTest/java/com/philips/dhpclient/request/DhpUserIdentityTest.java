package com.philips.dhpclient.request;

import android.test.InstrumentationTestCase;

import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class DhpUserIdentityTest extends InstrumentationTestCase {

    DhpUserIdentity mDhpUserIdentity ;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        DhpUserIdentity.Address primaryAddress = new DhpUserIdentity.Address("country");
        List<DhpUserIdentity.Photo> photos = new ArrayList<DhpUserIdentity.Photo>();
        double height =12234;
        double weight=12344;
        DhpUserIdentity.Profile profile = new DhpUserIdentity.Profile("givenName","middleName","familyName","birthday","currentLocation","displayName",
                "locale","gender","timeZone","preferredLanguage",height,weight,primaryAddress,photos);
        mDhpUserIdentity = new DhpUserIdentity("loginId","password", profile);
    }

    public void testDhpUserIdentitty(){
        assertNotNull(mDhpUserIdentity);
    }
}