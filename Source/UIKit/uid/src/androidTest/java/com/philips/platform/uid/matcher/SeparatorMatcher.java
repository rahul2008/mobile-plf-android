/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.matcher;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.philips.platform.uid.drawable.SeparatorDrawable;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import org.hamcrest.Matcher;

import java.util.List;

public class SeparatorMatcher {

    public static Matcher<? super View> hasSameHeight(final int height) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                boolean retValue = false;
                if (view instanceof ListView) {
                    ListView listView = (ListView) view;
                    setValues(listView.getDividerHeight(), height);

                    return areEqual();
                }
                if (view instanceof RecyclerView) {
                    final List<RecyclerView.ItemDecoration> itemsDecoration = UIDTestUtils.getItemsDecoration((RecyclerView) view);
                    final RecyclerView.ItemDecoration itemDecoration = itemsDecoration.get(0);
                    if (itemDecoration instanceof RecyclerViewSeparatorItemDecoration) {
                        final Drawable divider = ((RecyclerViewSeparatorItemDecoration) itemDecoration).getDividerDrawable();
                        return DrawableMatcher.isSameHeight(height).matches(divider);
                    }
                }
                if (view.getBackground() instanceof SeparatorDrawable) {
                    setValues(((SeparatorDrawable) view.getBackground()).getHeight(), height);
                    return areEqual();
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
                if (view.getBackground() instanceof SeparatorDrawable) {
                    return isSameColorDrawableColor(view.getBackground());
                }
                return false;
            }

            private boolean hasSameListviewDividerColor(final ListView view) {
                ListView listView = view;
                final Drawable divider = listView.getDivider();
                return isSameColorDrawableColor(divider);
            }

            private boolean hasSameRecyclerViewDividerColor(final RecyclerView view) {
                final List<RecyclerView.ItemDecoration> itemsDecoration = UIDTestUtils.getItemsDecoration(view);
                final RecyclerView.ItemDecoration itemDecoration = itemsDecoration.get(0);
                if (itemDecoration instanceof RecyclerViewSeparatorItemDecoration) {
                    return isSameColorDrawableColor(((RecyclerViewSeparatorItemDecoration) itemDecoration).getDividerDrawable());
                }
                return false;
            }

            private boolean isSameColorDrawableColor(final Drawable divider) {
                if (divider instanceof SeparatorDrawable) {
                    SeparatorDrawable colorDrawable = (SeparatorDrawable) divider;
                    setValues(colorDrawable.getColor(), color);
                    return areEqual();
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> hasHeight(final int height) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                return DrawableMatcher.isSameHeight(height).matches(view.getBackground());
            }
        };
    }
}
