//package com.philips.dhpclient.util;
//
//import android.test.InstrumentationTestCase;
//
//import com.philips.dhpclient.HSDPInstrumentationBase;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import static junit.framework.Assert.assertFalse;
//import static junit.framework.Assert.assertNotSame;
//import static junit.framework.Assert.assertTrue;
//import static org.junit.Assert.*;
//
//
//public class ObjectsTest extends HSDPInstrumentationBase {
//
//    @Mock
//    Objects objects;
//
//    @Before
//    public void setUp() throws Exception {
//
//        super.setUp();
//        objects = new Objects();
//    }
//
//    @Test
//    public void testHash() throws Exception {
//
//        Map<String, String> a = new HashMap<String,String>();
//
//
//        assertNotSame(a.hashCode(),objects.hash(a));
//
//    }
//
//    @Test
//    public void testEquals() throws Exception {
//        Map<String, String> a = new HashMap<String,String>();
//        Map<String, String> b = new HashMap<String,String>();
//
//        assertTrue(objects.equals(a, b));
//        a =null;
//        assertFalse(objects.equals(a, b));
//        b= null;
//        assertTrue(objects.equals(a, b));
//
//    }
//}