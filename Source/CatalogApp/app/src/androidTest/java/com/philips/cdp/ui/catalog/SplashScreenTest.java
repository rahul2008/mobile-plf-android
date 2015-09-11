package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.IsSimilarMatcher.isImageSimilar;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class SplashScreenTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public SplashScreenTest() {

        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testSplashScreenTextLogoCenterAsExpected() {
        onView(withText("Splash Screen")).perform(click());
        onView(withText("Logo Center, Title Top")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.philipslogo);
        onView(withId(com.philips.cdp.uikit.R.id.splash_background))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testSplashScreenTextLogoTopAsExpected() {
        onView(withText("Splash Screen")).perform(click());
        onView(withText("Logo Top")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.philipslogo);
        onView(withId(com.philips.cdp.uikit.R.id.splash_background))
                .check(matches(isImageSimilar(expectedBitmap)));
    }
    public void testSplashScreenTextLogoBottomAsExpected() {
            onView(withText("Splash Screen")).perform(click());
            onView(withText("Logo Bottom")).perform(click());

            Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.philipslogo);
            onView(withId(com.philips.cdp.uikit.R.id.splash_background))
                    .check(matches(isImageSimilar(expectedBitmap)));
        }

}
