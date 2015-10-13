package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isBelow;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsDimensionBitmapAsExpectedMatcher.isDimensionSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class SplashScreenMobileTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public SplashScreenMobileTest() {

        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testSplashScreenLogoAsExpected() {
        onView(withText("Splash Screen")).perform(click());
        onView(withText("Logo Center, Title bottom")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.philips_shield);
        onView(withId(com.philips.cdp.uikit.R.id.splash_logo))
                .check(matches(isDimensionSimilar(expectedBitmap)));
//        onView(withId(com.philips.cdp.uikit.R.id.splash_logo))
//                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testSplashScreenTitleAsExpected() {
        onView(withText("Splash Screen")).perform(click());
        onView(withText("Logo Center, Title bottom")).perform(click());
        onView(withId(R.id.splash_title))
                .check(matches(isTextColorSimilar("#ffffff")));
        onView(withId(R.id.splash_title))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.splash_title_text_size))));
    }

    public void testSplashScreenLogoTopAligned() {
        onView(withText("Splash Screen")).perform(click());
        onView(withText("Logo Top")).perform(click());
        onView(withId(R.id.splash_logo)).check(isAbove(withId(R.id.splash_title)));

    }

    public void testSplashScreenLogoBottomAligned() {
        onView(withText("Splash Screen")).perform(click());
        onView(withText("Logo Bottom")).perform(click());
        onView(withId(R.id.splash_logo)).check(isBelow(withId(R.id.splash_title)));

    }

}
