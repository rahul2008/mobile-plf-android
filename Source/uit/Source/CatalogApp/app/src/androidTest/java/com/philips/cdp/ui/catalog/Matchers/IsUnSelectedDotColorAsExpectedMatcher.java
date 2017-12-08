package com.philips.cdp.ui.catalog.Matchers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.uikit.customviews.CircleIndicator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsUnSelectedDotColorAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsBackgroundColorAsExpectedMatcher";
    private String expectedColor;

    public IsUnSelectedDotColorAsExpectedMatcher(final Class<? extends View> expectedType, String expectedColor) {
        super(expectedType);
        this.expectedColor = expectedColor;
    }

    public static Matcher<View> isUnSelectedDotColorSimilar(ViewGroup vg, final String expectedColor) {
        return new IsUnSelectedDotColorAsExpectedMatcher(View.class, expectedColor);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual  color  differs  when compared with expected color");
    }

    @Override
    public boolean matchesSafely(View view) {
        String actualColour = actualColor(view);
        return actualColour.equalsIgnoreCase(expectedColor);
    }

    public String actualColor(View view) {
        int color = 0;
        if (view instanceof CircleIndicator) {
            CircleIndicator unselecteddot = (CircleIndicator) view;

            Bitmap bitmap = null;
            if (unselecteddot.getUnSelectedDot().getIntrinsicWidth() <= 0 || unselecteddot.getUnSelectedDot().getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(unselecteddot.getUnSelectedDot().getIntrinsicWidth(), unselecteddot.getUnSelectedDot().getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            unselecteddot.getUnSelectedDot().setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            unselecteddot.getUnSelectedDot().draw(canvas);

            boolean found = false;
            for (int row = 0; row < bitmap.getHeight(); row++) {
                if (found) break;
                int col = 0;
                int count = 0;
                for (int column = 0; column < bitmap.getWidth(); column++) {
                    int temp = bitmap.getPixel(row, column);
                    if (temp == 0) continue;
                    if (col == 0) {
                        col = bitmap.getPixel(row, column);
                    }
                    if (temp == col) {
                        count++;
                        if (count == 5) {
                            color = col;
                            found = true;
                            break;
                        }
                    } else {
                        count = 0;
                        col = temp;
                    }
                    //                Log.d("test1","" +row +","+ column+ "  " +Color.red(col) +"," + Color.green(col)+"," +Color.blue(col));

                }
            }
        }
        return String.valueOf(color);
    }
}








