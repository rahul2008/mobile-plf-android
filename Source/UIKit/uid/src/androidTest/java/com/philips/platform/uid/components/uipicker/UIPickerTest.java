/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.components.uipicker;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertTrue;

public class UIPickerTest extends BaseTest {

    private static final int ULTRA_LIGHT = 0;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private Resources resources;
    private BaseTestActivity activity;

    @Before
    public void setUpTheme() {
        final Intent intent = getLaunchIntent(ULTRA_LIGHT, ColorRange.GROUP_BLUE.ordinal());
        activity = mActivityTestRule.launchActivity(intent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_uipicker_item);
        resources = activity.getResources();
    }

    private ViewInteraction getUIPickerItemTextView() {
        return onView(withId(com.philips.platform.uid.test.R.id.test_text1));
    }

    @Test
    public void verifyItemTextHeight() {
        int expectedHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uipicker_item_height);
        getUIPickerItemTextView().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyItemTextGravityCenterVertical() throws Exception {
        getUIPickerItemTextView().check(matches(TextViewPropertiesMatchers.isCenterVerticallyAligned()));
    }

    @Test
    public void verifyItemTextStartPadding() throws Exception {
        int expectedStartPadding = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uipicker_item_text_left_right_padding);
        getUIPickerItemTextView().check(matches(TextViewPropertiesMatchers.isSameStartPadding(expectedStartPadding)));
    }

    @Test
    public void verifyItemTextEndPadding() throws Exception {
        int expectedEndPadding = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uipicker_item_text_left_right_padding);
        getUIPickerItemTextView().check(matches(TextViewPropertiesMatchers.isSameEndPadding(expectedEndPadding)));
    }

    @Test
    public void verifyItemTextFontSize() {
        int expectedFontSize = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uipicker_item_text_size);
        getUIPickerItemTextView().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyItemTextTypeface() {
        AppCompatTextView parent = (AppCompatTextView) View.inflate(activity, com.philips.platform.uid.test.R.layout.layout_uipicker_item, null);
        TextView itemTextView = (TextView) parent.findViewById(com.philips.platform.uid.test.R.id.test_text1);
        assertTrue(TextViewPropertiesMatchers.isSameTypeface(activity, itemTextView.getTypeface(), TestConstants.FONT_PATH_CS_BOOK).matches(itemTextView));
    }

    @Test
    public void verifyItemTextColor(){
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidDialogBodyTextColor);
        getUIPickerItemTextView().check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }

    /*@Test
    public void verifyItemTextNormalBackgroundColor(){
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidPopupDefaultNormalBackgroundColor);
        getUIPickerItemTextView().check(matches(ViewPropertiesMatchers.hasSameStateListDrawableNormalBackgroundColor(color)));
    }

    @Test
    public void verifyItemTextPressedBackgroundColor(){
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidListItemDefaultPressedBackgroundColor);
        getUIPickerItemTextView().check(matches(ViewPropertiesMatchers.hasSameColorDrawableBackgroundColor(color)));
    }*/

    @Test
    public void verifyItemTextSupportsSingleLine() {
        getUIPickerItemTextView().check(matches(TextViewPropertiesMatchers.isSingleline()));
    }
}
