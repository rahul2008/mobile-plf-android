/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.aboutscreen;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.util.DisplayMetrics;
import android.view.Gravity;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.DrawableMatcher;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.AboutScreen;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class AboutScreenTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> activityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources resources;
    private AboutScreen aboutScreen;
    private Activity activity;

    @Before
    public void setUp() {
        final BaseTestActivity baseTestActivity = activityTestRule.getActivity();
        baseTestActivity.switchTo(com.philips.platform.uid.test.R.layout.layout_about_screen);
        resources = getInstrumentation().getContext().getResources();
        Espresso.registerIdlingResources(baseTestActivity.getIdlingResource());
        aboutScreen = (AboutScreen) baseTestActivity.findViewById(com.philips.platform.uid.test.R.id.test_about_screen);
        activity = baseTestActivity;
    }

    private ViewInteraction getAboutScreenLayout() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_about_screen));
    }

    private ViewInteraction getAboutScreenShield() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_about_screen_icon));
    }

    private ViewInteraction getAboutScreenTitle() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_about_screen_app_name));
    }

    private ViewInteraction getAboutScreenVersion() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_about_screen_version));
    }

    private ViewInteraction getAboutScreenCopyright() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_about_screen_copyright));
    }

    private ViewInteraction getAboutScreenTerms() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_about_screen_terms));
    }

    private ViewInteraction getAboutScreenPrivacy() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_about_screen_privacy));
    }

    private ViewInteraction getAboutScreenDisclosure() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_about_screen_disclosure));
    }

    @Test
    public void verifyAboutScreenStartPadding() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_about_screen_margin_start_end);
        getAboutScreenLayout().check(matches(ViewPropertiesMatchers.isSameStartPadding(expected)));
    }

    @Test
    public void verifyAboutScreenEndPadding() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_about_screen_margin_start_end);
        getAboutScreenLayout().check(matches(ViewPropertiesMatchers.isSameEndPadding(expected)));
    }

    @Test
    public void verifyAboutScreenBackgroundColor() {
        int expected = UIDTestUtils.getAttributeColor(activity, R.attr.uidAboutScreenFullScreenBackgroundColor);
        getAboutScreenLayout().check(matches(ViewPropertiesMatchers.hasSameColorDrawableBackgroundColor(expected)));
    }

    @Test
    public void verifyAboutScreenShieldTopPadding() {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getRealSize(point);
        int expected = Double.valueOf(Math.max(point.x, point.y) * 0.09).intValue();
        getAboutScreenShield().check(matches(ViewPropertiesMatchers.isSameTopPadding(expected)));
    }

    @Test
    public void verifyAboutScreenShieldBottomPadding() {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getRealSize(point);
        int expected = Double.valueOf(Math.max(point.x, point.y) * 0.09).intValue();
        getAboutScreenShield().check(matches(ViewPropertiesMatchers.isSameBottomPadding(expected)));
    }

    @Test
    public void verifyAboutScreenShieldGravity() {
        getAboutScreenShield().check(matches(ViewPropertiesMatchers.hasSameGravity(Gravity.CENTER_HORIZONTAL)));
    }

    @Test
    public void verifyAboutScreenTitleBottomMargin() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_about_screen_app_name_bottom_margin);
        getAboutScreenTitle().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expected)));
    }

    @Test
    public void verifyAboutScreenTitleHeight() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_about_screen_app_name_size);
        getAboutScreenTitle().check(matches(TextViewPropertiesMatchers.isSameFontSize(expected)));
    }

    @Test
    public void verifyAboutScreenTitleTypeface() {
        getAboutScreenTitle().check(matches(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_LIGHT)));
    }

    @Test
    public void verifyAboutScreenTitleGravity() {
        getAboutScreenTitle().check(matches(ViewPropertiesMatchers.hasSameGravity(Gravity.CENTER_HORIZONTAL)));
    }

    @Test
    public void verifyAboutScreenTitleColor() {
        int expected = UIDTestUtils.getAttributeColor(activity, R.attr.uidAboutScreenDefaultTitleColor);
        getAboutScreenTitle().check(matches(TextViewPropertiesMatchers.isSameTextColor(expected)));
    }

    @Test
    public void verifyAboutScreenVersionBottomMargin() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_about_screen_margin_start_end);
        getAboutScreenVersion().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expected)));
    }

    @Test
    public void verifyAboutScreenVersionHeight() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_about_screen_label_size);
        getAboutScreenVersion().check(matches(TextViewPropertiesMatchers.isSameFontSize(expected)));
    }

    @Test
    public void verifyAboutScreenVersionTypeface() {
        getAboutScreenVersion().check(matches(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_BOLD)));
    }

    @Test
    public void verifyAboutScreenVersionColor() {
        int expected = UIDTestUtils.getAttributeColor(activity, R.attr.uidAboutScreenDefaultSubtitleColor);
        getAboutScreenVersion().check(matches(TextViewPropertiesMatchers.isSameTextColor(expected)));
    }

    @Test
    public void verifyAboutScreenCopyrightBottomMargin() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_about_screen_label_bottom_margin);
        getAboutScreenCopyright().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expected)));
    }

    @Test
    public void verifyAboutScreenCopyrightHeight() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_about_screen_label_size);
        getAboutScreenCopyright().check(matches(TextViewPropertiesMatchers.isSameFontSize(expected)));
    }

    @Test
    public void verifyAboutScreenCopyrightTypeface() {
        getAboutScreenCopyright().check(matches(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_BOOK)));
    }

    @Test
    public void verifyAboutScreenCopyrightColor() {
        int expected = UIDTestUtils.getAttributeColor(activity, R.attr.uidAboutScreenDefaultTextColor);
        getAboutScreenCopyright().check(matches(TextViewPropertiesMatchers.isSameTextColor(expected)));
    }

    @Test
    public void verifyAboutScreenDisclosureHeight() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_about_screen_label_size);
        getAboutScreenDisclosure().check(matches(TextViewPropertiesMatchers.isSameFontSize(expected)));
    }

    @Test
    public void verifyAboutScreenDisclosureTypeface() {
        getAboutScreenDisclosure().check(matches(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_BOOK)));
    }

    @Test
    public void verifyAboutScreenDisclosureColor() {
        int expected = UIDTestUtils.getAttributeColor(activity, R.attr.uidAboutScreenDefaultTextColor);
        getAboutScreenDisclosure().check(matches(TextViewPropertiesMatchers.isSameTextColor(expected)));
    }

    @Test
    public void verifyAboutScreenTermsBottomMargin() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_about_screen_label_bottom_margin);
        getAboutScreenTerms().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expected)));
    }

    @Test
    public void verifyAboutScreenTermsHeight() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_about_screen_label_size);
        getAboutScreenTerms().check(matches(TextViewPropertiesMatchers.isSameFontSize(expected)));
    }

    @Test
    public void verifyAboutScreenTermsTypeface() {
        getAboutScreenTerms().check(matches(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_LIGHT)));
    }

    @Test
    public void verifyAboutScreenPrivacyBottomMargin() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_about_screen_label_bottom_margin);
        getAboutScreenPrivacy().check(matches(ViewPropertiesMatchers.isSameBottomMargin(expected)));
    }

    @Test
    public void verifyAboutScreenPrivacyHeight() {
        int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_about_screen_label_size);
        getAboutScreenPrivacy().check(matches(TextViewPropertiesMatchers.isSameFontSize(expected)));
    }

    @Test
    public void verifyAboutScreenPrivacyTypeface() {
        getAboutScreenPrivacy().check(matches(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_LIGHT)));
    }

    @Test
    public void verifyAboutScreenSetTextWithRes() {
        String text = "Catalog App DLS";
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                aboutScreen.setAppName(com.philips.platform.uid.test.R.string.catalog_app_name);
                aboutScreen.setAppVersion(com.philips.platform.uid.test.R.string.catalog_app_name);
                aboutScreen.setCopyright(com.philips.platform.uid.test.R.string.catalog_app_name);
                aboutScreen.setDisclosure(com.philips.platform.uid.test.R.string.catalog_app_name);
                aboutScreen.setPrivacy(com.philips.platform.uid.test.R.string.catalog_app_name);
                aboutScreen.setTerms(com.philips.platform.uid.test.R.string.catalog_app_name);
            }
        });
        getAboutScreenTitle().check(matches(TextViewPropertiesMatchers.hasSameText(text)));
    }

    @Test
    public void verifyAboutScreenSetTextWithCharSeq() {
        final String text = "Catalog App DLS";
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                aboutScreen.setAppName(text);
                aboutScreen.setAppVersion(text);
                aboutScreen.setCopyright(text);
                aboutScreen.setDisclosure(text);
                aboutScreen.setPrivacy(text);
                aboutScreen.setTerms(text);
            }
        });
        getAboutScreenVersion().check(matches(TextViewPropertiesMatchers.hasSameText(resources.getText(com.philips.platform.uid.test.R.string.catalog_app_name))));
    }

}
