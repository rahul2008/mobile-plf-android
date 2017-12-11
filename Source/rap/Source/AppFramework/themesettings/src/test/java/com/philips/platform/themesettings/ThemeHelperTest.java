/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.themesettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.TestCase.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ThemeHelperTest {

    private ThemeHelper themeHelper;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        themeHelper = new ThemeHelper(sharedPreferences, context);
    }

    @Test
    public void testInitNavigationRange() {
        assertEquals(NavigationColor.BRIGHT.name(), themeHelper.initNavigationRange().name());
    }

    @Test
    public void testInitColorRange() {
        assertEquals(ColorRange.GROUP_BLUE.name(), themeHelper.initColorRange().name());
    }

    @Test
    public void testInitContentTonalRange() {
        assertEquals(ContentColor.ULTRA_LIGHT.name(), themeHelper.initContentTonalRange().name());
    }

    @Test
    public void testInitAccentRange() {
        assertEquals(AccentRange.ORANGE.name(), themeHelper.initAccentRange().name());
    }

    @After
    public void tearDown() {

    }
}