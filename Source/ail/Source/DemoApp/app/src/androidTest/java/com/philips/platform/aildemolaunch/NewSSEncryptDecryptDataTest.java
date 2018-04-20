package com.philips.platform.aildemolaunch;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NewSSEncryptDecryptDataTest {

    @Rule
    public ActivityTestRule<AppInfraLaunchActivity> mActivityTestRule = new ActivityTestRule<>(AppInfraLaunchActivity.class);

    @Test
    public void newSSEncryptDecryptDataTest() {

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.listViewAppInfraComponents),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                2)))
                .atPosition(0);
        linearLayout.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.EncryptDecrypt), withText("Encrypt and Decrypt"),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.plainText),
                        childAtPosition(
                                allOf(withId(R.id.SCROLLER_ID3),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                0)));
        appCompatEditText.perform(scrollTo(), replaceText("app infra"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.buttonEncrypt), withText("Encrypt Data"),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonDecrypt), withText("Decrypt Data"),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textViewDataMatched), withText("True"),
                        isDisplayed()));
        textView.check(matches(withText("True")));

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
