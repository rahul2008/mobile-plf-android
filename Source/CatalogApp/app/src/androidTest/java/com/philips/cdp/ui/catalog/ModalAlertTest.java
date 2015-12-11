package com.philips.cdp.ui.catalog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.ModalAlertDemo;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsOpacityValueAsExpectedMatcher.isOpacityValueSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ModalAlertTest extends ActivityInstrumentationTestCase2<ModalAlertDemo> {

    private Resources testResources;
    private ModalAlertDemo modalAlertDemo;
    private ThemeUtils themeUtils;

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;

    public ModalAlertTest() {
        super(ModalAlertDemo.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        modalAlertDemo = getActivity();
        SharedPreferences preferences = modalAlertDemo.getSharedPreferences(modalAlertDemo.getString((R.string.app_name)), Context.MODE_PRIVATE);
        themeUtils = new ThemeUtils(preferences);
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    private void relaunchActivity() {
        Intent intent;
        modalAlertDemo.setResult(1);
        intent = new Intent(modalAlertDemo, ModalAlertDemo.class);
        modalAlertDemo.startActivity(intent);
        modalAlertDemo.finish();
    }

    public void testModalAlertDemonstrated(){
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogTitle)).check(matches(isDisplayed()));
    }

    public void testMASupportsControls(){
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogButtonCancel)).check(matches(isDisplayed()));
        onView(withId(R.id.dialogButtonOK)).check(matches(isDisplayed()));
    }

    public void testMAControlsFunctionality() {
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogButtonCancel)).perform(click());
        onView(withId(R.id.dialogButtonCancel)).check(doesNotExist());
    }

    public void testMAFontSizeofAlertTitle() {
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogTitle)).check(matches(isTextSizeSimilar((testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.modal_alert_title_size)))));
    }

    public void testMAFontSizeofDescription() {
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogDescription)).check(matches(isTextSizeSimilar((testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.modal_alert_description_buttontext_size)))));
    }

    public void testMAFontSizeofButtonText() {
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogButtonCancel)).check(matches(isTextSizeSimilar((testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.modal_alert_description_buttontext_size)))));
    }

    public void testMAFontColorofAlertTitleDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogTitle)).check(matches(isTextColorSimilar("#03478")));
    }

    public void testMAFontColorofAlertTitleBOTheme() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogTitle)).check(matches(isTextColorSimilar("#e98300")));
    }

    public void testMAFontColorofAlertTitleBATheme() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogTitle)).check(matches(isTextColorSimilar("#1e9d8b")));
    }

    public void testMAFontColorofAlertDescriptionDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogDescription)).check(matches(isTextColorSimilar("#03478")));
    }

    public void testMAFontColorofAlertDescriptionBOTheme() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogDescription)).check(matches(isTextColorSimilar("#e9830")));
    }

    public void testMAFontColorofAlertDescriptionBATheme() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogDescription)).check(matches(isTextColorSimilar("#1e9d8b")));
    }

    public void testMAFontColorofFocussedButtonBATheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogButtonOK)).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testMAOutlineColorofButtonDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogButtonCancel)).check(matches(isOutlineColorSimilar("#03478")));
        onView(withId(R.id.dialogButtonCancel)).check(matches(isOpacityValueSimilar(128, 0, 5)));
    }

    public void testMAOutlineColorofButtonBOTheme() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogButtonCancel)).check(matches(isOutlineColorSimilar("#e9830")));
        onView(withId(R.id.dialogButtonCancel)).check(matches(isOpacityValueSimilar(128, 0, 5)));
    }

    public void testMAOutlineColorofButtonBATheme() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogButtonCancel)).check(matches(isOutlineColorSimilar("#1e9d8b")));
        onView(withId(R.id.dialogButtonCancel)).check(matches(isOpacityValueSimilar(128, 0, 5)));
    }

    public void testMAColorofFocusedButtonDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogButtonOK)).check(matches(isBackgroundColorSimilar("#03478", 5, 5)));
    }

    public void testMAColorofFocusedButtonBOTheme() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogButtonOK)).check(matches(isBackgroundColorSimilar("#e9830",5,5)));
    }

    public void testMAColorofFocusedButtonBATheme() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogButtonOK)).check(matches(isBackgroundColorSimilar("#1e9d8b",5,5)));
    }

    public void testMAHeightofButton() {
        onView(withId(R.id.show_modal_dialog)).perform(click());
        onView(withId(R.id.dialogButtonCancel)).check(matches(isHeightSimilar((int)(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.button_size)))));
        onView(withId(R.id.dialogButtonOK)).check(matches(isHeightSimilar((int)(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.button_size)))));
    }



}
