/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.splashscreen;


import android.app.Activity;
import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.util.DisplayMetrics;
import android.view.Gravity;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.LayoutPropertiesMatcher;
import com.philips.platform.uid.matcher.SplashScreenMatcher;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.utils.UIDUtils;
import com.philips.platform.uid.view.widget.SplashScreen;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class SplashScreenTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> activityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources resources;
    private SplashScreen splashScreen;
    private Activity activity;

    @Before
    public void setUp() {
        final BaseTestActivity baseTestActivity = activityTestRule.getActivity();
        baseTestActivity.switchTo(com.philips.platform.uid.test.R.layout.layout_splash_screen);
        resources = getInstrumentation().getContext().getResources();
        Espresso.registerIdlingResources(baseTestActivity.getIdlingResource());
        splashScreen = (SplashScreen) baseTestActivity.findViewById(com.philips.platform.uid.test.R.id.test_splash_screen);
        activity = baseTestActivity;
    }

    private ViewInteraction getSplashScreenLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_splash_screen));
    }

    private ViewInteraction getSplashScreenIcon() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_splash_screen_icon));
    }

    private ViewInteraction getSplashScreenTitle() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_splash_screen_app_name));
    }

    private ViewInteraction getSplashScreenView() {
        return onView(withId(com.philips.platform.uid.test.R.id.test_splash_screen));
    }

    @Test
    public void verifySplashScreenStartPadding() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_splash_screen_margin_horizontal);
        getSplashScreenLayout().check(matches(ViewPropertiesMatchers.isSameStartPadding(expected)));
    }

    @Test
    public void verifySplashScreenEndPadding() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_splash_screen_margin_horizontal);
        getSplashScreenLayout().check(matches(ViewPropertiesMatchers.isSameEndPadding(expected)));
    }

    @Test
    public void verifySplashScreenBackgroundColor() {
        int expected = UIDTestUtils.getAttributeColor(activity, R.attr.uidSplashScreenDefaultBackgroundColor);
        getSplashScreenView().check(matches(SplashScreenMatcher.isSameBackgroundColor(expected)));
    }


    @Test
    public void verifySplashScreenGradientStartColor() {
        int expected = UIDTestUtils.getAttributeColor(activity, R.attr.uidLightDefaultLightCenterColor);
        getSplashScreenView().check(matches(SplashScreenMatcher.isSameGradientColor(expected, SplashScreenMatcher.GRADIENT_START_COLOR)));
    }

    @Test
    public void verifySplashScreenGradientEndColor() {
        int expected = UIDTestUtils.getAttributeColor(activity, R.attr.uidLightDefaultLightEdgeColor);
        getSplashScreenView().check(matches(SplashScreenMatcher.isSameGradientColor(expected, SplashScreenMatcher.GRADIENT_END_COLOR)));
    }

    @Test
    public void verifySplashScreenGradientRadius() {
        float expected = Math.max(UIDUtils.getDeviceHeight(activity), UIDUtils.getDeviceWidth(activity)) / 2;
        getSplashScreenView().check(matches(SplashScreenMatcher.isSameGradientRadius(expected)));
    }

    @Test
    public void verifySplashScreenIconBottomPadding() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int expected = Double.valueOf(Math.max(displayMetrics.heightPixels, displayMetrics.widthPixels) * 0.146).intValue();
        getSplashScreenIcon().check(matches(ViewPropertiesMatchers.isSameBottomPadding(expected)));
    }

    @Test
    public void verifySplashScreenContentGravity() {
        getSplashScreenLayout().check(matches(LayoutPropertiesMatcher.isSameGravity(Gravity.CENTER)));
    }

    @Test
    public void verifySplashScreenTitleHeight() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_splash_screen_app_name_size);
        getSplashScreenTitle().check(matches(TextViewPropertiesMatchers.isSameFontSize(expected)));
    }

    @Test
    public void verifySplashScreenTitleTypeface() {
        getSplashScreenTitle().check(matches(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_LIGHT)));
    }

    @Test
    public void verifySplashScreenTitleColor() {
        int expected = UIDTestUtils.getAttributeColor(activity, R.attr.uidSplashScreenDefaultTextColor);
        getSplashScreenTitle().check(matches(TextViewPropertiesMatchers.isSameTextColor(expected)));
    }

    @Test
    public void verifySplashScreenSetAppNameWithCharResId() {
        final String text = "Catalog App DLS";
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                splashScreen.setAppName(com.philips.platform.uid.test.R.string.catalog_app_name);
            }
        });
        getSplashScreenTitle().check(matches(TextViewPropertiesMatchers.hasSameText(text)));
    }

    @Test
    public void verifySplashScreenSetAppNameWithCharSeq() {
        final String text = "Catalog App DLS";
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                splashScreen.setAppName(text);
            }
        });
        getSplashScreenTitle().check(matches(TextViewPropertiesMatchers.hasSameText(resources.getText(com.philips.platform.uid.test.R.string.catalog_app_name))));
    }

    @Test
    public void verifySplashScreenSetAppNameLineSpacing() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_splash_screen_app_name_spacing);
        getSplashScreenTitle().check(matches(TextViewPropertiesMatchers.isSameLineSpacing(expected)));
    }

}
