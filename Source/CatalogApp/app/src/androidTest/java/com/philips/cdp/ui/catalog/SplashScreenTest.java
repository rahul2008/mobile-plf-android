package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.test.espresso.ViewAssertion;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.activity.MainActivity;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.IsPixelAsExpectedMatcher.isImageSimilar;
import static com.philips.cdp.ui.catalog.IsEqualMatcher.isFontSimilar;

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

    public void testSplashScreenLogoAsExpected() {
        onView(withText("Splash Screen")).perform(click());
        onView(withText("Logo Center, Title Top")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.philips_shield);
        onView(withId(com.philips.cdp.uikit.R.id.splash_background))
                .check(matches(IsDimensionAsExpectedMatcher.isDimensionSimilar(expectedBitmap)));
        onView(withId(com.philips.cdp.uikit.R.id.splash_background))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testSplashScreenTextAsExpected(){
        onView(withText("Splash Screen")).perform(click());
        onView(withText("Logo Center, Title Top")).perform(click());

        //Paint.FontMetricsInt expectedFontSize = Paint.
        float expectedFontSize = 30;
                onView(withId(com.philips.cdp.uikit.R.id.splash_title)).check(matches(isFontSimilar(expectedFontSize));

    }

}
