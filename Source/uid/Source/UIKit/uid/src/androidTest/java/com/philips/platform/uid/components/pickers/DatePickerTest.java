/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.pickers;

import android.support.test.espresso.action.ViewActions;
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
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class DatePickerTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> activityActivityTestRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class);
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = activityActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(new PickersFragment());
    }

    @Test
    public void verifyHeaderBackgroundColor() throws Exception {
        int titleId = getViewId("date_picker_header");

        onView(withId(com.philips.platform.uid.test.R.id.datePicker)).perform(ViewActions.click());
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidDatePickerAndroidNormalHeaderBackgroundColor);
        onView(withId(titleId)).check(matches(ViewPropertiesMatchers.hasSameColorDrawableBackgroundColor(color)));
    }

    @Test
    public void verifyHeaderTextColor() throws Exception {
        int titleId = getViewId("date_picker_header_date");
        if(titleId == 0) {
            titleId = getViewId("date_picker_day");
        }
        onView(withId(com.philips.platform.uid.test.R.id.datePicker)).perform(ViewActions.click());
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidDatePickerAndroidNormalOnHeaderTextColor);
        onView(withId(titleId)).check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }

    @Ignore
    @Test
    public void verifyCancelButtonTextColor() throws Exception {
        int titleId = getViewId("button2");

        onView(withId(com.philips.platform.uid.test.R.id.datePicker)).perform(ViewActions.click());
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidDatePickerAndroidNormalButtonTextColor);
        onView(withId(titleId)).check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }

    @Ignore
    @Test
    public void verifyOkButtonTextColor() throws Exception {
        int titleId = getViewId("button1");

        onView(withId(com.philips.platform.uid.test.R.id.datePicker)).perform(ViewActions.click());
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidDatePickerAndroidNormalButtonTextColor);
        onView(withId(titleId)).check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }

    @Ignore
    @Test
    public void verifyTimePickerHeaderBackgroundColor() throws Exception {
        int titleId = getViewId("time_header");

        onView(withId(com.philips.platform.uid.test.R.id.timePicker)).perform(ViewActions.click());
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidDatePickerAndroidNormalHeaderBackgroundColor);
        onView(withId(titleId)).check(matches(ViewPropertiesMatchers.hasSameColorDrawableBackgroundColor(color)));
    }

    @Test
    public void verifyTimePickerCancelButtonTextColor() throws Exception {
        int titleId = getViewId("button2");

        onView(withId(com.philips.platform.uid.test.R.id.timePicker)).perform(ViewActions.click());
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidDatePickerAndroidNormalButtonTextColor);
        onView(withId(titleId)).check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }

    @Test
    public void verifyTimePickerOkButtonTextColor() throws Exception {
        int titleId = getViewId("button1");

        onView(withId(com.philips.platform.uid.test.R.id.timePicker)).perform(ViewActions.click());
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidDatePickerAndroidNormalButtonTextColor);
        onView(withId(titleId)).check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }

    @Ignore
    @Test
    public void verifyTimePickerBackgroundColor() throws Exception {
        int titleId = getViewId("content");

        onView(withId(com.philips.platform.uid.test.R.id.timePicker)).perform(ViewActions.click());
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidDatePickerAndroidNormalHeaderBackgroundColor);
        onView(withId(titleId)).check(matches(ViewPropertiesMatchers.hasSameColorDrawableBackgroundColor(color)));
    }

    @Ignore
    @Test
    public void verifyCalenderContentBackgroundColor() throws Exception {
        int titleId = getViewId("buttonPanel");

        onView(withId(com.philips.platform.uid.test.R.id.datePicker)).perform(ViewActions.click());
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidDatePickerAndroidNormalHeaderBackgroundColor);
        onView(withId(titleId)).check(matches(ViewPropertiesMatchers.hasSameColorDrawableBackgroundColor(color)));
    }

    @Ignore
    @Test
    public void verifyPreviousButtonColor() throws Exception {
        int titleId = getViewId("month_view");

        onView(withText(com.philips.platform.uid.test.R.id.timePicker)).perform(ViewActions.click());
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidDatePickerAndroidNormalContentTextColor);
        onView(withText(titleId)).check(matches(ViewPropertiesMatchers.hasChildrensWithSameTextColor(color)));
    }

    @Ignore
    // TODO: 2/2/2017 fix once time picker is implemented.
    @Test
    public void verifyHourTextColor() throws Exception {
        int hours = getViewId("hours");

        onView(withId(com.philips.platform.uid.test.R.id.timePicker)).perform(ViewActions.click());
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidDatePickerAndroidNormalHeaderTextColor);
        onView(withId(hours)).check(matches(TextViewPropertiesMatchers.isSameTextColor(color)));
    }

    @Ignore
    @Test
    public void verifyNextButtonColor() throws Exception {
        int titleId = getViewId("month_view");

        onView(withId(com.philips.platform.uid.test.R.id.datePicker)).perform(ViewActions.click());
        final int color = UIDTestUtils.getAttributeColor(activity, R.attr.uidDatePickerAndroidNormalContentTextColor);
        onView(withId(titleId)).check(matches(ViewPropertiesMatchers.hasChildrensWithSameTextColor(color)));
    }

    private int getViewId(final String viewName) {
        return activity.getResources().getIdentifier(viewName, "id", "android");
    }
}
