/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.alert;


import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.activity.EmptyDialogFragment;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class EmptyDialogTest extends BaseTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    BaseTestActivity activity;
    private Resources testResources;

    @Before
    public void setUp() {
        activity = mActivityTestRule.launchActivity(getLaunchIntent(0));
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(new EmptyDialogFragment());
        testResources = activity.getResources();
    }

    @Test
    public void verifyDialogPositiveButtonMargin() {
        int expectedMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_alert_dialog_positive_button_margin_end);
        getPositiveButton().check(matches(ViewPropertiesMatchers.isSameStartMargin(expectedMargin)));
    }

    private ViewInteraction getPositiveButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_positive_button));
    }
}