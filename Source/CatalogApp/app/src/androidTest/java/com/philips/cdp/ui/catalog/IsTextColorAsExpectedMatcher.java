package com.philips.cdp.ui.catalog;

        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.support.test.espresso.matcher.BoundedMatcher;
        import android.view.View;
        import android.widget.TextView;

        import org.hamcrest.Description;
        import org.hamcrest.Matcher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IsTextColorAsExpectedMatcher extends BoundedMatcher<View, View> {

    public static final String TAG = "IsTextColorAsExpectedMatcher";
    private String expectedColor;

    public IsTextColorAsExpectedMatcher(final Class<? extends View> expectedType, String expectedColor) {
        super(expectedType);
        this.expectedColor = expectedColor;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Actual TextColor   differs  when compared with expected Textcolor");
    }

    @Override
    public boolean matchesSafely(View view) {

        if (view instanceof TextView) {
            TextView actualTextview = (TextView) view;
            String actualcolor = "#"  +
                    Integer.toString(Color.red(actualTextview.getCurrentTextColor()), 16) +
                    Integer.toString(Color.green(actualTextview.getCurrentTextColor()), 16) +
                    Integer.toString(Color.blue(actualTextview.getCurrentTextColor()), 16);

            if (actualcolor.equalsIgnoreCase(expectedColor)) {
                return true;
            }
        }
        return false;
    }
    public static Matcher<View> isTextColorSimilar(final String expectedTextColor){
        return new IsTextColorAsExpectedMatcher(View.class, expectedTextColor);
    }
}









