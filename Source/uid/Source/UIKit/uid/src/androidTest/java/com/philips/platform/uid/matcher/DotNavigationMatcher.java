/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.uid.matcher;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.uid.drawableutils.GradientDrawableUtils;
import com.philips.platform.uid.view.widget.DotNavigationIndicator;

import org.hamcrest.Matcher;

public class DotNavigationMatcher {
    public static BaseTypeSafteyMatcher<View> hasSameSelectedColor(final int index, final int expectedColor) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View item) {
                if (item instanceof DotNavigationIndicator) {
                    final View view = ((DotNavigationIndicator) item).getChildAt(index);
                    if (view instanceof AppCompatImageButton) {
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
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                    setValues(lp.leftMargin, expectedValue);
                    return areEqual();
                }
                return false;
            }
        };
    }

    public static Matcher<View> isSameRightMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof DotNavigationIndicator) {
                    view = ((DotNavigationIndicator) view).getChildAt(0);
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                    setValues(lp.rightMargin, expectedValue);
                }
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
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    setValues(item.getForegroundGravity(), Gravity.CENTER);
//                    return areEqual();
//                } else {
//                    setValues(((LinearLayout.LayoutParams) item.getLayoutParams()).gravity, center);
//                }
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
                        if (item instanceof AppCompatImageView) {
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
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View item) {
                if (item instanceof DotNavigationIndicator) {
                    final View childAt = ((DotNavigationIndicator) item).getChildAt(0);

                    setValues(childAt.getWidth(), expectedWidth);
                    return areEqual();
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> hasSameHeight(final int expectedHeight) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View item) {
                if (item instanceof DotNavigationIndicator) {
                    final View childAt = ((DotNavigationIndicator) item).getChildAt(0);

                    setValues(childAt.getHeight(), expectedHeight);
                    return areEqual();
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> sameBackgroundColor(final int[] index, final int attributeColor) {
        return new BaseTypeSafteyMatcher<View>() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof DotNavigationIndicator) {
                    final AppCompatImageView childAt = (AppCompatImageView) ((DotNavigationIndicator) view).getChildAt(0);
                    final ColorStateList backgroundTintList = childAt.getBackgroundTintList();
                    int defaultColor = backgroundTintList.getDefaultColor();
                    if (index.length != 0) {
                        defaultColor = backgroundTintList.getColorForState(index, backgroundTintList.getDefaultColor());
                    }
                    setValues(defaultColor, attributeColor);
                    return areEqual();
                }
                return false;
            }
        };
    }
}
