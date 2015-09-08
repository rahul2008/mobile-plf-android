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
public class MButtonPressedTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public MButtonPressedTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

   /* public void setSelected(boolean b){
        onView(withText("Miscellaneous Buttons")).perform(click());
         Button button = (Button)getActivity().findViewById(R.id.miscBtnSquarePlus);
        button.setSelected(b);
    }*/

 /*   public void setState(int state_pressed){
        onView(withText("Miscellaneous Buttons")).perform(click());
        Button mybutton = (Button) mybutton.findViewById();
        mybutton.setState(state_pressed);
    }*/

    public void testMButtonSquarePlusPressedExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.sqaure_plus_pressed);
        onView(withId(R.id.miscBtnSquarePlus))
                .check(matches(isImageSimilar(expectedBitmap)));
    }
}
