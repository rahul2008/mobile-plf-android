package com.philips.cdp.di.iapdemo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.philips.cdp.di.iap.activity.IAPActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class IAPActivityTest {
    @Rule
    public ActivityTestRule<IAPActivity> mActivityRule =
            new ActivityTestRule<>(IAPActivity.class);


    @Test
    public void ensureTextChangesWork() {
        // Type text and then press the button.
//        onView(withId(R.id.iap_header_title))
//                .perform(typeText("HELLO"), closeSoftKeyboard());

        // Check that the text was changed.
//        onView(withId(R.id.iap_header_title)).check(matches(withText("Lalala")));
    }
}
