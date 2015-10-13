package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsPixelAsExpectedMatcher.isImageSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;
/*

*
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.

*/

public class OutlineButtonTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public OutlineButtonTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testOButtonIsHeightAsExpected() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.button_size))));
    }

    public void testDBThemeOButtonOutlineColourAsExpected() {
        // Apply Bright blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isOutlineColorSimilar("#03478")));
    }

    public void testDBThemeOButtonTextColor() {
        // Apply Bright blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isTextColorSimilar("#03478")));
    }

    public void testDBThemeOButtonBGColor() {
        // Apply Bright blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
    }

    public void testBOThemeOButtonOutlineColorAsExpected() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isOutlineColorSimilar("#e9830")));
    }

    public void testBOThemeOButtonTextColor() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isTextColorSimilar("#e9830")));
    }

    public void testBOThemeOButtonBGColor() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
    }

    public void testBAThemeOButtonOutlineColorAsExpected() {
        // Apply Bright Aqua theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isOutlineColorSimilar("#1e9d8b")));
    }

    public void testBAThemeOButtonTextColor() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isTextColorSimilar("#1e9d8b")));
    }

    public void testBAThemeOButtonBGColor() {
        // Apply Bright Aqua theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
    }

}
