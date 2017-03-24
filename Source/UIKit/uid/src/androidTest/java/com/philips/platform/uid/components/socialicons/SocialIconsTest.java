package com.philips.platform.uid.components.socialicons;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class SocialIconsTest extends BaseTest {

    private static final int ULTRA_LIGHT = 0;
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private BaseTestActivity activity;

    /***********************
     * Layout scenarios
     ***************************/
    @Before
    public void setUp() {
        mActivityTestRule.launchActivity(getIntentWithContentRange(ULTRA_LIGHT));
        activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_social_icons);
    }

    @Test
    public void verifyIconWidth() throws Exception {
        int width = activity.getResources().getDimensionPixelSize(R.dimen.uid_social_icons_width);
        getSocialIconPrimaryButton().check(matches(ViewPropertiesMatchers.isSameViewWidth(width)));
        getSocialIconPrimaryButton().check(matches(ViewPropertiesMatchers.isSameViewMinWidth(width)));
    }

    @Test
    public void verifyIconHeight() throws Exception {
        int width = activity.getResources().getDimensionPixelSize(R.dimen.uid_social_icons_width);
        getSocialIconPrimaryButton().check(matches(ViewPropertiesMatchers.isSameViewHeight(width)));
        getSocialIconPrimaryButton().check(matches(ViewPropertiesMatchers.isSameViewMinHeight(width)));
    }

    @Test
    public void verifyPaddingLeftRightHeight() throws Exception {
        getSocialIconPrimaryButton().check(matches(ViewPropertiesMatchers.isSameStartPadding(0)));
        getSocialIconPrimaryButton().check(matches(ViewPropertiesMatchers.isSameEndPadding(0)));
    }

    @Test
    public void verifyDrawableSize() throws Exception {
        int width = activity.getResources().getDimensionPixelSize(R.dimen.uid_social_icons_width);
        getSocialIconPrimaryButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableHeight(0, width)));
        getSocialIconPrimaryButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableWidth(0, width)));
    }

    @Test
    public void verifySocialIconCornerRadius() {
        float radius = (float) Math.floor(activity.getResources().getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.button_cornerradius));
        getSocialIconPrimaryButton().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, radius)));
    }

    /***********************
     * Theming scenarios
     ***************************/

    @Test
    public void verifySocialMediaPrimaryButtonBackgroundIconColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidButtonSocialMediaPrimaryNormalIconColor);
        getSocialIconPrimaryButton().check(matches(TextViewPropertiesMatchers
                .isSameCompoundDrawableColor(0, android.R.attr.state_enabled, expectedColor)));
    }

    //// TODO: 1/20/2017
    // cannot automated because it is a vector
    @Test
    public void verifySocialMediaPrimaryButtonFillColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidButtonSocialMediaPrimaryNormalBackgroundColor);
        getSocialIconPrimaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifySocialMediaPrimaryButtonPressedFillColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidButtonSocialMediaPrimaryPressedBackgroundColor);

        getSocialIconPrimaryButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorListWithReflection(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, 1, expectedColor)));
    }

    @Test
    public void verifySocialMediaWhiteButtonBackgroundIconColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidButtonSocialMediaWhiteNormalIconColor);
        getSocialIconWhiteButton().check(matches(TextViewPropertiesMatchers
                .isSameCompoundDrawableColor(0, android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifySocialMediaWhiteButtonFillColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidButtonSocialMediaWhiteNormalBackgroundColor);

        getSocialIconWhiteButton().check(matches(FunctionDrawableMatchers.isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST,
                android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifySocialMediaWhiteButtonPressedColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidButtonSocialMediaWhitePressedBackgroundColor);

        getSocialIconWhiteButton().check(matches(FunctionDrawableMatchers.isSameColorFromColorListWithReflection(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, 1, expectedColor)));
    }

    private ViewInteraction getSocialIconPrimaryButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.social_icon_facebook));
    }

    private ViewInteraction getSocialIconWhiteButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.social_icon_twitter));
    }
}