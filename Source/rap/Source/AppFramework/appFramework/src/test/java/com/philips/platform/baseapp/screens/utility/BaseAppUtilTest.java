/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.utility;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(CustomRobolectricRunner.class)
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
