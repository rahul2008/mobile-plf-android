/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.matcher;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.hamcrest.Matcher;

@SuppressWarnings("ReturnOfInnerClass")
public class ViewPropertiesMatchers {
    public static Matcher<View> isSameLeftPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(view.getPaddingLeft(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameStartPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(view.getPaddingStart(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameEndPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(view.getPaddingEnd(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameRightPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(view.getPaddingRight(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameTopPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(view.getPaddingTop(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameBottomPadding(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(view.getPaddingBottom(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameViewWidth(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(view.getWidth(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameViewHeight(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                setValues(view.getHeight(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameLeftMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                setValues(lp.leftMargin, expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameStartMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                setValues(lp.getMarginStart(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameEndMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                setValues(lp.getMarginEnd(), expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameBottomMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                setValues(lp.bottomMargin, expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<View> isSameTopMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                setValues(lp.topMargin, expectedValue);
                return areEqual();
            }
        };
    }

    public static Matcher<? super View> isSameViewMinHeight(final int expectedIconHeight) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                setValues(view.getMinimumHeight(), expectedIconHeight);
                return areEqual();
            }
        };
    }

    public static Matcher<? super View> isMinHeight(final int height) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                setValues(view.getMinimumHeight(), height);
                return view.getMinimumHeight() <= height;
            }
        };
    }


    public static Matcher<? super View> isSameViewMinWidth(final int expectedIconWidth) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                setValues(view.getMinimumWidth(), expectedIconWidth);
                return areEqual();
            }
        };
    }

    public static Matcher<? super View> isVisible(final int gone) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                setValues(view.getVisibility(), gone);
                return areEqual();
            }
        };
    }

    public static Matcher<? super View> isSameTitleRightMargin(final int rightMargin) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof Toolbar) {
                    Toolbar toolbar = (Toolbar) view;
                    setValues(toolbar.getTitleMarginEnd(), rightMargin);
                    return areEqual();
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> isSameTittleLeftMargin(final int leftMargin) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof Toolbar) {
                    Toolbar toolbar = (Toolbar) view;
                    setValues(toolbar.getTitleMarginStart(), leftMargin);
                    return areEqual();
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> hasSameColorDrawableBackgroundColor(final int color) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    final int drawablecolor = ((ColorDrawable) group.getBackground()).getColor();
                    setValues(Integer.toHexString(drawablecolor), Integer.toHexString(color));
                    return areEqual();
                }
                if (view instanceof View) {
                    final int drawablecolor = ((ColorDrawable) view.getBackground()).getColor();
                    setValues(Integer.toHexString(drawablecolor), Integer.toHexString(color));
                    return areEqual();
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> hasSameStateListBackgroundDrawableStateColor(final int[]attrs, final int color) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                StateListDrawable background = (StateListDrawable) view.getBackground();
                background.setState(attrs);
                Drawable normalBackground = background.getCurrent();
                BaseTypeSafteyMatcher<Drawable> d = (BaseTypeSafteyMatcher<Drawable>) DrawableMatcher.isSameColor(new int[]{android.R.attr.state_enabled}, color, false);
                d.matches(normalBackground);
                setValues(d.actual, d.expected);
                return areEqual();
            }
        };
    }

    public static Matcher<? super View> hasChildrensWithSameTextColor(final int color) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;

                    final int drawablecolor = ((AppCompatTextView) group.getChildAt(0)).getTextColors().getDefaultColor();
                    setValues(Integer.toHexString(drawablecolor), Integer.toHexString(color));
                    return areEqual();
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> isSameElevation(final float elevation) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                setValues(view.getElevation(), elevation);
                return floatEqual(1f);
            }
        };
    }

    public static Matcher<? super View> hasSameLinearLayoutGravity(final int gravity){
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                setValues(((LinearLayout.LayoutParams)item.getLayoutParams()).gravity,gravity);
                return areEqual();
            }
        };
    }

    public static Matcher<? super View> hasSameFrameLayoutGravity(final int gravity){
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                setValues(((FrameLayout.LayoutParams)item.getLayoutParams()).gravity,gravity);
                return areEqual();
            }
        };
    }

    public static Matcher<? super View> hasSameImageTintListStateColor(final int[]attrs, final int color) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if(view instanceof ImageView){
                    ColorStateList colorStateList = ((ImageView)view).getImageTintList();
                    setValues(Integer.toHexString(colorStateList.getColorForState(attrs,-1)), Integer.toHexString(color));
                    return areEqual();
                }
                return false;
            }
        };
    }
}