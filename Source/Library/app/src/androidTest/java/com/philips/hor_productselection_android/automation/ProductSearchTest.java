package com.philips.hor_productselection_android.automation;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.philips.hor_productselection_android.Launcher;
import com.philips.hor_productselection_android.R;

import org.junit.Ignore;
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
public class ProductSearchTest extends AutomationTestHelper{

    @Rule
    public ActivityTestRule<Launcher> mActivityTestRule = new ActivityTestRule<>(Launcher.class);

    @Ignore
    @Test
    public void productSearchTest() {

        ViewInteraction button = onView(
                allOf(ViewMatchers.withId(R.id.buttonActivity), withText("launch as Activity"), isDisplayed()));
        button.perform(click());
        sleepEightSec();

        ViewInteraction button2 = onView(
                allOf(withId(R.id.find_product_btn), withText("Find product"),
                        withParent(withId(R.id.welcome_screen_parent_one))));
        sleepTwoSec();
        if(exists(button2)){
            button2.perform(scrollTo(), click());
        }

        ViewInteraction customSearchView = onView(
                allOf(withId(R.id.search_box),
                        withParent(allOf(withId(R.id.productListContainer),
                                withParent(withId(R.id.mainContainer)))),
                        isDisplayed()));
        sleepTwoSec();
        customSearchView.perform(replaceText("shaver"), closeSoftKeyboard());

        sleepTwoSec();
        ViewInteraction customSearchView2 = onView(
                allOf(withId(R.id.search_box), withText("shaver"),
                        withParent(allOf(withId(R.id.productListContainer),
                                withParent(withId(R.id.mainContainer)))),
                        isDisplayed()));
        customSearchView2.perform(click());

        ViewInteraction customSearchView3 = onView(
                allOf(withId(R.id.search_box), withText("shaver"),
                        withParent(allOf(withId(R.id.productListContainer),
                                withParent(withId(R.id.mainContainer)))),
                        isDisplayed()));
        sleepTwoSec();
        customSearchView3.perform(click());

        ViewInteraction customSearchView4 = onView(
                allOf(withId(R.id.search_box), withText("shaver"),
                        withParent(allOf(withId(R.id.productListContainer),
                                withParent(withId(R.id.mainContainer)))),
                        isDisplayed()));
        sleepTwoSec();
        customSearchView4.perform(replaceText("shaver"), closeSoftKeyboard());

        ViewInteraction customSearchView5 = onView(
                allOf(withId(R.id.search_box),
                        withParent(allOf(withId(R.id.productListContainer),
                                withParent(withId(R.id.mainContainer)))),
                        isDisplayed()));
        customSearchView5.perform(click());

        ViewInteraction customSearchView6 = onView(
                allOf(withId(R.id.search_box),
                        withParent(allOf(withId(R.id.productListContainer),
                                withParent(withId(R.id.mainContainer)))),
                        isDisplayed()));
        sleepTwoSec();
        customSearchView6.perform(replaceText("guuu55"), closeSoftKeyboard());

        sleepTwoSec();
        ViewInteraction customSearchView7 = onView(
                allOf(withId(R.id.search_box), withText("guuu55"),
                        withParent(allOf(withId(R.id.productListContainer),
                                withParent(withId(R.id.mainContainer)))),
                        isDisplayed()));
        customSearchView7.perform(click());

        sleepTwoSec();
        ViewInteraction customSearchView9 = onView(
                allOf(withId(R.id.search_box), withText("guuu55"),
                        withParent(allOf(withId(R.id.productListContainer),
                                withParent(withId(R.id.mainContainer)))),
                        isDisplayed()));
        sleepTwoSec();
        customSearchView9.perform(click());

        ViewInteraction customSearchView10 = onView(
                allOf(withId(R.id.search_box),
                        withParent(allOf(withId(R.id.productListContainer),
                                withParent(withId(R.id.mainContainer)))),
                        isDisplayed()));
        sleepTwoSec();
        customSearchView10.perform(click());

        ViewInteraction customSearchView11 = onView(
                allOf(withId(R.id.search_box),
                        withParent(allOf(withId(R.id.productListContainer),
                                withParent(withId(R.id.mainContainer)))),
                        isDisplayed()));
        customSearchView11.perform(click());

        ViewInteraction customSearchView12 = onView(
                allOf(withId(R.id.search_box),
                        withParent(allOf(withId(R.id.productListContainer),
                                withParent(withId(R.id.mainContainer)))),
                        isDisplayed()));
        customSearchView12.perform(click());

        sleepTwoSec();
        ViewInteraction customSearchView14 = onView(
                allOf(withId(R.id.search_box),
                        withParent(allOf(withId(R.id.productListContainer),
                                withParent(withId(R.id.mainContainer)))),
                        isDisplayed()));
        sleepTwoSec();
        customSearchView14.perform(replaceText("aven"), closeSoftKeyboard());

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.productselection_ratingtheme),
                        childAtPosition(
                                allOf(withId(R.id.productListView),
                                        withParent(withId(R.id.productListContainer))),
                                0),
                        isDisplayed()));
        sleepTwoSec();
        relativeLayout.perform(click());

        ViewInteraction button3 = onView(
                allOf(withId(R.id.detailedscreen_select_button), withText("Select this product"),
                        withParent(allOf(withId(R.id.detailed_screen_parent_one),
                                withParent(withId(R.id.detailed_screen_parent))))));
        sleepTwoSec();
        button3.perform(scrollTo(), click());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.savedscreen_button_continue), withText("Continue"),
                        withParent(allOf(withId(R.id.bottom_layout_container),
                                withParent(withId(R.id.savedScreen_screen_parent_one))))));
        button4.perform(scrollTo(), click());


    }
}
