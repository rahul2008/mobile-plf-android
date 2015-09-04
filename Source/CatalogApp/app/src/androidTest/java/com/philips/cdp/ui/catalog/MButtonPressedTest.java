package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by 310185184 on 9/3/2015.
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

    public void testMButtonSquareCrossPressedExpected() {
        onView(withText("Miscellaneous Buttons")).perform(click());
        Button button = (Button)getActivity().findViewById(R.id.miscBtnSquareCrossMark);
                button.setPressed(true);
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.square_cross);
        onView(withId(R.id.miscBtnSquareCrossMark))
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
//                return expectedBitmap.sameAs(actualBitmap);
                return TestUtils.sameAs(actualBitmap, expectedBitmap);

            }
        };
    }


}
