package com.philips.platform.catalogapp.themesettings;

import android.app.Application;

import com.philips.platform.catalogapp.BuildConfig;
import com.philips.platform.catalogapp.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class ThemeColorHelperTest {
    ThemeColorHelper themeColorHelper;

    @Before
    public void setUp() throws Exception {
        themeColorHelper = new ThemeColorHelper();
    }

    @Test
    public void ShouldReturnEightColorsWhenGetColorRangeArrayIsCalled() throws Exception {
        final int[] colorRangeArray = themeColorHelper.getColorRangeArray();

        assertEquals(8, colorRangeArray.length);
        assertEquals(colorRangeArray[0], R.color.uit_group_blue_level_45);
        assertEquals(colorRangeArray[1], R.color.uit_blue_level_45);
        assertEquals(colorRangeArray[2], R.color.uit_aqua_level_45);
        assertEquals(colorRangeArray[3], R.color.uit_green_level_45);
        assertEquals(colorRangeArray[4], R.color.uit_orange_level_45);
        assertEquals(colorRangeArray[5], R.color.uit_pink_level_45);
        assertEquals(colorRangeArray[6], R.color.uit_purple_level_45);
        assertEquals(colorRangeArray[7], R.color.uit_gray_level_45);
    }

    @Test
    public void ShouldReturnContentTonalRangesWhenGetContentTonalColorsIsCalled() throws Exception {
        final Application application = RuntimeEnvironment.application;
        final int[] colorsArray = themeColorHelper.getContentColorsArray(application.getResources(), "group_blue", application.getPackageName());

        assertEquals(5, colorsArray.length);
        assertEquals(colorsArray[0], R.color.uit_group_blue_level_80);
        assertEquals(colorsArray[1], R.color.uit_group_blue_level_50);
        assertEquals(colorsArray[2], R.color.uit_group_blue_level_35);
        assertEquals(colorsArray[3], R.color.uit_group_blue_level_20);
        assertEquals(colorsArray[4], R.color.uitColorWhite);
    }
}