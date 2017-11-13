/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.components.uipicker;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.UIPicker;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class UIPickerTest extends BaseTest {

    private final String[] STATES = new String[]{
            "Alabama"
    };
    private static final int ULTRA_LIGHT = 0;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private Resources resources;
    private BaseTestActivity activity;

    @Before
    public void setUpTheme() {
        final Intent intent = getLaunchIntent(ULTRA_LIGHT, ColorRange.GROUP_BLUE.ordinal());
        activity = mActivityTestRule.launchActivity(intent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_uipicker);
        resources = activity.getResources();
    }

    private ViewInteraction getUIPickerItemTextView() {
        return onView(withId(com.philips.platform.uid.test.R.id.ui_picker_text1));
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
        AppCompatTextView parent = (AppCompatTextView) View.inflate(activity, com.philips.platform.uid.test.R.layout.ui_picker_item, null);
        TextView itemTextView = (TextView) parent.findViewById(com.philips.platform.uid.test.R.id.text1);
        assertTrue(TextViewPropertiesMatchers.isSameTypeface(activity, TestConstants.FONT_PATH_CS_BOOK).matches(itemTextView));
    }

    @Test
    public void verifyItemTextColor(){
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidListItemDefaultNormalOffTextColor);
        getUIPickerItemTextView().check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }

    @Test
    public void verifyItemTextNormalBackgroundColor(){
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidPopupDefaultNormalBackgroundColor);
        getUIPickerItemTextView().check(matches(ViewPropertiesMatchers.hasSameStateListBackgroundDrawableStateColor(new int[]{android.R.attr.state_enabled},color)));
    }

    @Test
    public void verifyItemTextPressedBackgroundColor(){
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidListItemDefaultPressedBackgroundColor);
        getUIPickerItemTextView().check(matches(ViewPropertiesMatchers.hasSameStateListBackgroundDrawableStateColor(new int[]{android.R.attr.state_pressed},color)));
    }

    @Test
    public void verifyItemTextActivatedBackgroundColor() {
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidListItemDefaultPressedBackgroundColor);
        getUIPickerItemTextView().check(matches(ViewPropertiesMatchers.hasSameStateListBackgroundDrawableStateColor(new int[]{android.R.attr.state_activated}, color)));
    }

    @Test
    public void verifyItemTextSupportsSingleLine() {
        getUIPickerItemTextView().check(matches(TextViewPropertiesMatchers.isSingleline()));
    }

    @Test
    public void verifyUiPickerDefaultHeight(){
        int expected = resources.getDimensionPixelSize(R.dimen.uid_uipicker_item_height);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Label selectedLabel = (Label) activity.findViewById(com.philips.platform.uid.test.R.id.ui_picker_text1);
                final UIPicker uiPicker = new UIPicker(activity);
                ArrayAdapter adapter = new UIPickerAdapter(activity, com.philips.platform.uid.test.R.layout.ui_picker_item, STATES);
                uiPicker.setAdapter(adapter);
                uiPicker.setAnchorView(selectedLabel);
                uiPicker.setModal(true);
                uiPicker.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                uiPicker.setSelection(position);
                                uiPicker.dismiss();
                            }
                        }
                );
                uiPicker.show();
            }
        });
        onView(withText("Alabama"))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).
                check(matches(ViewPropertiesMatchers.isSameViewHeight(expected)));
    }

    @Test
    public void verifyUiPickerDimens(){
        final int expected = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uipicker_item_width);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final UIPicker uiPicker= new UIPicker(activity);
                ArrayAdapter adapter = new UIPickerAdapter(activity, com.philips.platform.uid.test.R.layout.ui_picker_item, STATES);
                Label selectedLabel = (Label) activity.findViewById(com.philips.platform.uid.test.R.id.ui_picker_text1);
                uiPicker.setAdapter(adapter);
                uiPicker.setAnchorView(selectedLabel);
                uiPicker.setModal(true);
                uiPicker.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                uiPicker.setSelection(position);
                                uiPicker.dismiss();
                            }
                        }
                );
                uiPicker.setHeight(expected);
                uiPicker.setWidth(expected);
                uiPicker.setDropDownGravity(Gravity.END);
                uiPicker.show();
            }
        });
        onView(withText("Alabama"))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).
                check(matches(ViewPropertiesMatchers.isSameViewWidth(expected)));
    }
}
