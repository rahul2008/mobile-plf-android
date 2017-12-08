/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.alert;


import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.LinearLayout;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.activity.DialogTestFragment;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.LayoutPropertiesMatcher;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class DialogDividerTest extends BaseTest {
    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class, false, false);
    BaseTestActivity activity;
    Resources testResources;

    @Before
    public void setUp() {
        activity = mActivityTestRule.launchActivity(getLaunchIntent(0));
        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(DialogTestFragment.create());
        testResources = activity.getResources();
    }

    @Test
    public void verifyDialogTopDividerHeight() {

        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.dialog_divider_height);
        getDialogTopDivider().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyDialogBottomDividerHeight() {

        int expectedHeight = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.dialog_divider_height);
        getDialogBottomDivider().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyDialogPositiveButtonTopMargin() {

        int expectedMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertaction_buttons_padding);
        getPositiveButton().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedMargin)));
    }

    @Test
    public void verifyDialogNegativeButtonTopMargin() {

        int expectedMargin = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.alertaction_buttons_padding);
        getNegativeButton().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedMargin)));
    }

    @Test
    public void verifyDialogActionAreaIsVertical() {

        int expectedOrientation = LinearLayout.VERTICAL;
        getDialogActionArea().check(matches(LayoutPropertiesMatcher.isSameOrientation(expectedOrientation)));
    }

    @Test
    public void verifyFunctionalityOfAlternateButton() {

        getAlternateButton().perform(ViewActions.click());
        onView(withId(com.philips.platform.uid.test.R.id.uid_alert)).check(doesNotExist());
    }

    private ViewInteraction getDialogTopDivider() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_top_divider));
    }

    private ViewInteraction getDialogBottomDivider() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_bottom_divider));
    }

    private ViewInteraction getAlternateButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_alternate_button));
    }

    private ViewInteraction getDialogActionArea() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_control_area));
    }

    private ViewInteraction getPositiveButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_positive_button));
    }

    private ViewInteraction getNegativeButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.uid_dialog_negative_button));
    }
}
