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
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.IsPixelAsExpectedMatcher.isImageSimilar;
import static com.philips.cdp.ui.catalog.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;

/**
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

    public void testDBThemeOutlineButtonIsPixelPerfect() {
        onView(withText("Buttons")).perform(click());
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.outlinesbtn_regular_blue_mdpi);
        onView(withId(R.id.inverted_very_light_blue_button))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testBOThemeOutlineButtonIsPixelPerfect() {
        onView(withText("Buttons")).perform(click());
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.outlinebtn_regular_orange_mdpi);
        onView(withId(R.id.inverted_very_light_orange_button))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testDBThemeOButtonOutlineColourAsExpected() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.inverted_very_light_blue_button))
                .check(matches(isOutlineColorSimilar("#03478")));
    }

    public void testBOThemeOButtonOutlineColourAsExpected() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.inverted_very_light_orange_button))
              .check(matches(isOutlineColorSimilar("#e9830")));
    }

    public void testDBThemeOButtonTextColor() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.inverted_very_light_blue_button))
                .check(matches(isTextColorSimilar("#03478")));
    }

    public void testBOThemeOButtonTextColor() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.inverted_very_light_orange_button))
                .check(matches(isTextColorSimilar("#e9830")));
    }

    public void testDBThemeOButtonBGColor() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.inverted_very_light_blue_button))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
    }

    public void testBOThemeOButtonBGColor() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.inverted_very_light_orange_button))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
    }
}
