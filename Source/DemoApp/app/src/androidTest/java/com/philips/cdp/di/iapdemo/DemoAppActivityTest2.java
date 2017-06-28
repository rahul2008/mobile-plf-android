package com.philips.cdp.di.iapdemo;


import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

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
public class DemoAppActivityTest2 {

    @Rule
    public ActivityTestRule<DemoAppActivity> mActivityTestRule = new ActivityTestRule<>(DemoAppActivity.class);

    @Test
    public void demoAppActivityTest2() {

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_register), withText("Register/Login"), isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction xProviderButton = onView(
                allOf(withId(R.id.btn_reg_my_philips),
                        withParent(allOf(withId(R.id.rl_reg_singin_options),
                                withParent(withId(R.id.ll_reg_root_container))))));
        xProviderButton.perform(scrollTo(), click());

        ViewInteraction xEditText = onView(
                allOf(withId(R.id.et_reg_email),
                        withParent(withId(R.id.rl_reg_parent_verified_field)),
                        isDisplayed()));
        xEditText.perform(replaceText("uc11@mailinator.com"), closeSoftKeyboard());

        ViewInteraction xEditText2 = onView(
                allOf(withId(R.id.et_reg_password),
                        withParent(withId(R.id.rl_reg_parent_verified_field)),
                        isDisplayed()));
        xEditText2.perform(replaceText("uc11@mailinator.com"), closeSoftKeyboard());

        ViewInteraction xButton = onView(
                allOf(withId(R.id.btn_reg_sign_in), withText("Log In"),
                        withParent(allOf(withId(R.id.fl_reg_sign_in),
                                withParent(withId(R.id.rl_reg_welcome_container))))));
        xButton.perform(scrollTo(), click());

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.rl_x_checkbox),
                        withParent(allOf(withId(R.id.reg_x_checkbox_parent),
                                withParent(allOf(withId(R.id.cb_reg_accept_terms), withText("android.widget.TextView{6256220 V.ED..... ......ID 81,8-81,59 #7f0f01da app:id/reg_tv_checkbox}"))))),
                        isDisplayed()));
        relativeLayout.perform(click());

        ViewInteraction xButton2 = onView(
                allOf(withId(R.id.reg_btn_continue), withText("Continue"),
                        withParent(allOf(withId(R.id.rl_reg_btn_continue_container),
                                withParent(withId(R.id.ll_reg_root_container))))));
        xButton2.perform(scrollTo(), click());

        ViewInteraction xButton3 = onView(
                allOf(withId(R.id.btn_reg_continue), withText("Continue"),
                        withParent(allOf(withId(R.id.fl_reg_sign_in),
                                withParent(withId(R.id.rl_reg_continue_id))))));
        xButton3.perform(scrollTo(), click());

    }

}
