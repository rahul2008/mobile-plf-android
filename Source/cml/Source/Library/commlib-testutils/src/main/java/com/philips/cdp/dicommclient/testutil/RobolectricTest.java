/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.testutil;

import com.philips.cdp2.commlib_testutils.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
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
}
