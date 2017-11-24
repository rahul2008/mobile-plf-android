/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.util;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;


public class TextUtilTest {

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void itShouldVerifyIsEmpty() throws Exception {
        assertEquals(true,TextUtil.isEmpty(null));
    }

}