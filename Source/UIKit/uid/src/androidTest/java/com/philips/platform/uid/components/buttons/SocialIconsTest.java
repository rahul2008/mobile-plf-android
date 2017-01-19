/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.buttons;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class SocialIconsTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private BaseTestActivity activity;

    @Before
    public void setUp() {
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

    private ViewInteraction getPrimarySocialIcon() {
        return onView(withId(com.philips.platform.uid.test.R.id.social_icon_facebook));
    }

    @Ignore
    @Test
    public void verifyBackgroundColor() throws Exception {
        final int attributeColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidSocialIconPrimaryButtonTextColor);
//        final ColorStateList colorStateList = ThemeUtils.buildColorStateList(activity.getResources(), activity.getTheme(), attributeColor);
        getPrimarySocialIcon().check(matches(TextViewPropertiesMatchers.sameBackgroundColor(attributeColor)));
    }
}
