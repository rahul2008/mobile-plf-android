/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.components.buttons;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class QuietButtonTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources testResources;
    private Context context;

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_quiet_buttons);
        testResources = activity.getResources();
        context = activity;
    }

    /*****************************************
     * Layout Text Only Scenarios
     *********************************************/

    @Test
    public void verifyQuietButtonHeight() {
        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.button_height);

        getQuietButton()
                .check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyQuietButtonLeftPadding() {
        int expectedLeftPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.quietbutton_left_right_padding);
        getQuietButton().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
    }

    @Test
    public void verifyQuietButtonRightPadding() {
        int expectedRightPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.quietbutton_left_right_padding);
        getQuietButton().check(matches(ViewPropertiesMatchers.isSameRightPadding(expectedRightPadding)));
    }

    @Test
    public void verifyQuietButtonFontSize() {
        int expectedFontSize = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.button_font_size);
        getQuietButton().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    /*****************************************
     * Layout Icon Only Scenarios
     *********************************************/
    @Test
    public void verifyQuietIconOnlyButtonWidth() {
        int expectedWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.iconbutton_width);
        getQuietIconOnlyButton()
                .check(matches(ViewPropertiesMatchers.isSameViewWidth(expectedWidth)));
    }

    @Test
    public void verifyQuietButtonIconHeight() {
        int expectedIconHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.icon_height);
        getQuietIconOnlyButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableHeight(0, expectedIconHeight)));
    }

    @Test
    public void verifyQuietButtonIconWidth() {
        int expectedIconWidth = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.icon_width);
        getQuietIconOnlyButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableWidth(0, expectedIconWidth)));
    }

    @Test
    public void verifyQuietButtonWithIconLeftPadding() {
        int expectedLeftPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.iconbutton_left_padding);
        getQuietIconOnlyButton().check(matches(ViewPropertiesMatchers.isSameLeftPadding(expectedLeftPadding)));
    }

    @Test
    public void verifyQuietButtonWithIconRightPadding() {
        int expectedRightPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.iconbutton_right_padding);
        getQuietIconOnlyButton().check(matches(ViewPropertiesMatchers.isSameRightPadding(expectedRightPadding)));
    }

    @Test
    public void verifyQuietTextandIconButtonCompoundPadding() {
        int expectedCompoundPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.iconandtextbutton_compoundpadding);
        getQuietIconandTextButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawablePadding(expectedCompoundPadding)));
    }

    /*******************************************************
     * Theming
     ******************************************************/

    @Test
    public void verifyQuietTextOnlyButtonFontColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(context, R.attr.uidButtonQuietEmphasisNormalTextColor);
        getQuietButton().check(matches(TextViewPropertiesMatchers.isSameTextColorWithReflection(2, expectedColor)));
    }

    @Test
    public void verifyQuietTextOnlyPressedButtonFontColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(context, R.attr.uidButtonQuietEmphasisPressedIconColor);
        getQuietButton().check(matches(TextViewPropertiesMatchers.isSameTextColorWithReflection(1, expectedColor)));
    }

    @Test
    public void verifyQuietTextOnlyDisabledButtonFontColor() {
        final int disabledTextColor = UIDTestUtils.getAttributeColor(context, R.attr.uidButtonQuietEmphasisDisabledTextColor);
        getQuietButton().check(matches(TextViewPropertiesMatchers.isSameTextColorWithReflection(0, disabledTextColor)));
    }

    private ViewInteraction getQuietButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.quiet_button));
    }

    private ViewInteraction getQuietIconOnlyButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.quiet_icon_only));
    }

    private ViewInteraction getQuietIconandTextButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.quiet_demo_image_text_button));
    }
}


