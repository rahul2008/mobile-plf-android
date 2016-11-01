/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.components.Buttons;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;

import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.matcher.FunctionDrawableMatchers;
import com.philips.platform.uit.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uit.matcher.ViewPropertiesMatchers;
import com.philips.platform.uit.utils.TestConstants;
import com.philips.platform.uit.utils.UITTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uit.test.R.color.GroupBlue45;
import static com.philips.platform.uit.test.R.color.GroupBlue55;
import static com.philips.platform.uit.utils.UITTestUtils.waitFor;

public class QuietButtonTest {

    private Resources testResources;
    private Context instrumentationContext;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);

    @Before
    public void setUp() {
        mActivityTestRule.getActivity().switchTo(com.philips.platform.uit.test.R.layout.layout_buttons);
        testResources = getInstrumentation().getContext().getResources();
        instrumentationContext = getInstrumentation().getContext();
    }

    /*****************************************
     * Layout Text Only Scenarios
     *********************************************/

    @Test
    public void verifyQuietButtonHeight() {
        UITTestUtils.waitFor(testResources, 750);
        int expectedHeight = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.button_height);
        getQuietButton()
                .check(matches(FunctionDrawableMatchers.isSameHeight(TestConstants.FUNCTION_GET_BACKGROUND, expectedHeight)));
    }

    @Test
    public void verifyQuietButtonLeftPadding() {
        int expectedLeftPadding = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.quietbutton_left_padding);
        getQuietButton().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
    }

    @Test
    public void verifyQuietButtonRightPadding() {
        int expectedRightPadding = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.quietbutton_right_padding);
        getQuietButton().check(matches(ViewPropertiesMatchers.isSameRightPadding(expectedRightPadding)));
    }

    @Test
    public void verifyQuietButtonFontSize() {
        int expectedFontSize = (int) (testResources.getDimension(com.philips.platform.uit.test.R.dimen.button_font_size));
        getQuietButton().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    /*****************************************
     * Layout Icon Only Scenarios
     *********************************************/
    @Test
    public void verifyQuietIconOnlyButtonWidth() {
        waitFor(testResources, 750);
        int expectedWidth = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.iconbutton_width);
        getQuietIconOnlyButton()
                .check(matches(FunctionDrawableMatchers.isSameWidth(TestConstants.FUNCTION_GET_BACKGROUND, expectedWidth)));
    }

    @Test
    public void verifyQuietButtonIconHeight() {
        waitFor(testResources, 750);
        int expectedIconHeight = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.icon_height);
        getQuietIconOnlyButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableHeight(0, expectedIconHeight)));
    }

    @Test
    public void verifyQuietButtonIconWidth() {
        waitFor(testResources, 750);
        int expectedIconWidth = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.icon_width);
        getQuietIconOnlyButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableWidth(0, expectedIconWidth)));
    }

    @Test
    public void verifyQuietButtonWithIconLeftPadding() {
        int expectedLeftPadding = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.iconbutton_left_padding);
        getQuietIconOnlyButton().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
    }

    @Test
    public void verifyQuietButtonWithIconRightPadding() {
        int expectedRightPadding = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.iconbutton_right_padding);
        getQuietIconOnlyButton().check(matches(ViewPropertiesMatchers.isSameRightPadding(expectedRightPadding)));
    }

    @Test
    public void verifyQuietTextandIconButtonCompoundPadding() {

    }

    /*******************************************************
     * Theming
     ******************************************************/

    @Test
    public void verifyQuietTextOnlyButtonFontColor() {
        final int expectedColor = ContextCompat.getColor(instrumentationContext, GroupBlue45);
        getQuietButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyQuietTextOnlyPressedButtonFontColor() {
        final int expectedColor = ContextCompat.getColor(instrumentationContext, GroupBlue55);
        getQuietButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_pressed, expectedColor)));
    }

    @Test
    public void verifyQuietTextOnlyDisabledButtonFontColor() {
        final int disabledTextColor = UITTestUtils.modulateColorAlpha(GroupBlue45, 0.35f);
        getQuietButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.enabled, disabledTextColor)));
    }

    private ViewInteraction getQuietButton() {
        return onView(withId(com.philips.platform.uit.test.R.id.quiet_button));
    }

    private ViewInteraction getQuietIconOnlyButton() {
        return onView(withId(com.philips.platform.uit.test.R.id.quiet_icon_only));
    }
}


