package com.philips.cdp.sampledigitalcare;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.philips.cdp.sampledigitalcare.launcher.uAppComponetLaunch.MicroAppLauncher;
import com.philips.cl.di.dev.pa.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DigitalCareAutomationTest {

    @Rule
    public ActivityTestRule<MicroAppLauncher> mActivityTestRule = new ActivityTestRule<>(MicroAppLauncher.class);

    @Test
    public void LiveChatTest() {
        ViewInteraction button = onView(withId(R.id.launchAsFragment));
        button.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(withId(R.id.supportMenuContainer));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction button2 = onView(withId(R.id.contactUsChat));
        button2.perform(scrollTo(), click());

        ViewInteraction button3 = onView(withId(R.id.chatNow));
        button3.perform(scrollTo(), click());
    }

    @Test
    public void productInformationTest() {
        // launch product information screen
        clickOn(R.id.launchAsFragment).clickAtPosition(R.id.supportMenuContainer,0);

        // assert screen and elements displayed
        assertDisplayed(R.id.products_layout, R.id.productimage, R.id.prodbuttonsParent, R.id.videoContainerParent);

        // check clickable item behaviors
        // 1.FAQ page
        // 2.Product information at philips.com
        // clickAtPosition(R.id.prodbuttonsParent,0).assertDisplayed(R.id.faq_list_recycle_view);
        // tap on back
    }

    @Test
    public void faqScreenTest(){

    }

    @Test
    public void opinionOverviewTest(){

    }

    private DigitalCareAutomationTest clickOn(int id) {
        onView(withId(id)).perform(scrollTo(), click());
        return this;
    }

    private DigitalCareAutomationTest clickAtPosition(int id,int index) {
        onView(withId(id)).perform(actionOnItemAtPosition(index, click()));
        return this;
    }

    private DigitalCareAutomationTest assertDisplayed(int... ids) {
        for (int id : ids) {
            onView(withId(id)).check(matches(isDisplayed()));
        }
        return this;
    }
}
