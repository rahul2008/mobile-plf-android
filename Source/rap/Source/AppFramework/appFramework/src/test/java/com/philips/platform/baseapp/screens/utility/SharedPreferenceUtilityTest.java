/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.baseapp.screens.utility;

import android.content.Context;

import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class SharedPreferenceUtilityTest extends TestCase {
    private Context context;
    private SharedPreferenceUtility sharedPreferenceUtility;

    @Before
    public void setUp() throws Exception{
        super.setUp();
        context = Robolectric.buildActivity(TestActivity.class).create().start().get().getApplicationContext();
        sharedPreferenceUtility = new SharedPreferenceUtility(context);
    }

    @Test
    public void testGetPreferenceObject(){
        assertNotNull(sharedPreferenceUtility.getMyPreferences());
    }

    @Test
    public void testGetPreferenceString(){
        sharedPreferenceUtility.writePreferenceString("TestString","TestValue");
        assertEquals("TestValue",sharedPreferenceUtility.getPreferenceString("TestString"));
    }

    @Test
    public void testGetPreferenceBoolean(){
        sharedPreferenceUtility.writePreferenceBoolean("TestBoolean",true);
        assertEquals(true,sharedPreferenceUtility.getPreferenceBoolean("TestBoolean"));
    }

    @Test
    public void testGetPreferenceInt(){
        sharedPreferenceUtility.writePreferenceInt("TestBoolean",100);
        assertEquals(100,sharedPreferenceUtility.getPreferenceInt("TestBoolean"));
    }

    @Test
    public void testContains(){
        sharedPreferenceUtility.writePreferenceString("TestContains","TestContains");
        assertEquals(true,sharedPreferenceUtility.contains("TestContains"));
    }
}
