package com.philips.platform.prdemoapp;


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
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PRDemoActivityTestRegister {

    @Rule
    public ActivityTestRule<PRDemoActivity> mActivityTestRule = new ActivityTestRule<>(PRDemoActivity.class);

//    @Test
    public void testProductRegisteredSuccessOnAppSetUpFlow() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.launch_pr_demo_app_button), withText("Launch PR Demo App"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_product_registration), withText("Product Registration")));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.edt_ctn));
        appCompatEditText.perform(scrollTo(), replaceText("HC5410/83"), closeSoftKeyboard());

        ViewInteraction appCompatDateEditText = onView(
                withId(R.id.edt_purchase_date));
        appCompatDateEditText.perform(scrollTo(), replaceText("2017-09-11"), closeSoftKeyboard());

        String serialNumber = "user"+ System.currentTimeMillis();

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.edt_serial_number));
        appCompatEditText2.perform(scrollTo(), replaceText(serialNumber), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edt_serial_number), withText(serialNumber)));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edt_serial_number), withText(serialNumber)));
        appCompatEditText4.perform(pressImeActionButton());

        ViewInteraction button = onView(
                allOf(withId(R.id.pr_activity_a), withText("App set up flow as activity")));
        button.perform(scrollTo(), click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.prg_welcomeScreen_yes_button), withText("Yes, extend my warranty")));
        button2.perform(scrollTo(), click());


        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction button3 = onView(
                allOf(withId(R.id.prg_registerScreen_register_button), withText("Register")));
        button3.perform(scrollTo(), click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction button4 = onView(
                allOf(withText("Continue"),
                        withParent(withId(R.id.successLayout))));
        button4.perform(scrollTo(), click());

    }


    @Test
    public void testProductRegisteredSuccessOnUserInitiationFlow() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.launch_pr_demo_app_button), withText("Launch PR Demo App"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_product_registration), withText("Product Registration")));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.edt_ctn));
        appCompatEditText.perform(scrollTo(), replaceText("HC5410/83"), closeSoftKeyboard());

        ViewInteraction appCompatDateEditText = onView(
                withId(R.id.edt_purchase_date));
        appCompatDateEditText.perform(scrollTo(), replaceText("2017-09-11"), closeSoftKeyboard());

        String serialNumber = "user"+ System.currentTimeMillis();

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.edt_serial_number));
        appCompatEditText2.perform(scrollTo(), replaceText(serialNumber), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edt_serial_number), withText(serialNumber)));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edt_serial_number), withText(serialNumber)));
        appCompatEditText4.perform(pressImeActionButton());

        ViewInteraction button = onView(
                allOf(withId(R.id.pr_activity_b), withText("User initiation flow as activity")));
        button.perform(scrollTo(), click());


        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ViewInteraction button3 = onView(
                allOf(withId(R.id.prg_registerScreen_register_button), withText("Register")));
        button3.perform(scrollTo(), click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction button4 = onView(
                allOf(withText("Continue"),
                        withParent(withId(R.id.successLayout))));
        button4.perform(scrollTo(), click());

    }

}
