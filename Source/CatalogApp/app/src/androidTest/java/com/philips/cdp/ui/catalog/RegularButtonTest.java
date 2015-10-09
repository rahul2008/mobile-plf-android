package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegularButtonTest extends ActivityInstrumentationTestCase2<MainActivity>{

    private Resources testResources;

    public RegularButtonTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testRButtonIsHeightAsExpected() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.theme_button))
                .check(matches(isHeightSimilar((int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.button_size))));
    }

    public void testDBThemeRButtonColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.theme_button))
                .check(matches(isBackgroundColorSimilar("#03478")));
    }

    public void testBOThemeRButtonColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.theme_button))
                .check(matches(isBackgroundColorSimilar("#e9830")));
    }

    public void testBAThemeRButtonColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        pressBack();
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.theme_button))
                .check(matches(isBackgroundColorSimilar("#1e9d8b")));
    }

    public void testDBThemeRButtonTextColor() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.theme_button))
                .check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testBOThemeRButtonTextColor() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.theme_button))
                .check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testBAThemeRButtonTextColor() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        pressBack();
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.theme_button))
                .check(matches(isTextColorSimilar("#ffffff")));
    }




    public void testActionSampleButtonTextSize() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.theme_button))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.theme_button_text_size))));
    }


}
