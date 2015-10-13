package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsOpacityValueAsExpectedMatcher.isOpacityValueSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TransparentButtonTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public TransparentButtonTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testTButtonIsHeightAsExpected() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.button_size))));
    }

    public void testDBThemeTButtonOutlineColourAsExpected() {
        // Apply Bright blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isOutlineColorSimilar("#03478")));
    }

    public void testDBThemeTButtonTextColor() {
        // Apply Bright blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isTextColorSimilar("#03478")));
    }

    public void testDBThemeTButtonBGColor() {
        // Apply Bright blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isOpacityValueSimilar(0)));
    }

    public void testBOThemeTButtonOutlineColorAsExpected() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isOutlineColorSimilar("#e9830")));
    }

    public void testBOThemeTButtonTextColor() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isTextColorSimilar("#e9830")));
    }

    public void testBOThemeTButtonBGColor() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isOpacityValueSimilar(0)));
    }

    public void testBAThemeTButtonOutlineColorAsExpected() {
        // Apply Bright Aqua theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isOutlineColorSimilar("#1e9d8b")));
    }

    public void testBAThemeTButtonTextColor() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isTextColorSimilar("#1e9d8b")));
    }

    public void testBAThemeTButtonBGColor() {
        // Apply Bright Aqua theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isOpacityValueSimilar(0)));
    }
}

