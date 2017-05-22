/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.uid.matcher;

import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.uid.drawableutils.GradientDrawableUtils;
import com.philips.platform.uid.view.widget.DotNavigationIcon;
import com.philips.platform.uid.view.widget.DotNavigationIndicator;

import org.hamcrest.Matcher;

public class DotNavigationMatcher {
    public static BaseTypeSafteyMatcher<View> hasSameSelectedColor(final int index, final int expectedColor) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View item) {
                if (item instanceof DotNavigationIndicator) {
                    final View view = ((DotNavigationIndicator) item).getChildAt(index);
                    if (view instanceof DotNavigationIcon) {
                        final Drawable background = view.getBackground();
                        final Matcher<Drawable> matcher = DrawableMatcher.isSameColors(1, expectedColor);
                        return matcher.matches(background);
                    }
                }
                return false;
            }
        };
    }

    public static Matcher<View> isSameLeftMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof DotNavigationIndicator) {
                    view = ((DotNavigationIndicator) view).getChildAt(0);
                }
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                setValues(lp.leftMargin, expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameRightMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof DotNavigationIndicator) {
                    view = ((DotNavigationIndicator) view).getChildAt(0);
                }
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                setValues(lp.rightMargin, expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<? super View> hasChildrens(final int childCount) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof DotNavigationIndicator) {
                    setValues(((DotNavigationIndicator) view).getChildCount(), childCount);
                    return areEqual();
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> hasGravity(final int center) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View item) {
                return true;
            }
        };
    }

    public static Matcher<? super View> hasSameRadius(final int expectedRadius) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (item instanceof DotNavigationIndicator) {
                    final PagerAdapter adapter = ((DotNavigationIndicator) item).getViewPager().getAdapter();
                    if (adapter.getCount() > 0) {
                        item = ((DotNavigationIndicator) item).getChildAt(0);
                        if (item instanceof DotNavigationIcon) {
                            final Drawable background = item.getBackground();
                            GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(background);
                            setValues(stateColors.getCornerRadius()[0], expectedRadius);
                            return Float.compare(stateColors.getCornerRadius()[0], expectedRadius) == 0;
                        }
                    }
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> hasSameWidth(final int expectedWidth) {
        return null;
    }

    public static Matcher<? super View> hasSameHeight(final int expectedHeight) {
        return null;
    }
}
