package com.philips.cdp.di.iapdemo;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.philips.cdp.registration.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class DemoAppActivityTest {
    private User mUser;
    private static final String CONTENT_COLOR_KEY = "CONTENT_COLOR_KEY";
    @Rule
    public ActivityTestRule<DemoAppActivity> mActivityRule =
            new ActivityTestRule<>(DemoAppActivity.class);

    @Before
    public void setUp() {
        mUser = new User(mActivityRule.getActivity());
    }

    @Test
    public void testClickUserRegisterationButton() {


        if (!mUser.isUserSignIn()) {
            onView(withId(R.id.btn_register)).perform(click());
            onView(withId(R.id.btn_reg_my_philips)).perform(click());
            pauseTestFor(10000);

            ViewInteraction emailEditText = onView(
                    allOf(withId(R.id.et_reg_email), isDisplayed()));
            emailEditText.perform(click(), typeTextIntoFocusedView("uc11@mailinator.com"));
            ViewInteraction pwdEditText = onView(
                    allOf(withId(R.id.et_reg_password), isDisplayed()));
            pwdEditText.perform(click(), typeTextIntoFocusedView("uc11@mailinator.com"));

            onView(withId(R.id.btn_reg_sign_in)).perform(click());
            pauseTestFor(10000);
            //Check Terms & Cindition and Press Continue
            onView(withId(R.id.cb_reg_accept_terms)).perform(click());
            onView(withId(R.id.rl_reg_btn_continue_container)).perform(click());
            onView(withId(R.id.rl_reg_continue_id)).perform(click());
            pauseTestFor(100);

            //onData(anything()).inAdapterView(withId(R.id.product_catalog_recycler_view)).atPosition(0).perform(click());
        }


    }


    @Test
    public void testIfUserRegisterationDone() {


        if (!mUser.isUserSignIn()) {
            onView(withId(R.id.btn_register)).perform(click());
            onView(withId(R.id.btn_reg_my_philips)).perform(click());
            pauseTestFor(10000);

            ViewInteraction emailEditText = onView(
                    allOf(withId(R.id.et_reg_email), isDisplayed()));
            emailEditText.perform(click(), typeTextIntoFocusedView("uc11@mailinator.com"));
            ViewInteraction pwdEditText = onView(
                    allOf(withId(R.id.et_reg_password), isDisplayed()));
            pwdEditText.perform(click(), typeTextIntoFocusedView("uc11@mailinator.com"));

            onView(withId(R.id.btn_reg_sign_in)).perform(click());
            pauseTestFor(10000);
            //Check Terms & Cindition and Press Continue
            onView(withId(R.id.cb_reg_accept_terms)).perform(click());
            onView(withId(R.id.rl_reg_btn_continue_container)).perform(click());
            onView(withId(R.id.rl_reg_continue_id)).perform(click());
            pauseTestFor(100);

            //onData(anything()).inAdapterView(withId(R.id.product_catalog_recycler_view)).atPosition(0).perform(click());
        } else {
            //Enter CTN and Click on Add Button
            // ViewInteraction ctnEditText = onView(withId(R.id.et_add_ctn));
            onView(withId(R.id.et_add_ctn)).check(matches(withHint("Enter CTN"))).perform(click(), typeText("HX8332/11"), closeSoftKeyboard());
            onView(withId(R.id.btn_add_ctn)).perform(click());
            onView(withId(R.id.btn_categorized_shop_now)).perform(click());
            pauseTestFor(5000);
           // onData(anything()).inAdapterView(withId(R.id.product_catalog_recycler_view)).atPosition(0).perform(click());
            onView(withId(R.id.product_catalog_recycler_view)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.buy_from_retailor)).perform(click());
        }
    }

    private void pauseTestFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}