/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */
package com.philips.platform.uid.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 *
 */
public class DotNavigationIndicator extends LinearLayout implements PageIndicator {
    private ViewPager mViewPager;
    private OnPageChangeListener mListener;
    private int mSelectedIndex;
    private boolean isIconActionDownDetected;
    private boolean isIconActionUpDetected;

    public DotNavigationIndicator(Context context) {
        this(context, null);
    }

    public DotNavigationIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotNavigationIndicator(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotNavigationIndicator(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    private void init(final Context context) {
        setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isIconActionDownDetected = true;
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            isIconActionUpDetected = true;
        }
        return processTouch(event);
    }

    private boolean processTouch(final MotionEvent event) {
        if (isIconActionDownDetected && isIconActionUpDetected) {

            resetIconTouch();

            return isSelectedChildTouched(event);
        }

        return false;
    }

    private boolean isSelectedChildTouched(final MotionEvent event) {
        final View selectedChild = getSelectedChild();
        if (selectedChild != null) {
            final Rect childRect = new Rect();
            selectedChild.getDrawingRect(childRect);
            final MarginLayoutParams marginLayoutParams = (MarginLayoutParams) selectedChild.getLayoutParams();
            final boolean isLeft = event.getX() < (selectedChild.getX() - selectedChild.getPaddingStart() - marginLayoutParams.getMarginStart());
            final boolean isRight = event.getX() > (selectedChild.getX() + selectedChild.getWidth() + marginLayoutParams.getMarginStart());
            if (isLeft)
                showPrevious();
            if (isRight)
                showNext();
        }
        return true;
    }

    private void resetIconTouch() {
        isIconActionDownDetected = false;
        isIconActionUpDetected = false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int currentPage) {
        setCurrentItem(currentPage);
        if (mListener != null) {
            mListener.onPageSelected(currentPage);
        }
    }

    @Override
    public void setViewPager(final ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        PagerAdapter adapter = view.getAdapter();
        final int count = adapter.getCount();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        mViewPager = view;
        view.setOnPageChangeListener(this);
        refresh();
    }

    public void refresh() {
        removeAllViews();
        final PagerAdapter iconAdapter = mViewPager.getAdapter();
        int count = iconAdapter.getCount();
        if (count > 1) {
            for (int itemIndex = 0; itemIndex < count; itemIndex++) {
                View view = getNavigationDisplayView();
                addView(view);
            }
            if (mSelectedIndex > count) {
                mSelectedIndex = count - 1;
            }
            setCurrentItem(mSelectedIndex);
            requestLayout();
        } else {
            setVisibility(GONE);
        }
    }

    @NonNull
    protected View getNavigationDisplayView() {
        return new DotNavigationIcon(getContext());
    }

    @Override
    public void showNext() {
        isViewPagerInitialized();

        if (mSelectedIndex == mViewPager.getAdapter().getCount()) {
            return;
        }
        mViewPager.setCurrentItem(mSelectedIndex + 1);
    }

    @Override
    public void showPrevious() {
        isViewPagerInitialized();

        if (mSelectedIndex == 0) {
            return;
        }
        mViewPager.setCurrentItem(mSelectedIndex - 1);
    }

    @Override
    public void setViewPager(final ViewPager viewPager, int initialPosition) {
        setViewPager(viewPager);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        isViewPagerInitialized();
        mSelectedIndex = item;
        mViewPager.setCurrentItem(item);

        int childCount = getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            View child = getChildAt(childIndex);
            final boolean isSelected = (childIndex == item);
            child.setSelected(isSelected);
        }
    }

    private View getSelectedChild() {
        int childCount = getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            View child = getChildAt(childIndex);
            if (child.isSelected()) return child;
        }
        return null;
    }

    private void isViewPagerInitialized() {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }
}
