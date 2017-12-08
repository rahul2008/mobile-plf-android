package com.philips.cdp.ui.catalog.Matchers;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsSliderHeightAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsHeightAsExpected";
    private int expectedHeight;
    private String sliderComponent;

    public IsSliderHeightAsExpectedMatcher(final Class<? extends View> expectedType, int expectedHeight, String sliderComponent) {
        super(expectedType);
        this.expectedHeight = expectedHeight;
        this.sliderComponent = sliderComponent;
    }

    public static Matcher<View> isSliderHeightSimilar(final int expectedHeight, String string) {
        return new IsSliderHeightAsExpectedMatcher(View.class, expectedHeight, string);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual image differs in height when compared with expected image");
    }

    @Override
    public boolean matchesSafely(View view) {
        if (view instanceof SeekBar) {
            SeekBar seekbar = (SeekBar) view;
            if ("thumb".equalsIgnoreCase(sliderComponent)) {
                int actualHeight = seekbar.getThumb().getBounds().bottom - seekbar.getThumb().getBounds().top;
                return actualHeight == expectedHeight;
            } else if ("progress".equalsIgnoreCase(sliderComponent)) {
                Bitmap bitmap = null;
                if (seekbar.getProgressDrawable() instanceof BitmapDrawable) {
                    bitmap = ((BitmapDrawable) seekbar.getProgressDrawable()).getBitmap();
                }
//                int actualHeight = bitmap.getHeight();
//                seekbar.setMax(10);
//                seekbar.setProgress(5);
                int actualHeight = seekbar.getProgressDrawable().getBounds().bottom - seekbar.getProgressDrawable().getBounds().top;
                return actualHeight == expectedHeight;
            } else if ("background".equalsIgnoreCase(sliderComponent)) {
                int actualHeight = seekbar.getBackground().getBounds().bottom - seekbar.getBackground().getBounds().top;
                return actualHeight == expectedHeight;
            }
        }
        return false;
    }
}
