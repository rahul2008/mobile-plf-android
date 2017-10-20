/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.models;

import android.content.Context;
import android.support.graphics.drawable.VectorDrawableCompat;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.appframework.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(CustomRobolectricRunner.class)
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