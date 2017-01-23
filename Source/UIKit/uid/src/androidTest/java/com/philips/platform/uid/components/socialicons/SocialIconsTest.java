package com.philips.platform.uid.components.socialicons;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.CheckBox;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uid.activity.BaseTestActivity.CONTENT_COLOR_KEY;
import static com.philips.platform.uid.test.R.color.GroupBlue45;
import static com.philips.platform.uid.test.R.color.White;
import static org.hamcrest.CoreMatchers.allOf;

public class SocialIconsTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private BaseTestActivity activity;

    Resources resources;
    private static final int ULTRA_LIGHT = 0;
    private static final int VERY_LIGHT = 1;
    private static final int LIGHT = 2;
    private static final int BRIGHT = 3;
    private static final int VERY_DARK = 4;

    /***********************
     * Layout scenarios
     ***************************/
    @Before
    public void setUpDefaultTheme() {
        mActivityTestRule.launchActivity(getIntent(0));
        activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_buttons);
    }

    @Test
    public void verifyIconWidth() throws Exception {
        int width = activity.getResources().getDimensionPixelSize(R.dimen.uid_social_icons_width);
        getPrimarySocialIcon().check(matches(ViewPropertiesMatchers.isSameViewWidth(width)));
        getPrimarySocialIcon().check(matches(ViewPropertiesMatchers.isSameViewMinWidth(width)));
    }

    @Test
    public void verifyIconHeight() throws Exception {
        int width = activity.getResources().getDimensionPixelSize(R.dimen.uid_social_icons_width);
        getPrimarySocialIcon().check(matches(ViewPropertiesMatchers.isSameViewHeight(width)));
        getPrimarySocialIcon().check(matches(ViewPropertiesMatchers.isSameViewMinHeight(width)));
    }

    @Test
    public void verifyPaddingLeftRightHeight() throws Exception {
        getPrimarySocialIcon().check(matches(ViewPropertiesMatchers.isSameStartPadding(0)));
        getPrimarySocialIcon().check(matches(ViewPropertiesMatchers.isSameEndPadding(0)));
    }

    @Test
    public void verifyDrawableSize() throws Exception {
        int width = activity.getResources().getDimensionPixelSize(R.dimen.uid_social_icons_width);
        getPrimarySocialIcon().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableHeight(0, width)));
        getPrimarySocialIcon().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableWidth(0, width)));
    }

    @Test
    public void verifySocialIconCornerRadius() {
        float radius = (float) Math.floor(activity.getResources().getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.button_cornerradius));
        getPrimarySocialIcon().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, radius)));
    }

    /***********************
     * Theming scenarios
     ***************************/

    @Test
    public void verifySocialMediaPrimaryButtonBackgroundIconColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidSocialIconPrimaryButtonTextColor);
        getPrimarySocialIcon().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, android.R.attr.state_enabled, expectedColor)));
    }

    //// TODO: 1/20/2017
    // cannot automated because it is a vector
    @Test
    public void verifySocialMediaPrimaryButtonFillColor() {


    }

    @Test
    public void verifySocialMediaPrimaryButtonPressedFillColor() {

    }

    @Test
    public void verifySocialMediaWhiteButtonBackgroundIconColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidSocialIconWhiteButtonTextColor);
        getPrimarySocialIcon().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, android.R.attr.state_enabled, expectedColor)));

    }

    //// TODO: 1/20/2017
    // cannot automated because it is a vector
    @Test
    public void verifySocialMediaWhiteButtonFillColor() {


    }

    @Test
    public void verifySocialMediaWhiteButtonPressedColor() {

    }

    @Test
    public void verifyIconSameAsProvidedInView() throws Exception {
        getPrimarySocialIcon().check(matches(TextViewPropertiesMatchers.hasSameCompoundDrawable(0, com.philips.platform.uid.test.R.drawable.ic_hamburger_menu)));
    }

    private ViewInteraction getPrimarySocialIcon() {
        return onView(withId(com.philips.platform.uid.test.R.id.social_icon_facebook));
    }


    @NonNull
    private Intent getIntent(final int contentColorIndex) {
        final Bundle bundleExtra = new Bundle();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        bundleExtra.putInt(CONTENT_COLOR_KEY, contentColorIndex);
        intent.putExtras(bundleExtra);
        return intent;
    }
}