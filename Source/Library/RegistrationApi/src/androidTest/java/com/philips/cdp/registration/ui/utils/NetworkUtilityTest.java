package com.philips.cdp.registration.ui.utils;

import android.content.Context;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/18/2016.
 */
public class NetworkUtilityTest extends InstrumentationTestCase {

    @Mock
    NetworkUtility networkUtility;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        context = getInstrumentation().getTargetContext();
        networkUtility = new NetworkUtility();
    }

    @Test
    public void testIsNetworkAvailable() throws Exception {
        NetworkUtility.isNetworkAvailable(context);
    }
}