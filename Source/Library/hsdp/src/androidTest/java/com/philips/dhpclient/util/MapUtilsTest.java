//package com.philips.dhpclient.util;
//
//import android.test.InstrumentationTestCase;
//
//import com.philips.dhpclient.HSDPInstrumentationBase;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static junit.framework.Assert.assertNull;
//import static org.junit.Assert.*;
//
//
//public class MapUtilsTest extends HSDPInstrumentationBase {
//
//
//    @Before
//    public void setUp() throws Exception {
//
//        super.setUp();
//    }
//    @Test
//    public void testExtract() throws Exception {
//
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("a","aaaa");
//        assertNull(MapUtils.extract(map,"helo"));
//
//    }
//}