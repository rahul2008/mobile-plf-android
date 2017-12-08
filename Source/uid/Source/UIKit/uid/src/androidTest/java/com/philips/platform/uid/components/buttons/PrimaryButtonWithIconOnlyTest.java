/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.components.buttons;

import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class PrimaryButtonWithIconOnlyTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources testResources;

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_buttons);
        testResources = getInstrumentation().getContext().getResources();
    }

    /************************************************
     * Layout
     ************************************************/

    @Test
    public void verifyIconOnlyButtonHeight() {

        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.button_height);
        getIconOnlyButton()
                .check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyIconOnlyButtonWidth() {
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.iconbutton_width);
        getIconOnlyButton()
                .check(matches(ViewPropertiesMatchers.isSameViewWidth(expectedWidth)));
    }

    @Test
    public void verifyIconHeight() {
        int expectedIconHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.icon_height);
        getIconOnlyButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableHeight(0, expectedIconHeight)));
    }

    @Test
    public void verifyIconWidth() {
        int expectedIconWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.icon_width);
        getIconOnlyButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableWidth(0, expectedIconWidth)));
    }

    @Test
    public void verifyButtonWithIconLeftPadding() {
        int expectedLeftPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.iconbutton_left_padding);
        getIconOnlyButton().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
    }

    @Test
    public void verifyButtonWithIconRightPadding() {
        int expectedRightPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.iconbutton_right_padding);
        getIconOnlyButton().check(matches(ViewPropertiesMatchers.isSameRightPadding(expectedRightPadding)));
    }

    @Test
    public void verifyButtonCornerRadius() {
        float radius = (float) Math.floor(testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.button_cornerradius));
        getIconOnlyButton().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, radius)));
    }

    /************************************************
     * Theming
     ************************************************/

    private ViewInteraction getIconOnlyButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.demo_image_button));
    }
}
