/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.demoapp;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.philips.platform.appinfra.demoapp.AppInfraLaunchActivity;
import com.philips.platform.appinfra.demoapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/***
 * Description: Test bulk data encryption with new SS
 *
 * 1. Tap on Secure Storage from main menu
 * 2. Tap on Secure storage button
 * 3. Tap on Bulk
 * 4. Tap on store bulk
 * 5. Tap on read bulk
 * 6. Tap on encypt bulk
 * 7. Tap on decrypt bulk
 * Assertion : Check whether Text Match toast is coming or not
 *
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class NewSSEncyrptDecryptBulkDataTest extends SecureStorageBaseTest{

    @Rule
    public ActivityTestRule<AppInfraLaunchActivity> mActivityTestRule = new ActivityTestRule<>(AppInfraLaunchActivity.class);

    @Test
    public void newSSEncyrptDecryptBulkDataTest() {
        setUp();
        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.listViewAppInfraComponents),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                2)))
                .atPosition(0);
        linearLayout.perform(click());


        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.secureStorage), withText("Secure Storage"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.bulkTest_button), withText("Bulk "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                3),
                        isDisplayed()));
        appCompatButton2.perform(click());


        ViewInteraction button2 = onView(
                allOf(withId(R.id.store_bulk_btn), withText("Store Bulk"),
                        childAtPosition(
                                allOf(withId(R.id.button_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        button2.perform(click());

        ViewInteraction button3 = onView(
                allOf(withId(R.id.read_bulk_btn), withText("Read  Bulk"),
                        childAtPosition(
                                allOf(withId(R.id.button_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        button3.perform(click());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.store_bulk_encrypt_btn), withText("encrypt Bulk"),
                        childAtPosition(
                                allOf(withId(R.id.button_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        button4.perform(click());

        ViewInteraction button5 = onView(
                allOf(withId(R.id.read_bulk_decrypt_btn), withText("decrypt  Bulk"),
                        childAtPosition(
                                allOf(withId(R.id.button_bar),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                0)),
                                3),
                        isDisplayed()));
        button5.perform(click());


        onView(withText("Text Match")).inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
