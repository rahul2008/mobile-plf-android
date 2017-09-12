package com.philips.platform.prdemoapp;


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
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PRDemoActivityTest {

    @Rule
    public ActivityTestRule<PRDemoActivity> mActivityTestRule = new ActivityTestRule<>(PRDemoActivity.class);

    @Test
    public void pRDemoActivityTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.launch_pr_demo_app_button), withText("Launch PR Demo App"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_product_registration), withText("Product Registration")));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.edt_ctn));
        appCompatEditText.perform(scrollTo(), replaceText("HC5410/83"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(R.id.pr_activity_a), withText("App set up flow as activity")));
        button.perform(scrollTo(), click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.prg_welcomeScreen_yes_button), withText("Yes, extend my warranty")));
        button2.perform(scrollTo(), click());

        ViewInteraction button3 = onView(
                allOf(withId(R.id.pr_activity_b), withText("User initiation flow as activity")));
        button3.perform(scrollTo(), click());

        pressBack();

        ViewInteraction appCompatImageButton = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")),
                        withParent(allOf(withId(R.id.uid_toolbar),
                                withParent(withId(R.id.appBar)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        pressBack();

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.btn_user_registration), withText("User Registration")));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction login = onView(
                allOf(withId(R.id.usr_startScreen_Login_Button), withText("Log in"),
                        withParent(allOf(withId(R.id.usr_startScreen_baseLayout_ConstraintLayout),
                                withParent(withId(R.id.sv_root_layout))))));
        login.perform(scrollTo(), click());

        /*ViewInteraction loginIdEditText = onView(
                allOf(withId(R.id.usr_loginScreen_login_textField),
                        withParent(allOf(withId(R.id.ll_reg_create_account_fields),
                                withParent(withId(R.id.ll_reg_root_container))))));
        loginIdEditText.perform(scrollTo(), click());

        ViewInteraction xEditText = onView(
                allOf(withId(R.id.et_reg_email),
                        withParent(withId(R.id.rl_reg_parent_verified_field)),
                        isDisplayed()));
        xEditText.perform(click());

        ViewInteraction loginIdEditText2 = onView(
                allOf(withId(R.id.usr_loginScreen_login_textField),
                        withParent(allOf(withId(R.id.ll_reg_create_account_fields),
                                withParent(withId(R.id.ll_reg_root_container))))));
        loginIdEditText2.perform(scrollTo(), click());

        ViewInteraction loginIdEditText3 = onView(
                allOf(withId(R.id.usr_loginScreen_login_textField),
                        withParent(allOf(withId(R.id.ll_reg_create_account_fields),
                                withParent(withId(R.id.ll_reg_root_container))))));
        loginIdEditText3.perform(scrollTo(), click());

        ViewInteraction loginIdEditText4 = onView(
                allOf(withId(R.id.usr_loginScreen_login_textField),
                        withParent(allOf(withId(R.id.ll_reg_create_account_fields),
                                withParent(withId(R.id.ll_reg_root_container))))));
        loginIdEditText4.perform(scrollTo(), click());*/

        ViewInteraction xEditText3 = onView(
                allOf(withId(R.id.et_reg_email),
                        withParent(withId(R.id.rl_reg_parent_verified_field)),
                        isDisplayed()));
        xEditText3.perform(replaceText("lift56@mailinator.com"));

        ViewInteraction pwd = onView(
                allOf(withId(R.id.usr_loginScreen_password_textField),
                        withParent(allOf(withId(R.id.ll_reg_create_account_fields),
                                withParent(withId(R.id.ll_reg_root_container))))));
        pwd.perform(scrollTo(), click());

        ViewInteraction pwd1 = onView(
                allOf(withId(R.id.usr_loginScreen_password_textField),
                        withParent(allOf(withId(R.id.ll_reg_create_account_fields),
                                withParent(withId(R.id.ll_reg_root_container))))));
        pwd1.perform(scrollTo(), click());


        ViewInteraction passwordView = onView(
                allOf(withId(R.id.usr_loginScreen_password_textField),
                        withParent(allOf(withId(R.id.ll_reg_create_account_fields),
                                withParent(withId(R.id.ll_reg_root_container))))));
        passwordView.perform(replaceText("Product56"));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction loginButton = onView(
                allOf(withId(R.id.uid_progress_indicator_button_button), withText("Log in"),
                        withParent(allOf(withId(R.id.uid_progress_indicator_button_layout),
                                withParent(allOf(withId(R.id.usr_loginScreen_login_button), withText("Log in"))))),
                        isDisplayed()));
        loginButton.perform(click());

        ViewInteraction checkBox5 = onView(
                allOf(withId(R.id.usr_almostDoneScreen_termsAndConditions_checkBox), withText("Sure, I accept the app's \nTerms & Conditions"),
                        withParent(allOf(withId(R.id.usr_almostDoneScreen_rootContainer_linearLayout),
                                withParent(withId(R.id.usr_almostDoneScreen_rootLayout_scrollView))))));
        checkBox5.perform(scrollTo(), click());

