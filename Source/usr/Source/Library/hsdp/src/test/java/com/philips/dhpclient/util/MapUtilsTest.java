
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.dhpclient.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNull;

/**
 * Created by 310243576 on 8/19/2016.
 */
public class MapUtilsTest {

    @Test
    public void testExtract() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", "aaaa");

        assertNull(MapUtils.extract(map, "helo"));
    }
}
