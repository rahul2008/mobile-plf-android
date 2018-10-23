package com.philips.platform.uid.components.expander;

import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.Expander;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertTrue;

public class ExpanderTitlePanelDefaultTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> activityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources resources;
    private Expander expander;
    private BaseTestActivity activity;

    @Before
    public void setUp() {
        activity = activityTestRule.getActivity();
        activity.switchTo(R.layout.uid_expander_title_layout_default);
        resources = getInstrumentation().getContext().getResources();


    }

    // Icon tests
    @Test
    public void verifyIconEndMargin() {
        int expectedEndMargin = resources.getDimensionPixelSize(R.dimen.uid_expander_title_icon_margin_end);
        getExpanderTitlePanelIcon().check(matches(TextViewPropertiesMatchers.isSameRightMargin((expectedEndMargin))));
    }

    @Test
    public void verifyIconFontSize() {
        int expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_expander_title_icon_font_size);
        getExpanderTitlePanelIcon().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyIconColor() {
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidContentItemSecondaryNormalIconColor);
        getExpanderTitlePanelIcon().check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }

    @Test
    public void verifyIconDefaultVisibiulity() {
        getExpanderTitlePanelIcon().check(matches(ViewPropertiesMatchers.isVisible(View.GONE)));
    }

    // Chevron tests


    @Test
    public void verifyChevronFontSize() {
        int expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_expander_title_chevron_font_size);
        getExpanderTitlePanelChevron().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyChevronColor() {
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidContentItemSecondaryNormalIconColor);
        getExpanderTitlePanelChevron().check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }


    // Title tests
    // test title text color

    @Test
    public void verifyTitleTextEndMargin() {
        int expectedEndMargin = resources.getDimensionPixelSize(R.dimen.uid_expander_title_text_margin_end);
        getExpanderTitlePanelText().check(matches(ViewPropertiesMatchers.isSameEndMargin(expectedEndMargin)));
    }

    @Test
    public void verifyTitleTextMinHeight() {
        int expectedMinHeight = resources.getDimensionPixelSize(R.dimen.uid_expander_view_title_min_height);
        getExpanderTitlePanelText().check(matches(ViewPropertiesMatchers.isSameViewMinHeight(expectedMinHeight)));
    }


    @Test
    public void verifyTitleTextColor() {
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidContentItemPrimaryNormalTextColor);
        getExpanderTitlePanelText().check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }


    @Test
    public void verifyTitleTextTypeface() {
        getExpanderTitlePanelText().check(matches(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_MEDIUM)));
    }

    @Test
    public void verifyTitleTextFontSize() {
        int expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_expander_title_text_size);
        getExpanderTitlePanelText().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyTitleTextMaximumLines(){

        getExpanderTitlePanelText().check(matches(TextViewPropertiesMatchers.isSameMaxline(3)));
    }





    private ViewInteraction getExpanderTitlePanelIcon() {
        return onView(withId(com.philips.platform.uid.R.id.uid_expander_title_icon));
    }

    private ViewInteraction getExpanderTitlePanelChevron() {
        return onView(withId(com.philips.platform.uid.R.id.uid_expander_title_chevron));
    }

    private ViewInteraction getExpanderTitlePanelText() {
        return onView(withId(com.philips.platform.uid.R.id.uid_expander_title_text));
    }

}
