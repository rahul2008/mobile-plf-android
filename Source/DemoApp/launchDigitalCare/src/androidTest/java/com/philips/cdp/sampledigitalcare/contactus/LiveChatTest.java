package com.philips.cdp.sampledigitalcare.contactus;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.philips.cdp.sampledigitalcare.launcher.uAppComponetLaunch.MicroAppLauncher;
import com.philips.cl.di.dev.pa.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LiveChatTest {

    @Rule
    public ActivityTestRule<MicroAppLauncher> mActivityTestRule = new ActivityTestRule<>(MicroAppLauncher.class);

    @Test
    public void microAppLauncherTest() {
        ViewInteraction button = onView(withId(R.id.launchAsFragment));
        button.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(withId(R.id.optionContainer));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction button2 = onView(withId(R.id.contactUsChat));
        button2.perform(scrollTo(), click());

        ViewInteraction button3 = onView(withId(R.id.chatNow));
        button3.perform(scrollTo(), click());
    }
}
