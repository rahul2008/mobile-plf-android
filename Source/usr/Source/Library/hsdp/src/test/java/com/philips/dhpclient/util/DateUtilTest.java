/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient.util;

import org.junit.Test;

import static org.junit.Assert.assertNotSame;

/**
 * Created by 310243576 on 8/19/2016.
 */
public class DateUtilTest {

    @Test
    public void testGetTimestamp() throws Exception {
        assertNotSame("dd", DateUtil.getTimestamp());
    }
}
