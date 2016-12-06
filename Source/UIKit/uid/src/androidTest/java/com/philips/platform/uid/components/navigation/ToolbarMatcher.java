package com.philips.platform.uid.components.navigation;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.philips.platform.uid.matcher.BaseTypeSafteyMatcher;

public class ToolbarMatcher {
    public static BaseTypeSafteyMatcher<? super View> isSameTitleMarginRight(final int endMargin) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof Toolbar) {
                    Toolbar toolbar = (Toolbar) view;
                    return toolbar.getTitleMarginEnd() == endMargin;
                }
                throw new RuntimeException("view is not of type Toolbar but it it of " + view.getClass());
            }
        };
    }

    public static BaseTypeSafteyMatcher<? super View> isSameTitleMarginStart(final int startMargin) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof Toolbar) {
                    Toolbar toolbar = (Toolbar) view;
                    return toolbar.getTitleMarginStart() == startMargin;
                }
                throw new RuntimeException("view is not of type Toolbar but it it of " + view.getClass());
            }
        };
    }
}
