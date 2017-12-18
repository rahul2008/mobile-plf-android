/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class TextUtilTest {

    @Test
    public void itShouldVerifyIsEmpty() throws Exception {
        assertEquals(true,TextUtil.isEmpty(null));
    }

}