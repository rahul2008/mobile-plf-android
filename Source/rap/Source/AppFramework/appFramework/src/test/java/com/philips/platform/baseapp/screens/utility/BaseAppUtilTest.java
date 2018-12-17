/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.baseapp.screens.utility;

import com.philips.platform.TestAppFrameworkApplication;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class BaseAppUtilTest extends TestCase {
    BaseAppUtil baseAppUtil;

    @Before
    public void setUp() throws Exception{
        baseAppUtil = new BaseAppUtil();
    }


    @Test
    public void testIsNetworkAvailable(){
        assertTrue(BaseAppUtil.isNetworkAvailable(RuntimeEnvironment.application));
    }
}
