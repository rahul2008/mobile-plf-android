/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib;

import com.philips.cdp.cloudcontrollerapi.BuildConfig;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class BuildConfigTest {

    @Test
    public void containsTla() throws Exception {
        assertEquals("cla", BuildConfig.TLA);
    }
}
