package com.philips.platform.uid.matcher;

import android.view.View;
import android.widget.ImageView;

import com.philips.platform.uid.R;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.Label;

import org.hamcrest.Matcher;

public class InputValidationMatcher {

    public static Matcher<View> isSameErrorColor(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                InputValidationLayout layout = (InputValidationLayout) view;
                Label errorView = layout.getErrorLabelView();
                return TextViewPropertiesMatchers.isSameTextColor(R.attr.uid_state_error, expectedValue).matches(errorView);
            }
        };
    }

    public static Matcher<View> isSameIconMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                InputValidationLayout layout = (InputValidationLayout) view;
                ImageView errorImageView = layout.getErrorIconView();
                return ViewPropertiesMatchers.isSameEndMargin(expectedValue).matches(errorImageView);
            }
        };
    }

    public static Matcher<View> isSameErrorFontSize(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                InputValidationLayout layout = (InputValidationLayout) view;
                Label errorView = layout.getErrorLabelView();
                return TextViewPropertiesMatchers.isSameFontSize(expectedValue).matches(errorView);
            }
        };
    }

}


