package com.philips.cdp.sampledigitalcare.automation;


import android.support.test.espresso.AmbiguousViewMatcherException;
import android.support.test.espresso.NoMatchingRootException;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.philips.cdp.sampledigitalcare.launcher.uAppComponetLaunch.MicroAppLauncher;
import com.philips.cl.di.dev.pa.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsInstanceOf.any;


@LargeTest
@Ignore
@RunWith(AndroidJUnit4.class)
public class FindNearByPhilipsAutomationTest {

    @Rule
    public ActivityTestRule<MicroAppLauncher> mActivityTestRule = new ActivityTestRule<>(MicroAppLauncher.class);

    @Test
    public void microAppLauncherTest() throws InterruptedException {
        ViewInteraction button = onView(
                allOf(withId(R.id.launchAsFragment), withText("Launch DigitalCare")));
        button.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.supportMenuContainer),
                        withParent(withId(R.id.optionParent)),
                        isDisplayed()));

        if(exists(recyclerView)){
            recyclerView.perform(actionOnItemAtPosition(3, click()));
            Thread.sleep(8000);
        }

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.welcome_screen_parent_two),
                        withParent(withId(R.id.welcome_screen_parent_one))));
        Thread.sleep(5000);
        if(exists(relativeLayout)){
            relativeLayout.perform(scrollTo(), click());
        }

        ViewInteraction relativeLayout2 = onView(
                allOf(withId(R.id.productselection_ratingtheme),
                        childAtPosition(
                                allOf(withId(R.id.productListView),
                                        withParent(withId(R.id.productListContainer))),
                                2)));
        if(exists(relativeLayout2)){
            relativeLayout2.perform(scrollTo(), click());
        }

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.detailedscreen_select_button), withText("Select this product"),
                        withParent(allOf(withId(R.id.detailed_screen_parent_one),
                                withParent(withId(R.id.detailed_screen_parent))))));
        if(exists(appCompatButton)){
            appCompatButton.perform(scrollTo(), click());
        }

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.savedscreen_button_continue), withText("Continue"),
                        withParent(allOf(withId(R.id.savedScreen_screen_parent),
                                withParent(withId(R.id.savedScreen_screen_parent_one))))));

        if(exists(appCompatButton2)){
            appCompatButton2.perform(scrollTo(), click());
        }

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.supportMenuContainer),
                        withParent(withId(R.id.optionParent)),
                        isDisplayed()));

        if(exists(recyclerView2)){
            recyclerView2.perform(actionOnItemAtPosition(3, click()));
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction button2 = onView(
                allOf(withId(R.id.uid_alert_positive_button), withText("OK"),
                        withParent(allOf(withId(R.id.uid_alert_control_area),
                                withParent(withId(R.id.uid_alert)))),
                        isDisplayed()));

        if(exists(button2)){
            button2.perform(click());
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction customSearchView = onView(
                allOf(withId(R.id.search_box),
                        withParent(withId(R.id.locate_search_layout)),
                        isDisplayed()));
        if(exists(customSearchView)){
            customSearchView.perform(replaceText("bangalo"), closeSoftKeyboard());
            Thread.sleep(4000);
        }

        ViewInteraction relativeLayout3 = onView(
                allOf(childAtPosition(
                        withId(R.id.placelistview),
                        4),
                        isDisplayed()));
        if(exists(relativeLayout3)){
            relativeLayout3.perform(click());
        }
        // pressBack();
        ViewInteraction button3 = onView(
                allOf(withId(R.id.getdirection), withText("Get directions"),
                        withParent(allOf(withId(R.id.showlayout),
                                withParent(withId(R.id.locationDetailScroll))))));
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(exists(button3)){
            button3.perform(scrollTo(), click());
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.supportMenuContainer),
                        withParent(withId(R.id.optionParent)),
                        isDisplayed()));
        if(exists(recyclerView3)){
            recyclerView3.perform(actionOnItemAtPosition(3, click()));
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //pressBack();

        try {
            Thread.sleep(2142);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction button4 = onView(
                allOf(withId(R.id.call), withText("Call us 080  23159971"),
                        withParent(allOf(withId(R.id.showlayout),
                                withParent(withId(R.id.locationDetailScroll))))));

        if(exists(button4)){
            button4.perform(scrollTo(), click());
        }
    }

    private boolean exists(ViewInteraction interaction) {
        try {
            interaction.perform(new ViewAction() {
                @Override public Matcher<View> getConstraints() {
                    return any(View.class);
                }
                @Override public String getDescription() {
                    return "check for existence";
                }
                @Override public void perform(UiController uiController, View view) {
                }
            });
            return true;
        } catch (AmbiguousViewMatcherException ex) {
            return true;
        } catch (NoMatchingViewException ex) {
            return false;
        } catch (NoMatchingRootException ex) {
            return false;
        }
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
