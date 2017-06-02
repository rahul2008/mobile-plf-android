package com.philips.hor_productselection_android;


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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddCTNTest {

    @Rule
    public ActivityTestRule<Launcher> mActivityTestRule = new ActivityTestRule<>(Launcher.class);

    @Test
    public void addCTNTest() {
        ViewInteraction actionButton = onView(
                allOf(withId(R.id.addimageButton), isDisplayed()));
        actionButton.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.ctn_editText), isDisplayed()));
        editText.perform(replaceText("100HC/20"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.category_editText), isDisplayed()));
        editText2.perform(replaceText("KHAN"), closeSoftKeyboard());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.catalog_editText), isDisplayed()));
        editText3.perform(replaceText("Maqsood"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(R.id.dialog_submit), withText("Submit"), isDisplayed()));
        button.perform(click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.buttonActivity), withText("launch as Activity"), isDisplayed()));
        button2.perform(click());
        sleepFourSec();

        ViewInteraction button3 = onView(
                allOf(withId(R.id.find_product_btn), withText("Find product"),
                        withParent(withId(R.id.welcome_screen_parent_one))));
        sleepTwoSec();
        button3.perform(scrollTo(), click());

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.productselection_ratingtheme),
                        childAtPosition(
                                allOf(withId(R.id.productListView),
                                        withParent(withId(R.id.productListContainer))),
                                0),
                        isDisplayed()));
        relativeLayout.perform(click());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.detailedscreen_select_button), withText("Select this product"),
                        withParent(allOf(withId(R.id.detailed_screen_parent_one),
                                withParent(withId(R.id.detailed_screen_parent))))));
        sleepTwoSec();
        button4.perform(scrollTo(), click());

        ViewInteraction button5 = onView(
                allOf(withId(R.id.savedscreen_button_continue), withText("Continue"),
                        withParent(allOf(withId(R.id.bottom_layout_container),
                                withParent(withId(R.id.savedScreen_screen_parent_one))))));
        button5.perform(scrollTo(), click());

    }

    private void sleepFourSec() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleepTwoSec() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
