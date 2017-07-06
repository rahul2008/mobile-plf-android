package com.philips.cdp.di.iapdemo;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.philips.cdp.di.iapdemo.idlingResources.BtnRegMyPhilipsIdlingResource;
import com.philips.cdp.di.iapdemo.idlingResources.BtnRegisterLoginIdlingResource;
import com.philips.cdp.di.iapdemo.idlingResources.BtnSignIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@Ignore
@RunWith(AndroidJUnit4.class)
public class LaunchUserFlowE2E {

    @Rule
    public ActivityTestRule<LuncherActivity> mActivityTestRule = new ActivityTestRule<>(LuncherActivity.class);

    @Test
    public void launchUserFlowE2E() {
        ViewInteraction button = onView(
                allOf(withId(R.id.btn_launch), withText("Launch Activity"), isDisplayed()));
        button.perform(click());
        IdlingPolicies.setMasterPolicyTimeout(
                1000 * 30, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(
                1000 * 30, TimeUnit.MILLISECONDS);

        BtnRegisterLoginIdlingResource btnRegisterLoginIdlingResource = new BtnRegisterLoginIdlingResource();
        Espresso.registerIdlingResources(btnRegisterLoginIdlingResource);


        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_register), withText("Register/Login"), isDisplayed()));
        appCompatButton.perform(click());

        Espresso.unregisterIdlingResources(btnRegisterLoginIdlingResource);

        BtnRegMyPhilipsIdlingResource btnRegMyPhilipsIdlingResource = new BtnRegMyPhilipsIdlingResource();
        Espresso.registerIdlingResources(btnRegMyPhilipsIdlingResource);

        ViewInteraction xProviderButton = onView(
                allOf(withId(R.id.btn_reg_my_philips),
                        withParent(allOf(withId(R.id.rl_reg_singin_options),
                                withParent(withId(R.id.ll_reg_root_container))))));

        xProviderButton.perform(scrollTo(), click());

        Espresso.unregisterIdlingResources(btnRegMyPhilipsIdlingResource);

        ViewInteraction xEditText2 = onView(
                allOf(withId(R.id.et_reg_email),
                        withParent(withId(R.id.rl_reg_parent_verified_field)),
                        isDisplayed()));
        xEditText2.perform(replaceText("pabitra@grr.la"), closeSoftKeyboard());

        ViewInteraction xEditText3 = onView(
                allOf(withId(R.id.et_reg_password),
                        withParent(withId(R.id.rl_reg_parent_verified_field)),
                        isDisplayed()));
        xEditText3.perform(replaceText("pabitra@grr.la"), closeSoftKeyboard());


        BtnSignIdlingResource btnSignIdlingResource = new BtnSignIdlingResource();
        Espresso.registerIdlingResources(btnSignIdlingResource);

        ViewInteraction xButton = onView(
                allOf(withId(R.id.btn_reg_sign_in), withText("Log In"),
                        withParent(allOf(withId(R.id.fl_reg_sign_in),
                                withParent(withId(R.id.rl_reg_welcome_container))))));
        xButton.perform(scrollTo(), click());


        //Check Terms & Cindition and Press Continue
        onView(withId(R.id.cb_reg_accept_terms)).perform(click());


        Espresso.unregisterIdlingResources(btnSignIdlingResource);

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

        ViewInteraction textView = onView(
                allOf(withId(R.id.iap_header_title), withText("Carrier App"), isDisplayed()));
        textView.check(matches(withText("Carrier App")));

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.et_add_ctn),
                        withParent(withId(R.id.ll_ctn)),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.et_add_ctn),
                        withParent(withId(R.id.ll_ctn)),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("hx8332/11"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_add_ctn), withText("Add"),
                        withParent(withId(R.id.ll_ctn)),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btn_categorized_shop_now), withText("Shop Now(Categorized)"), isDisplayed()));
        appCompatButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView1 = onView(
                allOf(withId(R.id.iap_header_title), withText("Products"),
                        childAtPosition(
                                allOf(withId(R.id.ratingthememain),
                                        childAtPosition(
                                                withId(R.id.action_bar),
                                                0)),
                                2),
                        isDisplayed()));
        textView1.check(matches(withText("Products")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.tv_ctn), withText("HX8332/11"),
                        childAtPosition(
                                allOf(withId(R.id.product_catalog_parent_layout),
                                        childAtPosition(
                                                withId(R.id.product_catalog_recycler_view),
                                                0)),
                                3),
                        isDisplayed()));
        textView2.check(matches(withText("HX8332/11")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.product_catalog_recycler_view),
                        withParent(allOf(withId(R.id.layout),
                                withParent(withId(R.id.fl_mainFragmentContainer)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.iap_header_title), withText("Sonicare AirFloss Ultra Interdental cleaner"),
                        childAtPosition(
                                allOf(withId(R.id.ratingthememain),
                                        childAtPosition(
                                                withId(R.id.action_bar),
                                                0)),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("Sonicare AirFloss Ultra Interdental cleaner")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.ctn), withText("HX8332/11"),
                        childAtPosition(
                                allOf(withId(R.id.detail_layout),
                                        childAtPosition(
                                                withId(R.id.scrollView),
                                                0)),
                                3),
                        isDisplayed()));
        textView4.check(matches(withText("HX8332/11")));

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.buy_from_retailor), withText("Buy Now"),
                        withParent(allOf(withId(R.id.detail_layout),
                                withParent(withId(R.id.scrollView))))));
        appCompatButton4.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.iap_header_title), withText("Retailer"),
                        childAtPosition(
                                allOf(withId(R.id.ratingthememain),
                                        childAtPosition(
                                                withId(R.id.action_bar),
                                                0)),
                                2),
                        isDisplayed()));
        textView5.check(matches(withText("Retailer")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.iap_header_title), withText("Retailer"),
                        childAtPosition(
                                allOf(withId(R.id.ratingthememain),
                                        childAtPosition(
                                                withId(R.id.action_bar),
                                                0)),
                                2),
                        isDisplayed()));
        textView6.check(matches(withText("Retailer")));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.iap_retailer_list),
                        withParent(allOf(withId(R.id.ratingtheme),
                                withParent(withId(R.id.fl_mainFragmentContainer)))),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction frameLayout = onView(
                allOf(withId(R.id.iap_header_back_button),
                        withParent(allOf(withId(R.id.ratingthememain),
                                withParent(withId(R.id.action_bar)))),
                        isDisplayed()));
        frameLayout.perform(click());

        ViewInteraction frameLayout2 = onView(
                allOf(withId(R.id.iap_header_back_button),
                        withParent(allOf(withId(R.id.ratingthememain),
                                withParent(withId(R.id.action_bar)))),
                        isDisplayed()));
        frameLayout2.perform(click());

        ViewInteraction frameLayout3 = onView(
                allOf(withId(R.id.iap_header_back_button),
                        withParent(allOf(withId(R.id.ratingthememain),
                                withParent(withId(R.id.action_bar)))),
                        isDisplayed()));
        frameLayout3.perform(click());

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
