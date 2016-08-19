package com.philips.dhpclient.util;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/19/2016.
 */
public class MapUtilsTest extends InstrumentationTestCase{


    @Before
    public void setUp() throws Exception {
        // Necessary to get Mockito framework working
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
    }
    @Test
    public void testExtract() throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a","aaaa");
        assertNull(MapUtils.extract(map,"helo"));

    }
}