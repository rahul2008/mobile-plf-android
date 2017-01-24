package com.philips.platform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.catalogapp.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityTest {

    private static final String CONTENT_COLOR_KEY = "CONTENT_COLOR_KEY";
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class, false, false);
    private MainActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent intent = getIntent(0);
        activity = activityTestRule.launchActivity(intent);
    }

    @NonNull
    private Intent getIntent(final int contentColorIndex) {
        final Bundle bundleExtra = new Bundle();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        bundleExtra.putInt(CONTENT_COLOR_KEY, contentColorIndex);
        intent.putExtras(bundleExtra);
        return intent;
    }

    @Test
    public void verifyButtonsPage() throws Exception {
        MainActivityTest.waitFor(activity, 4000);
        onView(withText("Buttons")).perform(ViewActions.click());
        MainActivityTest.waitFor(activity, 4000);
    }

    public static void waitFor(final Object object, final int seconds) {
        Thread thread = Thread.currentThread();
        synchronized (object) {
            try {
                object.wait(seconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
