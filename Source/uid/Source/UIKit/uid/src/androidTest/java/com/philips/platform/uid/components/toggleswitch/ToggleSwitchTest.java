/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.components.toggleswitch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.test.R.color.GroupBlue45;

public class ToggleSwitchTest extends BaseTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    private Context context;
    private Resources resources;

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.launchActivity(getLaunchIntent(NavigationColor.ULTRA_LIGHT.ordinal(), ContentColor.ULTRA_LIGHT.ordinal(), ColorRange.PINK.ordinal()));
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_toggle_switch);
        resources = getInstrumentation().getContext().getResources();
        context = activity;
    }

    //*********************************Toggle Switch Layout TestScenarios**************************//

    @Test
    public void verifyToggleSwitchNotCheckedOnEnabledTap() {
        getToggleSwitch().perform(ViewActions.click());
        getToggleSwitch().check(matches(isNotChecked()));
    }

    @Test
    public void verifyToggleSwitchCheckedOnDoubleEnabledTap() {
        getToggleSwitch().perform(ViewActions.click());
        getToggleSwitch().perform(ViewActions.click());
        getToggleSwitch().check(matches(isChecked()));
    }

    @Test
    public void verifyThumbHighlightRadius() {
        int radius = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.toggleswitch_ripple_radius);
        getToggleSwitch().check(matches(FunctionDrawableMatchers.isSameRippleRadius(TestConstants.FUNCTION_GET_BACKGROUND, radius)));
    }

    @Test
    public void verifyThumbHighlightColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(context, R.attr.uidThumbDefaultPressedBorderColor);
        getToggleSwitch().
                check(matches(FunctionDrawableMatchers.
                        isSameRippleColor(TestConstants.FUNCTION_GET_BACKGROUND, android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifySwitchOffIfSlideRightOnSwitchOn() {
        getToggleSwitch().perform(ViewActions.swipeRight());
        getToggleSwitch().check(matches(isNotChecked()));
    }

    @Test
    public void verifySwitchRemainsOnIfSlideRightTwiceOnSwitchOn() {
        getToggleSwitch().perform(ViewActions.swipeRight());
        getToggleSwitch().perform(ViewActions.swipeLeft());
        getToggleSwitch().check(matches(isChecked()));
    }
    //*********************************Toggle Switch Theming TestScenarios**************************//

    @Test
    public void verifySwitchThumbFillColor() {
        getToggleSwitch().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SWITCH_THUMB_TINT_LIST, android.R.attr.state_enabled, Color.WHITE)));
    }

    // TODO: 11/2/2016  How to fix this. Always returns disabled color combination ???
    @Ignore
    @Test
    public void verifyToggleSwitchTrackOnColorTest() {
        final int expectedTrackEnabledColor = ContextCompat.getColor(context, GroupBlue45);
        getToggleSwitch().check(matches(isChecked()));
        getToggleSwitch().check(matches(FunctionDrawableMatchers
                .isSameColor(trackFunction(), android.R.attr.state_checked, expectedTrackEnabledColor, trackID())));
    }

    private ViewInteraction getToggleSwitch() {
        return onView(withId(com.philips.platform.uid.test.R.id.toggle_switch));
    }

    private String trackFunction() {
        return TestConstants.FUNCTION_GET_UID_TRACK_DRAWABLE;
    }

    private int trackID() {
        return R.id.uid_id_switch_track;
    }
}