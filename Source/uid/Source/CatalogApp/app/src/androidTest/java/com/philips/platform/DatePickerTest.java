/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform;

import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class DatePickerTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class, false, false);
    private MainActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent intent = new Intent();
        activity = activityTestRule.launchActivity(intent);
    }

    @Test
    public void verifyCalenderIconDisplayed() throws Exception {
        onView(withText("Pickers")).perform(ViewActions.click());

        onView(withId(R.id.calender)).check(matches(isDisplayed()));
    }

    @Test
    public void verifyRangePickerStartCalenderIconDisplayed() throws Exception {
        onView(withText("Pickers")).perform(ViewActions.click());

        onView(withId(R.id.rangePickerStartCalender)).check(matches(isDisplayed()));
    }

    @Test
    public void verifyRangePickerEndCalenderIconDisplayed() throws Exception {
        onView(withText("Pickers")).perform(ViewActions.click());

        onView(withId(R.id.rangePickerEndCalender)).check(matches(isDisplayed()));
    }

    @Test
    public void verifyTimeslotCalenderIconDisplayed() throws Exception {
        onView(withText("Pickers")).perform(ViewActions.click());

        onView(withId(R.id.timeslotCalender)).check(matches(isDisplayed()));
    }

    @Test
    public void verifyDatePickerIsDisplayed() throws Exception {
        onView(withText("Pickers")).perform(ViewActions.click());

        onView(withId(R.id.datePicker)).perform(ViewActions.click());

        onView(withId(getViewId("date_picker_header"))).check(matches(isDisplayed()));
    }

    @Test
    public void verifyTimePickerIsDisplayed() throws Exception {
        onView(withText("Pickers")).perform(ViewActions.click());

        onView(withId(R.id.timePicker)).perform(ViewActions.click());

        onView(withId(getViewId("time_header"))).check(matches(isDisplayed()));
    }

    @Test
    public void verifyDatePickerIsDisplayedForRangeStartDate() throws Exception {
        onView(withText("Pickers")).perform(ViewActions.click());

        onView(withId(R.id.startDatePicker)).perform(ViewActions.click());

        onView(withId(getViewId("date_picker_header"))).check(matches(isDisplayed()));
    }

    @Test
    public void verifyTimePickerIsDisplayedForRangeStartTime() throws Exception {
        onView(withText("Pickers")).perform(ViewActions.click());

        onView(withId(R.id.startTimePicker)).perform(ViewActions.click());

        onView(withId(getViewId("time_header"))).check(matches(isDisplayed()));
    }

    @Test
    public void verifyDatePickerIsDisplayedForRangeEndDate() throws Exception {
        onView(withText("Pickers")).perform(ViewActions.click());

        onView(withId(R.id.endDatePicker)).perform(ViewActions.click());

        onView(withId(getViewId("date_picker_header"))).check(matches(isDisplayed()));
    }

    @Test
    public void verifyTimePickerIsDisplayedForRangeEndTime() throws Exception {
        onView(withText("Pickers")).perform(ViewActions.click());

        onView(withId(R.id.endTimePicker)).perform(ViewActions.click());

        onView(withId(getViewId("time_header"))).check(matches(isDisplayed()));
    }

    @Test
    public void verifyTimePickerIsDisplayedForTimeslotPicker() throws Exception {
        onView(withText("Pickers")).perform(ViewActions.click());

        onView(withId(R.id.timeslotPicker)).perform(ViewActions.click());

        onView(withId(getViewId("time_header"))).check(matches(isDisplayed()));
    }

    private int getViewId(final String viewName) {
        return activity.getResources().getIdentifier(viewName, "id", "android");
    }
}
