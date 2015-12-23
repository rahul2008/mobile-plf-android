package com.philips.cdp.ui.catalog.Matchers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsSliderColorAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsBackgroundColorAsExpectedMatcher";
    private String expectedColor;
    private String sliderComponent;

    public IsSliderColorAsExpectedMatcher(final Class<? extends View> expectedType, String expectedColor, String sliderComponent) {
        super(expectedType);
        this.expectedColor = expectedColor;
        this.sliderComponent = sliderComponent;
    }

    public static Matcher<View> isSliderColorSimilar(final String expectedColor, String string) {
        return new IsSliderColorAsExpectedMatcher(View.class, expectedColor, string);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual  color  differs  when compared with expected color");
    }

    @Override
    public boolean matchesSafely(View view) {
        if (view instanceof SeekBar) {
            SeekBar seekbar = (SeekBar) view;
            if ("progress".equalsIgnoreCase(sliderComponent)) {
                Bitmap bitmap = null;
                if (seekbar.getProgressDrawable() instanceof BitmapDrawable) {
                    bitmap = ((BitmapDrawable) seekbar.getProgressDrawable()).getBitmap();
                    int actualRGB = bitmap.getPixel(1, 10);
                    String actualcolor = "#" +
                            Integer.toString(Color.red(actualRGB), 16) +
                            Integer.toString(Color.green(actualRGB), 16) +
                            Integer.toString(Color.blue(actualRGB), 16);

                    return actualcolor.equalsIgnoreCase(expectedColor);
                }
            } else if ("thumb".equalsIgnoreCase(sliderComponent)) {
                Bitmap bitmap = null;
                if (seekbar.getThumb() instanceof BitmapDrawable) {
                    bitmap = ((BitmapDrawable) seekbar.getThumb()).getBitmap();
                } else {
                    if (seekbar.getThumb().getIntrinsicWidth() <= 0 || seekbar.getThumb().getIntrinsicHeight() <= 0) {
                        bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
                    } else {
                        bitmap = Bitmap.createBitmap(seekbar.getThumb().getIntrinsicWidth(), seekbar.getThumb().getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    }

                    Canvas canvas = new Canvas(bitmap);
                    seekbar.getThumb().setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    seekbar.getThumb().draw(canvas);
                }
                int actualRGB = bitmap.getPixel(10, 10);
                String actualcolor = "#" +
                        Integer.toString(Color.red(actualRGB), 16) +
                        Integer.toString(Color.green(actualRGB), 16) +
                        Integer.toString(Color.blue(actualRGB), 16);

                return actualcolor.equalsIgnoreCase(expectedColor);
            } else if ("background".equalsIgnoreCase(sliderComponent)) {
                Bitmap bitmap = null;
                if (seekbar.getBackground() instanceof Drawable) {
                    bitmap = ((BitmapDrawable) seekbar.getBackground()).getBitmap();
                } else {
                    if (seekbar.getBackground().getIntrinsicWidth() <= 0 || seekbar.getBackground().getIntrinsicHeight() <= 0) {
                        bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
                    } else {
                        bitmap = Bitmap.createBitmap(seekbar.getBackground().getIntrinsicWidth(), seekbar.getBackground().getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    }

                    Canvas canvas = new Canvas(bitmap);
                    seekbar.getBackground().setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    seekbar.getBackground().draw(canvas);
                }
                int actualRGB = bitmap.getPixel(10, 10);
                String actualcolor = "#" +
                        Integer.toString(Color.red(actualRGB), 16) +
                        Integer.toString(Color.green(actualRGB), 16) +
                        Integer.toString(Color.blue(actualRGB), 16);

                return actualcolor.equalsIgnoreCase(expectedColor);
            }
        }

        return false;
    }
}








