/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.sampledigitalcare.automation;


import androidx.test.espresso.ViewInteraction;
import androidx.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.philips.platform.ccdemouapp.CCDemoUAppActivity;
import com.philips.platform.ccdemouapplibrary.R;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@LargeTest
@Ignore
@RunWith(AndroidJUnit4.class)
public class FindNearByPhilipsAutomationTest extends AutomationTestHelper {

    private CCDemoUAppActivity activity;

    @Rule
    public ActivityTestRule<CCDemoUAppActivity> mActivityTestRule = new ActivityTestRule<>(CCDemoUAppActivity.class);

    @Before
    public void setUp() throws Exception {
        activity = mActivityTestRule.getActivity();
    }

    @Test
    public void microAppLauncherTest() throws InterruptedException {
        ViewInteraction button = onView(
                allOf(withId(R.id.launchAsFragment)));
        button.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.supportMenuContainer),
                        withParent(withId(R.id.optionParent)),
                        isDisplayed()));

        if(exists(recyclerView)){
            recyclerView.perform(actionOnItemAtPosition(3, click()));
            sleepEightSec();
        }

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.welcome_screen_parent_one),
                        withParent(withId(R.id.welcome_screen_parent_one))));
        sleepFourSec();
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
                allOf(withId(R.id.welcome_screen_parent_one), withText("Find product")));
        if(exists(appCompatButton)){
            appCompatButton.perform(scrollTo(), click());
        }

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.supportMenuContainer),
                        withParent(withId(R.id.optionParent)),
                        isDisplayed()));

        if(exists(recyclerView2)){
            recyclerView2.perform(actionOnItemAtPosition(3, click()));
        }
        sleepTwoSec();
        ViewInteraction selectThisProductBtn = onView(
                allOf(withId(R.id.detailedscreen_select_button), withText("Select this product")));

        if(exists(selectThisProductBtn)){
            selectThisProductBtn.perform(scrollTo(), click());
        }

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.savedscreen_button_continue), withText("Continue"),
                        withParent(allOf(withId(R.id.savedScreen_screen_parent),
                                withParent(withId(R.id.savedScreen_screen_parent_one))))));

        if(exists(appCompatButton2)){
            appCompatButton2.perform(scrollTo(), click());
        }

//Start
        ViewInteraction button1 = onView(
                allOf(withId(R.id.launchAsFragment), withText("Launch DigitalCare")));
        if(exists(button1)){
            button1.perform(scrollTo(), click());
        }

        ViewInteraction recyclerView1 = onView(
                allOf(withId(R.id.supportMenuContainer),
                        withParent(withId(R.id.optionParent)),
                        isDisplayed()));

        if(exists(recyclerView1)){
            recyclerView.perform(actionOnItemAtPosition(3, click()));
            sleepEightSec();
        }

        ViewInteraction relativeLayout1 = onView(
                allOf(withId(R.id.welcome_screen_parent_one),
                        withParent(withId(R.id.welcome_screen_parent_one))));
        sleepFourSec();
        if(exists(relativeLayout1)){
            relativeLayout.perform(scrollTo(), click());
        }

        ViewInteraction relativeLayout22 = onView(
                allOf(withId(R.id.productselection_ratingtheme),
                        childAtPosition(
                                allOf(withId(R.id.productListView),
                                        withParent(withId(R.id.productListContainer))),
                                2)));
        if(exists(relativeLayout22)){
            relativeLayout22.perform(scrollTo(), click());
        }

        ViewInteraction philipsAlertBtn = onView(allOf(withId(getViewId("uid_alert_positive_button")), withText("OK"),isDisplayed()));

        if (exists(philipsAlertBtn)) {
            philipsAlertBtn.perform(click());
        }

        ViewInteraction permissionAllowBtn = onView(
                allOf(withId(getViewId("permission_allow_button")), withText("ALLOW"),isDisplayed()));

        if (exists(permissionAllowBtn)) {
            permissionAllowBtn.perform(click());
        }
        sleepFourSec();

        ViewInteraction customSearchView = onView(
                allOf(withId(R.id.search_box),
                        withParent(withId(R.id.locate_search_layout)),
                        isDisplayed()));
        if(exists(customSearchView)){
            customSearchView.perform(replaceText("bangalo"), closeSoftKeyboard());
           sleepFourSec();
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
        sleepSixSec();

        if(exists(button3)){
            button3.perform(scrollTo(), click());
        }
        sleepFourSec();

        pressBack();
        sleepFourSec();

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.supportMenuContainer),
                        withParent(withId(R.id.optionParent)),
                        isDisplayed()));
        if(exists(recyclerView3)){
            recyclerView3.perform(actionOnItemAtPosition(3, click()));
        }
        sleepFourSec();

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
    private int getViewId(final String viewName) {
        return activity.getResources().getIdentifier(viewName, "id", "android");
    }

}
