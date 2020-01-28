/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.sampledigitalcare.automation;

import android.graphics.Point;
import android.os.RemoteException;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static com.philips.cdp.sampledigitalcare.automation.Matchers.withRecyclerView;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DigitalCareAutomationTest extends AutomationTestHelper{

    private int millis = 1000;

    @Rule
    public ActivityTestRule<CCDemoUAppActivity> mActivityTestRule = new ActivityTestRule<>(CCDemoUAppActivity.class);

    @Before
    public void init(){
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Point[] coordinates = new Point[4];
        coordinates[0] = new Point(248, 1520);
        coordinates[1] = new Point(248, 929);
        coordinates[2] = new Point(796, 1520);
        coordinates[3] = new Point(796, 929);
        try {
            if (!uiDevice.isScreenOn()) {
                uiDevice.wakeUp();
                uiDevice.swipe(coordinates, 10);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Ignore
    @Test
    public void viewProductInformationTest() {
        testViewProductInformationFlow();
    }

    /*@Test
    public void faqTest() {
        testFaqFlow();
    }*/

    @Ignore
    @Test
    public void contactUsTest() {
        testContactUsFlow();
    }

    @Ignore
    @Test
    public void tellUsWhatYouThinkTest() {
        testTellUsWhatYouThinkTestFlow();
    }

    @Ignore
    @Test
    public void selectProductTest() {
        testSelectedProduct(6);
    }

    private void testTellUsWhatYouThinkTestFlow() {
        testSelectedProduct(4);
        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.supportMenuContainer),
                        withParent(withId(R.id.optionParent)),
                        isDisplayed()));
        if(exists(recyclerView2))
            recyclerView2.perform(actionOnItemAtPosition(4, click()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.tellus_PhilipsReviewButton)));
        if(exists(button2))
          button2.perform(scrollTo(), click());

        pressBack();
        pressBack();

       /* ViewInteraction button3 = onView(
                allOf(withId(R.id.tellus_PlayStoreReviewButton)));
        button3.perform(scrollTo(), click());*/
    }

    private void testContactUsFlow() {
        testSelectedProduct(2);
        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.supportMenuContainer),
                        withParent(withId(R.id.optionParent)),
                        isDisplayed()));
        if(exists(recyclerView2))
            recyclerView2.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.contactUsChat),
                        withParent(allOf(withId(R.id.contactUsHelpParent),
                                withParent(withId(R.id.contactUsParent))))));
        if(exists(button3))
          button3.perform(scrollTo(), click());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.chatNow),
                        withParent(withId(R.id.chatNowParentPort))));
        if(exists(button4))
          button4.perform(scrollTo(), click());

        pressBack();

        pressBack();

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.contactUsSocialProvideButtonsParent),
                        withParent(allOf(withId(R.id.contactUsSocialParent),
                                withParent(withId(R.id.contactUsParent))))));
        if(exists(recyclerView3))
          recyclerView3.perform(actionOnItemAtPosition(0, click()));

        pressBack();

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.contactUsSocialProvideButtonsParent),
                        withParent(allOf(withId(R.id.contactUsSocialParent),
                                withParent(withId(R.id.contactUsParent))))));
        if(exists(recyclerView4)){
            recyclerView4.perform(actionOnItemAtPosition(1, click()));
        }
        pressBack();

        ViewInteraction recyclerView5 = onView(
                allOf(withId(R.id.contactUsSocialProvideButtonsParent),
                        withParent(allOf(withId(R.id.contactUsSocialParent),
                                withParent(withId(R.id.contactUsParent))))));
        if(exists(recyclerView5)){
            recyclerView5.perform(actionOnItemAtPosition(2, click()));
        }
        return;
    }
    private void testFaqFlow() {
        testSelectedProduct(1);
        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.supportMenuContainer),
                        withParent(withId(R.id.optionParent)),
                        isDisplayed()));
        if(exists(recyclerView2)){
            recyclerView2.perform(actionOnItemAtPosition(1, click()));
        }
        sleepTwoSec();
        onView(withRecyclerView(R.id.faq_list_item_recycle_view).atPosition(0)).perform(click());
        assertWebViewDisplayed();

    }

    private void testViewProductInformationFlow() {
        testSelectedProduct(0);
        boolean isNetworkSuccess = false;
        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.supportMenuContainer),
                        withParent(withId(R.id.optionParent)),
                        isDisplayed()));
        if(exists(recyclerView2))
            recyclerView2.perform(actionOnItemAtPosition(0, click()));
        sleepTwoSec();

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.prodbuttonsParent),
                        withParent(withId(R.id.products_layout))));

        if(exists(recyclerView4)){
            isNetworkSuccess = true;
            recyclerView4.perform(actionOnItemAtPosition(0, click()));
        }else{
            sleepFourSec();
            if(exists(recyclerView4))
                recyclerView4.perform(actionOnItemAtPosition(0, click()));
        }

        if(exists(recyclerView4) && !isNetworkSuccess){
            isNetworkSuccess = true;
            recyclerView4.perform(actionOnItemAtPosition(0, click()));
        }else{
            sleepFourSec();
            if(exists(recyclerView4)){
                recyclerView4.perform(actionOnItemAtPosition(0, click()));
            }else{
                if(!isNetworkSuccess)
                    return;
            }
        }
        sleepTwoSec();

        onView(withRecyclerView(R.id.faq_list_item_recycle_view).atPosition(0)).perform(click());
        assertWebViewDisplayed();
        pressBack();
        pressBack();
        pressBack();
        pressBack();

    }

    private DigitalCareAutomationTest pressBack() {
        androidx.test.espresso.Espresso.pressBack();
        return waiting(millis);
    }

    private DigitalCareAutomationTest assertWebViewDisplayed() {
        assertDisplayed(R.id.webViewParent);
        return waiting(millis);
    }

    private DigitalCareAutomationTest assertDisplayed(int... ids) {
        for (int id : ids) {
            onView(withId(id)).check(matches(isDisplayed()));
        }
        return waiting(millis);

    }

    private DigitalCareAutomationTest waiting(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

   /* @Test
    public void changeThemeTest() {
        ViewInteraction button = onView(
                allOf(withId(R.id.change_theme), withText("Change Theme")));
        button.perform(scrollTo(), click());

    }*/

}
