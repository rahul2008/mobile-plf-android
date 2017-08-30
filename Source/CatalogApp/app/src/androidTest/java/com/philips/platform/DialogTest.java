package com.philips.platform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import com.philips.platform.catalogapp.MainActivity;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@Ignore
public class DialogTest {

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
    public void verifyDialogWithTitleAndIconAndLongText() throws Exception {
        onView(withText("Alert")).perform(ViewActions.click());

        checksaAlert(true, true, true);
    }

    @Test
    public void verifyDialogWithoutTitleAndShortMessage() throws Exception {
        onView(withText("Alert")).perform(ViewActions.click());
        setWithoutTitle();
        setShortLongContent();
        checksaAlert(false, false, false);
    }

    @Test
    public void verifyDialogWithTitleAndIconAndShortText() throws Exception {
        onView(withText("Alert")).perform(ViewActions.click());
        setShortLongContent();

        checksaAlert(true, true, false);
    }

    @Test
    public void verifyDialogWithTitleAndWithoutIconLongMessage() throws Exception {
        onView(withText("Alert")).perform(ViewActions.click());
        setWithoutIcon();

        checksaAlert(true, false, true);
    }

    @Test
    public void verifyDialogWithTitleAndWithoutIconAndShortMessage() throws Exception {
        onView(withText("Alert")).perform(ViewActions.click());
        setWithoutIcon();
        setShortLongContent();

        checksaAlert(true, false, false);
    }

    @Test
    public void verifyDialogWithoutTitleAndNoIconAndLongText() throws Exception {
        onView(withText("Alert")).perform(ViewActions.click());
        setWithoutTitle();

        checksaAlert(false, false, true);
    }

    void checksaAlert(boolean titleshown, boolean iconshown, boolean longtextshown) {
//        clickShowDialog();
//
//        onView(withId(R.id.uid_dialog_title)).check(matches(getMatcherBasedOnVisibility(titleshown)));
//        onView(withId(R.id.uid_dialog_icon)).check(matches(getMatcherBasedOnVisibility(iconshown)));
//        onView(withId(R.id.uid_alert_message)).check(matches(longtextshown ? ViewMatchers.withText(activity.getString(R.string.dialog_screen_long_content_text)) :
//                ViewMatchers.withText(activity.getString(R.string.dialog_screen_short_content_text))));
    }

    @NonNull
    private Matcher getMatcherBasedOnVisibility(final boolean titleshown) {
        return titleshown ? isDisplayed() : new IsNot(isDisplayed());
    }

    private void clickShowDialog() {
//        onView(withText(R.string.dialog_screen_button_text_dialog_with_text)).perform(ViewActions.click());
    }

    private void setWithoutIcon() {
//        onView(withText(R.string.dialog_screen_text_title_with_icon)).perform(ViewActions.click());
    }

    private void setWithoutTitle() {
//        onView(withText(R.string.dialog_screen_text_with_title)).perform(ViewActions.click());
    }

    private void setShortLongContent() {
//        onView(withText(R.string.dialog_screen_text_long_content)).perform(ViewActions.click());
    }
}
