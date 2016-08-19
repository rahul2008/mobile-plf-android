package com.philips.cdp.registration.events;

import android.content.Context;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/18/2016.
 */
public class NetworkStateHelperTest extends InstrumentationTestCase{
    @Mock
    NetworkStateHelper mNetworkStateHelper;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        // Necessary to get Mockito framework working
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();

        assertNotNull(mNetworkStateHelper.getInstance());

        mNetworkStateHelper = mNetworkStateHelper.getInstance();
        context = getInstrumentation().getTargetContext();

    }
    @Test
    public void testGetInstance() throws Exception {
        assertNotNull(mNetworkStateHelper);
        mNetworkStateHelper.getInstance();
    }
}