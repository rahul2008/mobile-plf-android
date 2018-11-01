/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.testutil;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
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