//        ViewInteraction button3 = onView(
//                allOf(withId(R.id.uid_progress_indicator_button_button), withText("Continue"),
//                        withParent(allOf(withId(R.id.uid_progress_indicator_button_layout),
//                                withParent(allOf(withId(R.id.usr_almostDoneScreen_continue_button), withText("Continue")))))));
//        button3.perform(scrollTo(), click());

        ViewInteraction xButton3 = onView(
                allOf(withId(R.id.btn_reg_continue), withText("Continue"),
                        withParent(allOf(withId(R.id.fl_reg_sign_in),
                                withParent(withId(R.id.rl_reg_continue_id))))));
        xButton3.perform(scrollTo(), click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.btn_product_registration), withText("Product Registration")));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.edt_ctn));
        appCompatEditText2.perform(scrollTo(), replaceText("Hc5410/83"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edt_ctn), withText("Hc5410/83")));
        appCompatEditText3.perform(scrollTo(), click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.edt_ctn), withText("Hc5410/83")));
        appCompatEditText4.perform(pressImeActionButton());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.edt_ctn), withText("Hc5410/83")));
        appCompatEditText5.perform(scrollTo(), click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.edt_ctn), withText("Hc5410/83")));
        appCompatEditText6.perform(scrollTo(), replaceText("HC5410/83"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.edt_ctn), withText("HC5410/83")));
        appCompatEditText7.perform(pressImeActionButton());

        ViewInteraction button4 = onView(
                allOf(withId(R.id.pr_activity_a), withText("App set up flow as activity")));
        button4.perform(scrollTo(), click());

        ViewInteraction button5 = onView(
                allOf(withId(R.id.prg_welcomeScreen_yes_button), withText("Yes")));
        button5.perform(scrollTo(), click());

        ViewInteraction validationEditText = onView(
                allOf(withId(R.id.prg_registerScreen_dateOfPurchase_validationEditText),
                        withParent(allOf(withId(R.id.prg_registerScreen_dateOfPurchase_validationLayout),
                                withParent(withId(R.id.prg_registerScreen_dateOfPurchase_Layout)))),
                        isDisplayed()));
        validationEditText.perform(longClick());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction button6 = onView(
                allOf(withId(R.id.prg_registerScreen_register_button), withText("Register")));
        button6.perform(scrollTo(), click());

        ViewInteraction button7 = onView(
                allOf(withId(R.id.continueButton), withText("Continue"),
                        withParent(withId(R.id.successLayout))));
        button7.perform(scrollTo(), click());

        ViewInteraction button8 = onView(
                allOf(withId(R.id.pr_activity_b), withText("User initiation flow as activity")));
        button8.perform(scrollTo(), click());

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(android.R.id.content),
                                0),
                        0),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction button9 = onView(
                allOf(withId(R.id.button_continue), withText("Continue"), isDisplayed()));
        button9.perform(click());

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
