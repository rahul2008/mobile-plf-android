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
import static com.philips.cdp.ui.catalog.Matchers.IsPixelAsExpectedMatcher.isImageSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsDimensionAsExpectedMatcher.isDimensionSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ActionButtonTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public ActionButtonTest() {

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

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.sqaure_cross_mdpi) ;
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnSquareCrossMark))
                .check(matches(isDimensionSimilar((int)width,(int)height)));
        onView(withId(R.id.miscBtnSquareCrossMark))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonSquareMinusIsPixelPerfect() {
        onView(withText("Action Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.sqaure_minus_mdpi);
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnSquareMinus))
                .check(matches(isDimensionSimilar((int)width,(int)height)));
        onView(withId(R.id.miscBtnSquareMinus))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonSquarePlusIsPixelPerfect() {

        onView(withText("Action Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.sqaure_plus_mdpi);
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnSquarePlus))
                .check(matches(isDimensionSimilar((int)width,(int)height)));
        onView(withId(R.id.miscBtnSquarePlus))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonSquareRightIsPixelPerfect() {
        onView(withText("Action Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.sqaure_right_mdpi);
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnSquareArrow))
                .check(matches(isDimensionSimilar((int)width,(int)height)));
        onView(withId(R.id.miscBtnSquareArrow))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonCirclePlusIsPixelPerfect() {
        onView(withText("Action Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_plus_mdpi);
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnCirclePlus))
                .check(matches(isDimensionSimilar((int)width,(int)height)));
        onView(withId(R.id.miscBtnCirclePlus))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonCircleMinusIsPixelPerfect() {
        onView(withText("Action Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_minus_mdpi);
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnCircleMinus))
                .check(matches(isDimensionSimilar((int)width,(int)height)));
        onView(withId(R.id.miscBtnCircleMinus))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonCircleRightIsPixelPerfect() {
        onView(withText("Action Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_right_mdpi);
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isDimensionSimilar((int)width,(int)height)));
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    // Not verifying for pixel perfectness as the design for question mark is a text instead of image
    public void testActionButtonCircleQuestionAsExpected() {
        onView(withText("Action Buttons")).perform(click());
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnCircleQuestionMark))
                .check(matches(isDimensionSimilar((int)width,(int)height)));
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

    public void testBAThemeActionButtonColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        pressBack();
        onView(withText("Action Buttons")).perform(click());
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isBackgroundColorSimilar("#1e9d8b")));
    }


/*    public void testActionButtonBGBitmapColorAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_question);
        onView(withId(R.id.miscBtnCircleQuestionMark))
                .check(matches(isBackgroundBitmapColorSimilar(expectedBitmap)));
    }*/

}
