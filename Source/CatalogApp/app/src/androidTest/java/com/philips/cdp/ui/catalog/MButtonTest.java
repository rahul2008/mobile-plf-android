package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

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
public class MButtonTest extends ActivityInstrumentationTestCase2<MainActivity> {

/*

    int actual = 0;
    int expected = 1;

    int [][] MiscButton_ids = {
            {R.id.miscBtnSquareArrow,com.philips.cdp.ui.catalog.test.R.drawable.square_right},
            {R.id.miscBtnSquarePlus,com.philips.cdp.ui.catalog.test.R.drawable.square_plus},
            {R.id.miscBtnSquareMinus,com.philips.cdp.ui.catalog.test.R.drawable.square_minus},
            {R.id.miscBtnSquareCrossMark,com.philips.cdp.ui.catalog.test.R.drawable.square_cross},
            {R.id.miscBtnCircleArrow,com.philips.cdp.ui.catalog.test.R.drawable.circle_right},
            {R.id.miscBtnCirclePlus,com.philips.cdp.ui.catalog.test.R.drawable.circle_plus},
            {R.id.miscBtnCircleMinus,com.philips.cdp.ui.catalog.test.R.drawable.circle_minus},
            {R.id.miscBtnCircleQuestionMark,com.philips.cdp.ui.catalog.test.R.drawable.circle_question},
           };
*/

    private Resources testResources;


    public MButtonTest() {
        super(MainActivity.class);

    }





    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();


    }

/*    public void testAllButtons() {
            for(int i = 0; i < MiscButton_ids.length; i++) {
            Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, MiscButton_ids[i][expected]);
            onView(withId(MiscButton_ids[i][actual]))
                    .check(matches(isImageSimilar(expectedBitmap)));
            expectedBitmap.recycle();

        }*/

    public void testMButtonSquareCrossExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_cross);
        onView(withId(R.id.miscBtnSquareCrossMark))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

 /*   public void testMButtonSquareMinusAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_minus);
        onView(withId(R.id.miscBtnSquareMinus))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

      public void testMButtonSquarePlusAsExpected() {

    }

    public void testMButtonSquareRightExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_right);
        onView(withId(R.id.miscBtnSquareGreater))
                .check(matches(isImageSimilar(expectedBitmap)));
    }
*/
 /*   public void testMButtonSquarePlusAsExpected() {

        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_plus);
        onView(withId(R.id.miscBtnSquarePlus))
                .check(matches(isImageSimilar(expectedBitmap)));
    }



        public void testMButtonSquareRightAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_right);
        onView(withId(R.id.miscBtnSquareArrow))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testMButtonCirclePlusAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_plus);
        onView(withId(R.id.miscBtnCirclePlus))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testMButtonCircleMinusAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_minus);
        onView(withId(R.id.miscBtnCircleMinus))

    public void testMButtonSquareMinusAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_minus);
        onView(withId(R.id.miscBtnSquareMinus))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testMButtonCircleRightAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_right);
        onView(withId(R.id.miscBtnCircleGreater))

                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testMButtonCircleQuestionAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_question);
        onView(withId(R.id.miscBtnCircleQuestionMark))
                .check(matches(isImageSimilar(expectedBitmap)));
    }



    public void testMButtonCircleRightAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_right);
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isImageSimilar(expectedBitmap)));
    }


*/


}
