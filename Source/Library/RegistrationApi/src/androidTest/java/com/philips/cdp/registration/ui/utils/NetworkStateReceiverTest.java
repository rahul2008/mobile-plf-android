package com.philips.cdp.registration.ui.utils;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class NetworkStateReceiverTest extends InstrumentationTestCase {

    NetworkStateReceiver networkStateReceiver;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        networkStateReceiver = new NetworkStateReceiver();
    }





}
