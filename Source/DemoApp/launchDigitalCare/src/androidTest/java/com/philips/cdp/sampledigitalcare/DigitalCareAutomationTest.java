package com.philips.cdp.sampledigitalcare;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.philips.cdp.sampledigitalcare.launcher.uAppComponetLaunch.MicroAppLauncher;
import com.philips.cl.di.dev.pa.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.cdp.sampledigitalcare.Matchers.withRecyclerView;
import static org.hamcrest.CoreMatchers.anything;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DigitalCareAutomationTest {
    private int millis = 1000;
    @Rule
    public ActivityTestRule<MicroAppLauncher> mActivityTestRule = new ActivityTestRule<>(MicroAppLauncher.class);

    @Test
    public void productInformationTest() {
        // launch screen
        clickOn(R.id.launchAsFragment).clickAtPosition(R.id.supportMenuContainer, 0);

        // assert screen and elements displayed
        assertDisplayed(R.id.products_layout, R.id.productimage, R.id.prodbuttonsParent, R.id.videoContainerParent);

        // check clickable item behaviors
        // 1.FAQ page
        clickAtPosition(R.id.prodbuttonsParent, 0).assertDisplayed(R.id.faq_list_recycle_view).pressBack();

        // 2.Product information at philips.com
        clickAtPosition(R.id.prodbuttonsParent, 1).assertWebViewDisplayed().pressBack();
        // 3.play videos
        //clickOn(R.id.videoPlay).assertDisplayed();

    }

    @Test
    public void faqScreenTest() {
        // launch screen
        clickOn(R.id.launchAsFragment).clickAtPosition(R.id.supportMenuContainer, 1);
        // assert screen and elements displayed
        assertDisplayed(R.id.faq_list_recycle_view);

        onView(withRecyclerView(R.id.faq_list_item_recycle_view).atPosition(0)).perform(click());
        assertWebViewDisplayed();
    }

    @Test
    public void contactUsTest() {
        // launch screen
        clickOn(R.id.launchAsFragment).clickAtPosition(R.id.supportMenuContainer, 2);
        // assert screen and elements displayed
        assertDisplayed(R.id.contactUsCall, R.id.contactUsChat, R.id.contactUsSocialParent, R.id.contactUsSocialProvideButtonsParent);
        // check clickable item behaviors
        // tap on call us
        //clickOn(R.id.contactUsCall);
        //pressBack();

        //tap on live chat
        clickOn(R.id.contactUsChat).assertDisplayed(R.id.chatnow_bg, R.id.chatNow);
        // click chat now
        clickOn(R.id.chatNow).assertWebViewDisplayed().pressBack().pressBack();

        clickAtPosition(R.id.contactUsSocialProvideButtonsParent, 0).pressBack();
//        clickAtPosition(R.id.contactUsSocialProvideButtonsParent, 1).pressBack();
//        clickAtPosition(R.id.contactUsSocialProvideButtonsParent, 2).pressBack();
    }

    @Test
    public void opinionOverviewTest() {
        // launch screen
        clickOn(R.id.launchAsFragment).clickAtPosition(R.id.supportMenuContainer, 4);
        // assert screen and elements displayed
        assertDisplayed(R.id.tellus_container, R.id.tellus_bg, R.id.tellus_PhilipsReviewButton, R.id.tellus_PlayStoreReviewButton);
        // check clickable item behaviors
        // 1.Write a review
        clickOn(R.id.tellus_PhilipsReviewButton).assertWebViewDisplayed();
        //pressBack();
        // 2.Rate this app
        //clickOn(R.id.tellus_PlayStoreReviewButton);
    }

    @Test
    public void selectProductTest() {
        // launch screen
        clickOn(R.id.launchAsFragment).clickAtPosition(R.id.supportMenuContainer, 6);
        // assert screen and elements displayed
        waiting(millis * 30).assertDisplayed(R.id.productListContainer, R.id.productListView);
        // check clickable item behaviors
        // tap to select product
        onData(anything()).inAdapterView(withId(R.id.productListView)).atPosition(0).perform(click());

        assertDisplayed(R.id.detailed_screen_parent, R.id.detailed_screen_parent_image, R.id.detailedscreen_pager, R.id.detailedscreen_select_button);
        // select product
        clickOn(R.id.detailedscreen_select_button).assertDisplayed(R.id.savedscreen_button_continue, R.id.savedScreen_screen_parent);
        // confirm selection
        clickOn(R.id.savedscreen_button_continue).assertDisplayed(R.id.supportMenuContainer);
    }

    private DigitalCareAutomationTest waiting(long millis) {
         /*Added a sleep statement to match the app's execution delay.
         The recommended way to handle such scenarios is to use Espresso idling resources:
         https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html*/
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    private DigitalCareAutomationTest pressBack() {
        android.support.test.espresso.Espresso.pressBack();
        return waiting(millis);
    }

    private DigitalCareAutomationTest clickOn(int id) {
        onView(withId(id)).perform(scrollTo(), click());
        return waiting(millis);
    }

    private DigitalCareAutomationTest clickAtPosition(int id, int index) {
        onView(withId(id)).perform(actionOnItemAtPosition(index, click()));
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
}

