package com.philips.cdp.registration.hsdp;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.configuration.HSDPInfo;

import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 9/6/2016.
 */
public class HsdpUserTest extends InstrumentationTestCase {

    HsdpUser mHsdpUser;

    Context context;
    @Before
    public void setUp() throws Exception {
        // Necessary to get Mockito framework working
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        context = getInstrumentation().getTargetContext();
        mHsdpUser = new HsdpUser(context);

    }
    public void testHsdpUser(){
        assertNotNull(mHsdpUser);
        assertFalse(mHsdpUser.isHsdpUserSignedIn());
    }
}