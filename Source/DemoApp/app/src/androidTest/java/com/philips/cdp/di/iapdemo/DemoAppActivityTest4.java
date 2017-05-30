package com.philips.cdp.di.iapdemo;


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
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DemoAppActivityTest4 {

    @Rule
    public ActivityTestRule<DemoAppActivity> mActivityTestRule = new ActivityTestRule<>(DemoAppActivity.class);

    @Test
    public void demoAppActivityTest4() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_register), withText("Register/Login"), isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
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
        xEditText.perform(replaceText("pabitra@grr.la"), closeSoftKeyboard());

        ViewInteraction xEditText2 = onView(
                allOf(withId(R.id.et_reg_password),
                        withParent(withId(R.id.rl_reg_parent_verified_field)),
                        isDisplayed()));
        xEditText2.perform(replaceText("pabitra@grr.la"), closeSoftKeyboard());

        ViewInteraction xIconTextView = onView(
                allOf(withId(R.id.tv_password_mask), withText("\uE619"),
                        withParent(allOf(withId(R.id.ll_alignment_lay),
                                withParent(withId(R.id.rl_reg_parent_verified_field)))),
                        isDisplayed()));
        xIconTextView.perform(click());

        ViewInteraction xButton = onView(
                allOf(withId(R.id.btn_reg_sign_in), withText("Log In"),
                        withParent(allOf(withId(R.id.fl_reg_sign_in),
                                withParent(withId(R.id.rl_reg_welcome_container))))));
        xButton.perform(scrollTo(), click());

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.rl_x_checkbox),
                        withParent(allOf(withId(R.id.reg_x_checkbox_parent),
                                withParent(allOf(withId(R.id.cb_reg_accept_terms), withText("android.widget.TextView{6377f39 V.ED..... ......ID 93,10-93,67 #7f0f0208 app:id/reg_tv_checkbox}"))))),
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

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.et_add_ctn),
                        withParent(withId(R.id.ll_ctn)),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.et_add_ctn),
                        withParent(withId(R.id.ll_ctn)),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("hx9042/64"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_add_ctn), withText("Add"),
                        withParent(withId(R.id.ll_ctn)),
                        isDisplayed()));
        appCompatButton2.perform(click());

        pressBack();

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btn_categorized_shop_now), withText("Shop Now(Categorized)"), isDisplayed()));
        appCompatButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.btn_register), withText("Register/Login"), isDisplayed()));
        appCompatButton4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction xButton4 = onView(
                allOf(withId(R.id.btn_reg_sign_out), withText("Log Out"),
                        withParent(allOf(withId(R.id.fl_reg_logout),
                                withParent(withId(R.id.rl_reg_continue_id))))));
        xButton4.perform(scrollTo(), click());

        ViewInteraction xTextView = onView(
                allOf(withId(R.id.tv_country_displat), withText("India"),
                        withParent(allOf(withId(R.id.reg_country_selection),
                                withParent(withId(R.id.ll_reg_create_account_container))))));
        xTextView.perform(scrollTo(), click());

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        withId(R.id.reg_country_picker_listview),
                        53),
                        isDisplayed()));
        linearLayout.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction xProviderButton2 = onView(
                allOf(withId(R.id.btn_reg_my_philips),
                        withParent(allOf(withId(R.id.rl_reg_singin_options),
                                withParent(withId(R.id.ll_reg_root_container))))));
        xProviderButton2.perform(scrollTo(), click());

        ViewInteraction xEditText3 = onView(
                allOf(withId(R.id.et_reg_email),
                        withParent(withId(R.id.rl_reg_parent_verified_field)),
                        isDisplayed()));
        xEditText3.perform(replaceText("pabitra@grr.la"), closeSoftKeyboard());

        ViewInteraction xEditText4 = onView(
                allOf(withId(R.id.et_reg_password),
                        withParent(withId(R.id.rl_reg_parent_verified_field)),
                        isDisplayed()));
        xEditText4.perform(replaceText("pabitra@grr.la"), closeSoftKeyboard());

        ViewInteraction xIconTextView2 = onView(
                allOf(withId(R.id.tv_password_mask), withText("\uE619"),
                        withParent(allOf(withId(R.id.ll_alignment_lay),
                                withParent(withId(R.id.rl_reg_parent_verified_field)))),
                        isDisplayed()));
        xIconTextView2.perform(click());

        ViewInteraction xButton5 = onView(
                allOf(withId(R.id.btn_reg_sign_in), withText("Log In"),
                        withParent(allOf(withId(R.id.fl_reg_sign_in),
                                withParent(withId(R.id.rl_reg_welcome_container))))));
        xButton5.perform(scrollTo(), click());

        ViewInteraction xButton6 = onView(
                allOf(withId(R.id.btn_reg_continue), withText("Continue"),
                        withParent(allOf(withId(R.id.fl_reg_sign_in),
                                withParent(withId(R.id.rl_reg_continue_id))))));
        xButton6.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.et_add_ctn),
                        withParent(withId(R.id.ll_ctn)),
                        isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.et_add_ctn),
                        withParent(withId(R.id.ll_ctn)),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("hx9042/64"), closeSoftKeyboard());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.btn_add_ctn), withText("Add"),
                        withParent(withId(R.id.ll_ctn)),
                        isDisplayed()));
        appCompatButton5.perform(click());

        pressBack();

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.btn_categorized_shop_now), withText("Shop Now(Categorized)"), isDisplayed()));
        appCompatButton6.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

        ViewInteraction button = onView(
                allOf(withId(R.id.iap_productDetailsScreen_buyFromRetailor_button), withText("Buy Now")));
        button.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.iap_retailer_list),
                        withParent(allOf(withId(R.id.ratingtheme),
                                withParent(withId(R.id.fl_mainFragmentContainer)))),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

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
