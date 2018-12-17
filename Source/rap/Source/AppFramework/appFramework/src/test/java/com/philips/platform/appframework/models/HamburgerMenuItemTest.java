/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.appframework.models;

import android.content.Context;

import com.philips.platform.appframework.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class HamburgerMenuItemTest {


    private HamburgerMenuItem hamburgerMenuItem;

    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application.getApplicationContext();
        hamburgerMenuItem = new HamburgerMenuItem(R.drawable.rap_home, context.getString(R.string.RA_DLS_HomeScreen_Title), context);
    }

    @Test
    public void getIconTest() {
        assertNotNull(hamburgerMenuItem.getIcon());
    }

    @Test
    public void getTitleTest() {
        assertEquals(hamburgerMenuItem.getTitle(), context.getString(R.string.RA_DLS_HomeScreen_Title));
    }

    @After
    public void tearDown() {
        hamburgerMenuItem = null;
        context = null;
    }

}