package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

/**
 * Created by 310185184 on 9/2/2015.
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

    public void testSplashScreenTextAsExpected() {
        onView(withText("Splash Screen")).perform(click());
        onView(withText("Logo Bottom")).perform(click());
     /*   onView(withId(R.id.splash_title)).check(matches(withText("Catalog App")));*/


        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.philipslogo);
        onView(withId(R.id.splash_background))
                .check(matches(isImageTheSame(expectedBitmap)));

    }
    public static Matcher<View> isImageTheSame(final Bitmap expectedBitmap) {
        return new BoundedMatcher<View, View>(View.class) {

            @Override
            public void describeTo(Description description) {

                description.appendText("image is not same as: ");
                description.appendValue(expectedBitmap);
            }

            @Override
            public boolean matchesSafely(View view) {
                Bitmap actualBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas actualCanvas = new Canvas(actualBitmap);
                view.draw(actualCanvas);
                return expectedBitmap.sameAs(actualBitmap);
            }
        };
    }
}
