/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.uid.components.dotnavigation;

import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.thememanager.NavigationColor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class DotNavigationIndicatorTest extends BaseTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    BaseTestActivity activity;
    private Resources testResources;

    @Before
    public void setUp() throws Exception {
        activity = mActivityTestRule.launchActivity(getLaunchIntent(NavigationColor.ULTRA_LIGHT.ordinal()));
    }

    @Test
    public void verifyDotsAreNotDrawnWhenOnlyOneItemIsSuppliedToAdapter() throws Exception {

    }

    @Test
    public void verifyLetMarginBetweenDots() throws Exception {

    }

    @Test
    public void verifyRightMarginBetweenDots() throws Exception {

    }

    @Test
    public void verifyDotsAreDrawnAtCenter() throws Exception {

    }

    @Test
    public void verifyUnselectedCircleColorBasedOnSuppliedTheme() throws Exception {

    }

    @Test
    public void verifySelectedCircleColorBasedOnSupploedTheme() throws Exception {

    }

    @Test
    public void verifyClickOnRightOfSelectedDotGivesCallbackToShowNext() throws Exception {

    }

    @Test
    public void verifyClickOnLeftOfSelectedDotGivesCallbackToShowPrevious() throws Exception {

    }

    @Test
    public void verifyRadiusOfDot() throws Exception {

    }

    @Test
    public void verifyWidthOfDot() throws Exception {

    }

    @Test
    public void verifyHeightOfDot() throws Exception {

    }

    @Test
    public void verifyDotNavigationContainerHeight() throws Exception {

    }
}