/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.matcher;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import org.hamcrest.Matcher;

public class DividerMatcher {

    public static Matcher<? super View> hasSameHeight(final float height) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof ListView) {
                    ListView listView = (ListView) view;
                    setValues(listView.getDividerHeight(), height);

                    return areEqual();
                }

                if (view instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) view;
                    setValues(recyclerView.getHeight(), height);

                    return areEqual();
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> isSameColor(final int color) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof ListView) {
                    ListView listView = (ListView) view;
                    final Drawable divider = listView.getDivider();
                    if (divider instanceof ColorDrawable) {
                        ColorDrawable colorDrawable = (ColorDrawable) divider;
                        setValues(colorDrawable.getColor(), color);
                        return areEqual();
                    }
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> isSameAlpha(final float alpha) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof ListView) {
                    ListView listView = (ListView) view;
                    final Drawable divider = listView.getDivider();
                    if (divider instanceof ColorDrawable) {
                        ColorDrawable colorDrawable = (ColorDrawable) divider;
                        setValues(colorDrawable.getAlpha(), alpha);
                        return areEqual();
                    }
                }
                return false;
            }
        };
    }
}
