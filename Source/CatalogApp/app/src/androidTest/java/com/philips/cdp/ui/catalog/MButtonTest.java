package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.test.ActivityInstrumentationTestCase2;

import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;


import java.lang.reflect.Array;


import java.nio.ByteBuffer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.assertion.PositionAssertions.isAbove;


import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
                    .check(matches(isImageTheSame(expectedBitmap)));
            expectedBitmap.recycle();

        }*/

    public void testMButtonSquareCrossExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_cross);
        onView(withId(R.id.miscBtnSquareCrossMark))
                .check(matches(isImageTheSame(expectedBitmap)));
    }

    public void testMButtonSquareMinusAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_minus);
        onView(withId(R.id.miscBtnSquareMinus))
                .check(matches(isImageTheSame(expectedBitmap)));
    }

      public void testMButtonSquarePlusAsExpected() {

    }

    public void testMButtonSquareRightExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_right);
        onView(withId(R.id.miscBtnSquareGreater))
                .check(matches(isImageTheSame(expectedBitmap)));
    }

 /*   public void testMButtonSquarePlusAsExpected() {

        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_plus);
        onView(withId(R.id.miscBtnSquarePlus))
                .check(matches(isImageTheSame(expectedBitmap)));
    }



        public void testMButtonSquareRightAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_right);
        onView(withId(R.id.miscBtnSquareArrow))
                .check(matches(isImageTheSame(expectedBitmap)));
    }

    public void testMButtonCirclePlusAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_plus);
        onView(withId(R.id.miscBtnCirclePlus))
                .check(matches(isImageTheSame(expectedBitmap)));
    }

    public void testMButtonCircleMinusAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_minus);
        onView(withId(R.id.miscBtnCircleMinus))

    public void testMButtonSquareMinusAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_minus);
        onView(withId(R.id.miscBtnSquareMinus))
                .check(matches(isImageTheSame(expectedBitmap)));
    }

    public void testMButtonCircleRightAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_right);
        onView(withId(R.id.miscBtnCircleGreater))

                .check(matches(isImageTheSame(expectedBitmap)));
    }

    public void testMButtonCircleQuestionAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_question);
        onView(withId(R.id.miscBtnCircleQuestionMark))
                .check(matches(isImageTheSame(expectedBitmap)));
    }



    public void testMButtonCircleRightAsExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_right);
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isImageTheSame(expectedBitmap)));
    }


*/


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

//                return expectedBitmap.sameAs(actualBitmap);
                return TestUtils.sameAs(actualBitmap, expectedBitmap);

            }
        };
    }
}
