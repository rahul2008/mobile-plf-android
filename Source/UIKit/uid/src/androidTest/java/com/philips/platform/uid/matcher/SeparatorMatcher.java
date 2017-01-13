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

import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import org.hamcrest.Matcher;

import java.util.List;

public class SeparatorMatcher {

    public static Matcher<? super View> hasSameHeight(final int height) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof ListView) {
                    ListView listView = (ListView) view;
                    setValues(listView.getDividerHeight(), height);

                    return areEqual();
                }
                if (view instanceof RecyclerView) {
                    final List<RecyclerView.ItemDecoration> itemsDecoration = UIDTestUtils.getItemsDecoration((RecyclerView) view);
                    final RecyclerView.ItemDecoration itemDecoration = itemsDecoration.get(0);
                    if (itemDecoration instanceof RecyclerViewSeparatorItemDecoration) {
                        final Drawable divider = ((RecyclerViewSeparatorItemDecoration) itemDecoration).getDivider();
                        return DrawableMatcher.isSameHeight(height).matches(divider);
                    }
                }
                if (view.getBackground() instanceof ColorDrawable) {
                    return DrawableMatcher.isSameHeight(height).matches(view.getBackground());
                }

                return false;
            }
        };
    }

    public static Matcher<? super View> hasSameColor(final int color) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof ListView) {
                    if (hasSameListviewDividerColor((ListView) view)) return areEqual();
                }
                if (view instanceof RecyclerView) {
                    if (hasSameRecyclerViewDividerColor((RecyclerView) view)) return areEqual();
                }
                if (view.getBackground() instanceof ColorDrawable) {
                    if (isSameColorDrawableColor(view.getBackground())) return true;
                }
                return false;
            }

            private boolean hasSameListviewDividerColor(final ListView view) {
                ListView listView = view;
                final Drawable divider = listView.getDivider();
                if (isSameColorDrawableColor(divider)) return true;
                return false;
            }

            private boolean hasSameRecyclerViewDividerColor(final RecyclerView view) {
                final List<RecyclerView.ItemDecoration> itemsDecoration = UIDTestUtils.getItemsDecoration(view);
                final RecyclerView.ItemDecoration itemDecoration = itemsDecoration.get(0);
                if (itemDecoration instanceof RecyclerViewSeparatorItemDecoration) {
                    final Drawable divider = ((RecyclerViewSeparatorItemDecoration) itemDecoration).getDivider();
                    if (isSameColorDrawableColor(divider)) return true;
                }
                return false;
            }

            private boolean isSameColorDrawableColor(final Drawable divider) {
                if (divider instanceof ColorDrawable) {
                    ColorDrawable colorDrawable = (ColorDrawable) divider;
                    setValues(colorDrawable.getColor(), color);
                    return true;
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> hasHeight(final float height) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                return DrawableMatcher.isSameHeight(4).matches(view.getBackground());
            }
        };
    }
}
