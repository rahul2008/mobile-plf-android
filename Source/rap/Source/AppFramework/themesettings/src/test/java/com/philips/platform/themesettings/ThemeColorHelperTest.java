/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.themesettings;

import android.content.Context;

import com.philips.platform.uid.thememanager.ColorRange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ThemeColorHelperTest {

    Context context;
    private ThemeColorHelper themeColorHelper;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application.getApplicationContext();
        themeColorHelper = new ThemeColorHelper();
    }

    @Test
    public void testGetColorRangeItemsList() {
        List<ColorModel> colorModelList = themeColorHelper.getColorRangeItemsList();
        assertEquals(8, colorModelList.size());
    }

    @Test
    public void testGetAccentColorsList() {
        List<ColorModel> accentColorsList = themeColorHelper.getAccentColorsList(ColorRange.PINK, context.getResources(), context.getPackageName());
        assertEquals(2, accentColorsList.size());
    }

    @Test
    public void testGetShortNameForGreen() {
        assertEquals("GR" ,themeColorHelper.getShortName(ColorRange.GREEN.name()));
    }

    @Test
    public void testGetShortNameForGroupBlue() {
        assertEquals("GB" ,themeColorHelper.getShortName(ColorRange.GROUP_BLUE.name()));
    }

    @Test
    public void testGetShortNameForPurple() {
        assertEquals("Pu" ,themeColorHelper.getShortName(ColorRange.PURPLE.name()));
    }

    @Test
    public void testGetNavigationColorModelsList() {
        List<ColorModel> navigationColorModelsList = themeColorHelper.getNavigationColorModelsList(ColorRange.GROUP_BLUE, context);
        assertEquals(4, navigationColorModelsList.size());
    }

    @Test
    public void testGetContentColorModelList() {
        List<ColorModel> contentColorModelsList = themeColorHelper.getContentColorModelList(ColorRange.GROUP_BLUE, context);
        assertEquals(4, contentColorModelsList.size());
    }

    @After
    public void tearDown() {
        themeColorHelper = null;
    }

}