package com.philips.dhpclient.util;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/19/2016.
 */
public class ObjectsTest extends InstrumentationTestCase{

    @Mock
    Objects objects;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        objects = new Objects();
    }

    @Test
    public void testHash() throws Exception {

        Map<String, String> a = new HashMap<String,String>();


        assertNotEquals(a.hashCode(),objects.hash(a));

    }

    @Test
    public void testEquals() throws Exception {
        Map<String, String> a = new HashMap<String,String>();
        Map<String, String> b = new HashMap<String,String>();

        assertTrue(objects.equals(a, b));
        a =null;
        assertFalse(objects.equals(a, b));
        b= null;
        assertTrue(objects.equals(a, b));

    }
}