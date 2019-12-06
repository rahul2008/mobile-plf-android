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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EncryptWithOldSSAndDecryptWithNewSSDataTest extends SecureStorageBaseTest{

    @Rule
    public ActivityTestRule<AppInfraLaunchActivity> mActivityTestRule = new ActivityTestRule<>(AppInfraLaunchActivity.class);

    @Test
    public void encryptWithOldSSAndDecryptWithNewSSDataTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        setUp();
        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.listViewAppInfraComponents),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                2)))
                .atPosition(0);
        linearLayout.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.EncryptDecrypt), withText("Encrypt and Decrypt"),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction toggleButton = onView(
                allOf(withId(R.id.toggleButton), withText("OFF"),
                        isDisplayed()));
        toggleButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.plainText),
                        childAtPosition(
                                allOf(withId(R.id.SCROLLER_ID3),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                0)));
        appCompatEditText.perform(scrollTo(), replaceText("app"), closeSoftKeyboard());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.plainText), withText("app"),
                        childAtPosition(
                                allOf(withId(R.id.SCROLLER_ID3),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                0)));
        appCompatEditText2.perform(scrollTo(), replaceText("app infra"));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.plainText), withText("app infra"),
                        childAtPosition(
                                allOf(withId(R.id.SCROLLER_ID3),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(closeSoftKeyboard());


        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.buttonEncrypt), withText("Encrypt Data"),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction toggleButton2 = onView(
                allOf(withId(R.id.toggleButton), withText("ON"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1),
                        isDisplayed()));
        toggleButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonDecrypt), withText("Decrypt Data"),
                        isDisplayed()));
        appCompatButton3.perform(click());

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
