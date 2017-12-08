//package com.philips.cdp.di.iapdemo;
//
//
//import android.support.test.espresso.Espresso;
//import android.support.test.espresso.IdlingPolicies;
//import android.support.test.espresso.ViewInteraction;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.test.suitebuilder.annotation.LargeTest;
//
//import com.philips.cdp.di.iapdemo.idlingResources.BtnRegMyPhilipsIdlingResource;
//import com.philips.cdp.di.iapdemo.idlingResources.BtnRegisterLoginIdlingResource;
//import com.philips.cdp.di.iapdemo.idlingResources.BtnSignIdlingResource;
//import com.philips.cdp.registration.User;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.concurrent.TimeUnit;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static android.support.test.espresso.action.ViewActions.replaceText;
//import static android.support.test.espresso.action.ViewActions.scrollTo;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withParent;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.allOf;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class LunchUserRegisterationToReachCarrierApp {
//    User user;
//    @Rule
//    public ActivityTestRule<LuncherActivity> mActivityTestRule = new ActivityTestRule<>(LuncherActivity.class);
//
//    @Before
//    public void setUp() {
//        user = new User(mActivityTestRule.getActivity());
//        IdlingPolicies.setMasterPolicyTimeout(
//                1000 * 30, TimeUnit.MILLISECONDS);
//        IdlingPolicies.setIdlingResourceTimeout(
//                1000 * 30, TimeUnit.MILLISECONDS);
//
//    }
//
//    @Test
//    public void lunchUserRegisterationToReachCarrierApp() {
//
//        if (user.isUserSignIn()) return;
//
//        ViewInteraction button = onView(
//                allOf(withId(R.id.btn_launch), withText("Launch Activity"), isDisplayed()));
//        button.perform(click());
//
//        BtnRegisterLoginIdlingResource btnRegisterLoginIdlingResource = new BtnRegisterLoginIdlingResource();
//        Espresso.registerIdlingResources(btnRegisterLoginIdlingResource);
//
//
//        ViewInteraction appCompatButton = onView(
//                allOf(withId(R.id.btn_register), withText("Register/Login"), isDisplayed()));
//        appCompatButton.perform(click());
//
//        Espresso.unregisterIdlingResources(btnRegisterLoginIdlingResource);
//
//        BtnRegMyPhilipsIdlingResource btnRegMyPhilipsIdlingResource = new BtnRegMyPhilipsIdlingResource();
//        Espresso.registerIdlingResources(btnRegMyPhilipsIdlingResource);
//
//        ViewInteraction xProviderButton = onView(
//                allOf(withId(R.id.btn_reg_my_philips),
//                        withParent(allOf(withId(R.id.rl_reg_singin_options),
//                                withParent(withId(R.id.ll_reg_root_container))))));
//
//        xProviderButton.perform(scrollTo(), click());
//
//        Espresso.unregisterIdlingResources(btnRegMyPhilipsIdlingResource);
//
//        ViewInteraction xEditText2 = onView(
//                allOf(withId(R.id.et_reg_email),
//                        withParent(withId(R.id.rl_reg_parent_verified_field)),
//                        isDisplayed()));
//        xEditText2.perform(replaceText("pabitra@grr.la"), closeSoftKeyboard());
//
//        ViewInteraction xEditText3 = onView(
//                allOf(withId(R.id.et_reg_password),
//                        withParent(withId(R.id.rl_reg_parent_verified_field)),
//                        isDisplayed()));
//        xEditText3.perform(replaceText("pabitra@grr.la"), closeSoftKeyboard());
//
//
//        BtnSignIdlingResource btnSignIdlingResource = new BtnSignIdlingResource();
//        Espresso.registerIdlingResources(btnSignIdlingResource);
//
//        ViewInteraction xButton = onView(
//                allOf(withId(R.id.btn_reg_sign_in), withText("Log In"),
//                        withParent(allOf(withId(R.id.fl_reg_sign_in),
//                                withParent(withId(R.id.rl_reg_welcome_container))))));
//        xButton.perform(scrollTo(), click());
//
//
//        //Check Terms & Cindition and Press Continue
//       // onView(withId(R.id.cb_reg_accept_terms)).perform(click());
//
//
//        Espresso.unregisterIdlingResources(btnSignIdlingResource);
//
//        ViewInteraction xButton2 = onView(
//                allOf(withId(R.id.reg_btn_continue), withText("Continue"),
//                        withParent(allOf(withId(R.id.rl_reg_btn_continue_container),
//                                withParent(withId(R.id.ll_reg_root_container))))));
//        xButton2.perform(scrollTo(), click());
//
//        ViewInteraction xButton3 = onView(
//                allOf(withId(R.id.btn_reg_continue), withText("Continue"),
//                        withParent(allOf(withId(R.id.fl_reg_sign_in),
//                                withParent(withId(R.id.rl_reg_continue_id))))));
//        xButton3.perform(scrollTo(), click());
//
////        ViewInteraction textView = onView(
////                allOf(withId(R.id.iap_header_title), withText("Carrier App"), isDisplayed()));
////        textView.check(matches(withText("Carrier App")));
//
//    }
//
//    @Test
//    public void launchProductCatalog(){
//        ViewInteraction button = onView(
//                allOf(withId(R.id.btn_launch), withText("Launch Activity"), isDisplayed()));
//        button.perform(click());
//
//    }
//
//
//}
