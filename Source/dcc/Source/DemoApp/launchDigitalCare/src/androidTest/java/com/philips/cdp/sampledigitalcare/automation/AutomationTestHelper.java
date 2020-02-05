package com.philips.cdp.sampledigitalcare.automation;

import androidx.test.espresso.AmbiguousViewMatcherException;
import androidx.test.espresso.NoMatchingRootException;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.philips.platform.ccdemouapplibrary.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsInstanceOf.any;


/**
 * Created by philips on 6/5/17.
 */
@Ignore
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AutomationTestHelper {

    protected boolean exists(ViewInteraction interaction) {
        try {
            interaction.perform(new ViewAction() {
                @Override public Matcher<View> getConstraints() {
                    return any(View.class);
                }
                @Override public String getDescription() {
                    return "check for existence";
                }
                @Override public void perform(UiController uiController, View view) {
                    // no op, if this is run, then the execution will continue after .perform(...)
                }
            });
            return true;
        } catch (AmbiguousViewMatcherException ex) {
            // if there's any interaction later with the same matcher, that'll fail anyway
            return true; // we found more than one
        } catch (NoMatchingViewException ex) {
            return false;
        } catch (NoMatchingRootException ex) {
            // optional depending on what you think "exists" means
            return false;
        }
    }

    protected static Matcher<View> childAtPosition(
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


    protected void testSelectedProduct(int position) {

        boolean isNetworkAvailable = false;
        ViewInteraction button = onView(
                allOf(withId(R.id.launchAsFragment)));
        button.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.supportMenuContainer),
                        withParent(withId(R.id.optionParent)),
                        isDisplayed()));

        if(exists(recyclerView)){
            isNetworkAvailable = true;
            recyclerView.perform(actionOnItemAtPosition(position, click()));
            sleepEightSec();
            sleepFourSec();
        }

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.welcome_screen_parent_one),
                        withParent(withId(R.id.welcome_screen_parent_one))));
        sleepSixSec();
        if(exists(appCompatButton)){
            isNetworkAvailable = true;
            appCompatButton.perform(scrollTo(), click());
        }else{
            sleepFourSec();
            if(exists(appCompatButton)) {
                isNetworkAvailable = true;
                appCompatButton.perform(scrollTo(), click());
            }
            sleepFourSec();
            if(exists(appCompatButton)) {
                isNetworkAvailable = true;
                appCompatButton.perform(scrollTo(), click());
            }
        }
        if(!isNetworkAvailable){
            return;
        }
        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.productListView),
                        withParent(withId(R.id.productListContainer)),
                        isDisplayed()));

        if(exists(recyclerView2)){
            if(position == 6){
                onData(anything()).inAdapterView(withId(R.id.productListView)).atPosition(1).perform(click());
            }else{
                onData(anything()).inAdapterView(withId(R.id.productListView)).atPosition(0).perform(click());
            }
        }else{
            return;
        }

        sleepTwoSec();
        ViewInteraction selectThisProductBtn = onView(
                allOf(withId(R.id.detailedscreen_select_button)));

        if(exists(selectThisProductBtn)){
            selectThisProductBtn.perform(scrollTo(), click());
        }

        sleepTwoSec();
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.savedscreen_button_continue),
                        withParent(allOf(withId(R.id.savedScreen_screen_parent),
                                withParent(withId(R.id.savedScreen_screen_parent_one))))));

        if(exists(appCompatButton2)){
            appCompatButton2.perform(scrollTo(), click());
        }
    }

    protected void sleepFourSec() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void sleepSixSec() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void sleepEightSec() {
        try {
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void sleepTwoSec() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
