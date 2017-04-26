package com.philips.cdp.di.iapdemo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class IAPActivityTest {
    @Rule
    public ActivityTestRule<IAPActivity> mActivityRule =
            new ActivityTestRule<>(IAPActivity.class);


    @Test
    public void ensureTextChangesWork() {
        // Type text and then press the button.
        onView(withId(R.id.iap_header_title))
                .perform(typeText("HELLO"), closeSoftKeyboard());

        // Check that the text was changed.
        onView(withId(R.id.iap_header_title)).check(matches(withText("Lalala")));
    }
}
