package com.philips.cdp.ui.catalog.Matchers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsDimensionAsExpectedMatcher.isDimensionSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsPixelAsExpectedMatcher.isImageSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ActionButtonTestXhdpi extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public ActionButtonTestXhdpi() {

        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testAButtonSquareCrossIsPixelPerfect() {
        onView(withText("Action Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_cross_xhdpi) ;
        onView(withId(R.id.miscBtnSquareCrossMark))
                .check(matches(isDimensionSimilar(66,66)));
        onView(withId(R.id.miscBtnSquareCrossMark))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonSquareMinusIsPixelPerfect() {
        onView(withText("Action Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_minus_xhdpi);
        onView(withId(R.id.miscBtnSquareMinus))
                .check(matches(isDimensionSimilar(66,66)));
        onView(withId(R.id.miscBtnSquareMinus))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonSquarePlusIsPixelPerfect() {

        onView(withText("Action Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_plus_xhdpi);
        onView(withId(R.id.miscBtnSquarePlus))
                .check(matches(isDimensionSimilar(66,66)));
        onView(withId(R.id.miscBtnSquarePlus))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonSquareRightIsPixelPerfect() {
        onView(withText("Action Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_right_xhdpi);
        onView(withId(R.id.miscBtnSquareArrow))
                .check(matches(isDimensionSimilar(66,66)));
        onView(withId(R.id.miscBtnSquareArrow))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonCirclePlusIsPixelPerfect() {
        onView(withText("Action Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_plus_xhdpi);
        onView(withId(R.id.miscBtnCirclePlus))
                .check(matches(isDimensionSimilar(66, 66)));
        onView(withId(R.id.miscBtnCirclePlus))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonCircleMinusIsPixelPerfect() {
        onView(withText("Action Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_minus_xhdpi);
        onView(withId(R.id.miscBtnCircleMinus))
                .check(matches(isDimensionSimilar(66, 66)));
        onView(withId(R.id.miscBtnCircleMinus))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonCircleRightIsPixelPerfect() {
        onView(withText("Action Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_right_xhdpi);
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isDimensionSimilar(66, 66)));
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    // Not verifying for pixel perfectness as the design for question mark is a text instead of image
    public void testActionButtonCircleQuestionAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isDimensionSimilar(66, 66)));
    }


    public void testDBThemeActionButtonColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();
        onView(withText("Action Buttons")).perform(click());
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isBackgroundColorSimilar("#03478")));
    }

    public void testBOThemeActionButtonColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();
        onView(withText("Action Buttons")).perform(click());
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isBackgroundColorSimilar("#e9830")));
    }


/*    public void testActionButtonBGBitmapColorAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_question);
        onView(withId(R.id.miscBtnCircleQuestionMark))
                .check(matches(isBackgroundBitmapColorSimilar(expectedBitmap)));
    }*/

}