package com.philips.cdp.registration.ui.utils;

import android.content.Intent;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.settings.RegistrationHelper;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class NetworkStateReceiverTest extends InstrumentationTestCase {

    NetworkStateReceiver networkStateReceiver;

    @Before
    public void setUp() throws Exception {
        networkStateReceiver = new NetworkStateReceiver();
    }

    @Test
    public void testAssert() {
        assertNotNull(networkStateReceiver);
    }

    @Test
    public void testOnRecive(){
        Intent intent= new Intent();
        networkStateReceiver.onReceive(getInstrumentation().getContext(),intent);
        boolean isOnline = NetworkUtility.isNetworkAvailable(getInstrumentation().getContext());
        assertNotNull(isOnline);
        assertNotNull(RegistrationHelper.getInstance().getNetworkStateListener());
    }

}
