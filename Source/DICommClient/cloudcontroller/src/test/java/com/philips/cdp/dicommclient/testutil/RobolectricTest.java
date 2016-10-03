/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.testutil;


import com.philips.cdp.cloudcontroller.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public abstract class RobolectricTest {

    @Before
    public void before() throws Exception {
        setUp();
    }

    @After
    public void after() throws Exception {
        tearDown();

    }

    protected void setUp() throws Exception {
        initMocks(this);
    }

    protected void tearDown() throws Exception {

    }

//    @Override
//    protected void setUp() throws Exception {
//         Necessary to get Mockito framework working
//        System.setProperty("dexmaker.dexcache", RuntimeEnvironment.application.getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
//        super.setUp();
//    }
}
