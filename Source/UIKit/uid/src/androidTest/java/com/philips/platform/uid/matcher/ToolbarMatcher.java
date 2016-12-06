package com.philips.platform.uid.matcher;

import android.support.v7.widget.Toolbar;
import android.view.View;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ToolbarMatcher {
    public static TypeSafeMatcher isSameTitleMarginLeft(final int titleMargin) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof Toolbar) {
                    Toolbar toolbar = (Toolbar) view;
                    return toolbar.getTitleMarginStart() == titleMargin;
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> isSameTitleMarginRight(final int titleMargin) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof Toolbar) {
                    Toolbar toolbar = (Toolbar) view;
                    return toolbar.getTitleMarginEnd() == titleMargin;
                }
                return false;
            }
        };
    }
}
