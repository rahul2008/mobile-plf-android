/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.philips.platform.uid.R;

/**
 * UID Bottom TabBar
 * <p>
 * <P> UID Bottom TabBar is custom TabLayout as per DLS design.
 * <p>
 * <P> Use Bottom TabBar in your xml layout file similar to TabLayout.
 * <p>
 *
 */

public class TabLayout extends android.support.design.widget.TabLayout {

    private int preferredHeight;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.uidBottomTabBarStyle);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        preferredHeight = getContext().getResources().getDimensionPixelSize(R.dimen.uid_tab_layout_height_without_title);
        @SuppressLint("CustomViewStyleable") TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UIDTabLayout, defStyleAttr, R.style.UIDBottomTabLayout);
        if (a.hasValue(R.styleable.UIDTabLayout_uidTabItemPreferredHeight)) {
            preferredHeight = a.getDimensionPixelSize(R.styleable.UIDTabLayout_uidTabItemPreferredHeight, preferredHeight);
        }
        a.recycle();

    }

    @Override
    public void addView(View child) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, int index) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        addViewInternal(child);
    }

    private void addViewInternal(final View child) {
        if (child instanceof UIDTabItem) {
            addTabFromItemView((UIDTabItem) child);
        } else {
            throw new IllegalArgumentException("Only UIDTabItem instances can be added to TabLayout");
        }
    }

    private void addTabFromItemView(@NonNull UIDTabItem item) {
        final Tab tab = newTab();
        tab.setCustomView(item);
        addTab(tab);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SavedState savedState = new TabLayout.SavedState(parcelable);
        savedState.selectedTabPosition = getSelectedTabPosition();
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof TabLayout.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final TabLayout.SavedState savedState = (TabLayout.SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        Tab tabToSelect = getTabAt(savedState.selectedTabPosition);
        if (tabToSelect != null) {
            tabToSelect.select();
        }
    }

    private static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<TabLayout.SavedState> CREATOR
                = new Parcelable.Creator<TabLayout.SavedState>() {
            public TabLayout.SavedState createFromParcel(Parcel in) {
                return new TabLayout.SavedState(in);
            }

            public TabLayout.SavedState[] newArray(int size) {
                return new TabLayout.SavedState[size];
            }
        };

        int selectedTabPosition;

        SavedState(final Parcelable superState) {
            super(superState);
        }

        SavedState(final Parcel in) {
            super(in);
            selectedTabPosition = in.readInt();
        }

        @Override
        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(selectedTabPosition);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(preferredHeight, MeasureSpec.EXACTLY));
    }

    public void setPreferredHeight(int height) {
        preferredHeight = height;
    }
}