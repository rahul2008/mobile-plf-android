/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */
package com.philips.platform.uid.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;

/**
 * <code>DotNavigationIndicator</code> draw dots based on number of items in viewpager's Adapter.
 * Adapter must be set on <code>{@link ViewPager}</code>
 * before calling {@link DotNavigationIndicator#setViewPager(ViewPager)} or {@link DotNavigationIndicator#setCurrentItem(int)}
 * <p>
 * <p>The attributes mapping follows below table.</p>
 * <table border="2" width="85%" align="center" cellpadding="5">
 * <thead>
 * <tr><th>ResourceID</th> <th>Configuration</th></tr>
 * </thead>
 * <p>
 * <tbody>
 * <tr>
 * <td rowspan="1">uidDotNavigationDrawable</td>
 * <td rowspan="1">Drawable for navigation icons</td>
 * </tr>
 * <tr>
 * <td rowspan="1">uidDotNavigationIconSpacingLeft</td>
 * <td rowspan="1">Left margin for icon</td>
 * </tr>
 * <tr>
 * <td rowspan="1">uidDotNavigationIconSpacingRight</td>
 * <td rowspan="1">Right margin for icon</td>
 * </tr>
 * <tr>
 * <td rowspan="1">uidDotNavigationIconColorList</td>
 * <td rowspan="1">ColorStatelist for the navigation icons</td>
 * </tr>
 * <p>
 * </tbody>
 * <p>
 * </table>
 */


public class DotNavigationIndicator extends LinearLayout implements PageIndicator {
    private ViewPager viewPager;
    private OnPageChangeListener pageChangeListener;
    private int selectedIndex;
    private boolean isIconActionDownDetected;
    private boolean isIconActionUpDetected;
    private int iconLeftSpacing;
    private int iconRightSpacing;
    private Drawable backgroundDrawable;
    private int dotNavigationIconColorListID = -1;
    private boolean isCircularSwipeEnabled;

    private int initialPosition;
    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            drawIndicatorDots();
            setCurrentItem(initialPosition);
        }
    };

    ViewPager.OnAdapterChangeListener adapterChangeListener = new ViewPager.OnAdapterChangeListener() {
        @Override
        public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
            if (oldAdapter != null) {
                oldAdapter.unregisterDataSetObserver(dataSetObserver);
            }
            if (newAdapter != null) {
                newAdapter.registerDataSetObserver(dataSetObserver);
            }
            drawIndicatorDots();
        }
    };

    public DotNavigationIndicator(@NonNull Context context) {
        this(context, null);
    }

    public DotNavigationIndicator(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotNavigationIndicator(@NonNull final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        setGravity(Gravity.CENTER);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDDotNavigation, defStyleAttr, R.style.UIDDotNavigationIndicatorItemStyle);
        backgroundDrawable = typedArray.getDrawable(R.styleable.UIDDotNavigation_uidDotNavigationDrawable);
        //Need to check if this works even for normal drawables and icons along with vector

        iconLeftSpacing = typedArray.getDimensionPixelSize(R.styleable.UIDDotNavigation_uidDotNavigationIconSpacingLeft, -1);
        iconRightSpacing = typedArray.getDimensionPixelSize(R.styleable.UIDDotNavigation_uidDotNavigationIconSpacingRight, -1);
        dotNavigationIconColorListID = typedArray.getResourceId(R.styleable.UIDDotNavigation_uidDotNavigationIconColorList, -1);

        setContainerMinHeight(context, attrs);

        typedArray.recycle();
    }

    private void setContainerMinHeight(final Context context, final AttributeSet attrs) {
        final TypedArray minHeightTypedArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.minHeight});
        final int attributeIntValue = minHeightTypedArray.getDimensionPixelSize(0, getResources().getDimensionPixelSize(R.dimen.uid_dot_navigation_indicator_min_height));

        setMinimumHeight(attributeIntValue);
        minHeightTypedArray.recycle();
    }

    @VisibleForTesting
    public ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * Sets the viewpager to dot navigation so that no of dots can be drawn based on number of pages in adapter
     * Either of setViewpager(pager) or setViewPager(pager, position) must be called so as to draw Dots
     *
     * @param viewPager pager you want to bind to Dot indicator
     */
    @Override
    public void setViewPager(@NonNull final ViewPager viewPager) {
        setViewPager(viewPager, 0);
    }

    /**
     * Provide the view pager reference you want to bind with and provide a current position to be highlighted
     *
     * @param newViewPager    viewpager to bind to indicator
     * @param initialPosition with initialPosition selected/highlited
     */
    @Override
    public void setViewPager(final ViewPager newViewPager, int initialPosition) {
        if (viewPager == newViewPager) {
            return;
        }

        //Remove earlier listener if any.
        if (viewPager != null) {
            viewPager.removeOnPageChangeListener(this);
            viewPager.removeOnAdapterChangeListener(adapterChangeListener);
            if (viewPager.getAdapter() != null) {
                viewPager.getAdapter().unregisterDataSetObserver(dataSetObserver);
            }
        }
        if (newViewPager == null) {
            removeAllViews();
            viewPager = null;
            return;
        }

        this.initialPosition = initialPosition;
        viewPager = newViewPager;
        viewPager.addOnPageChangeListener(this);
        viewPager.addOnAdapterChangeListener(adapterChangeListener);

        if (viewPager.getAdapter() != null) {
            viewPager.getAdapter().registerDataSetObserver(dataSetObserver);
            drawIndicatorDots();
            setCurrentItem(initialPosition);
        }
    }

    /**
     * Enables circular swipe behavior
     *
     * @param circularSwipeEnabled Configures circular swipe property
     */
    @SuppressWarnings("unused")
    public void setCircularSwipeEnabled(boolean circularSwipeEnabled) {
        isCircularSwipeEnabled = circularSwipeEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int currentPage) {
        setCurrentItem(currentPage);
        if (pageChangeListener != null) {
            pageChangeListener.onPageSelected(currentPage);
        }
    }

    /**
     * Sets the current page in viewpager  and highlight the corresponding dot
     *
     * @param position to be shown on pager and corresponding dot to be highlighted
     */
    @Override
    public void setCurrentItem(int position) {
        if (!isViewPagerInitialized()) {
            return;
        }
        selectedIndex = position;
        viewPager.setCurrentItem(position);

        int childCount = getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            View child = getChildAt(childIndex);
            child.setSelected(childIndex == position);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        pageChangeListener = listener;
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
            handleTouchAndDelegatePreviousNextToPager(event);
            return true;
        }
        return false;
    }

    private void resetIconTouch() {
        isIconActionDownDetected = false;
        isIconActionUpDetected = false;
    }

    private void handleTouchAndDelegatePreviousNextToPager(final MotionEvent event) {
        final View selectedChild = getSelectedChild();
        if (selectedChild != null) {
            final MarginLayoutParams marginLayoutParams = (MarginLayoutParams) selectedChild.getLayoutParams();
            final boolean isLeft = event.getX() < (selectedChild.getX() - selectedChild.getPaddingStart() - marginLayoutParams.getMarginStart());
            final boolean isRight = event.getX() > (selectedChild.getX() + selectedChild.getWidth() + marginLayoutParams.getMarginEnd());
            if (isLeft)
                showPrevious();
            if (isRight)
                showNext();
        }
    }

    private void drawIndicatorDots() {
        if (!isViewPagerInitialized()) {
            return;
        }
        removeAllViews();
        final PagerAdapter iconAdapter = viewPager.getAdapter();
        int count = iconAdapter.getCount();
        if (count > 1) {
            for (int itemIndex = 0; itemIndex < count; itemIndex++) {
                addView(getNavigationDisplayView());
            }
            setCurrentItem(selectedIndex);
            setVisibility(VISIBLE);
            requestLayout();
        } else {
            setVisibility(GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    protected View getNavigationDisplayView() {
        final AppCompatImageView dotImageView = new AppCompatImageView(getContext(), null, R.attr.uidDotNavigationStyle);
        dotImageView.setBackground(getIndicatorBackground());
        final LinearLayout.LayoutParams lp = generateDefaultLayoutParams();

        lp.leftMargin = iconLeftSpacing;
        lp.rightMargin = iconRightSpacing;
        dotImageView.setLayoutParams(lp);
        dotImageView.setBackgroundTintList(getIndicatorTintList());

        return dotImageView;
    }

    protected ColorStateList getIndicatorTintList() {
        return ThemeUtils.buildColorStateList(getContext(),
                dotNavigationIconColorListID != -1 ? dotNavigationIconColorListID : R.color.uid_dot_navigation_icon_color);
    }

    protected Drawable getIndicatorBackground() {
        if (backgroundDrawable != null) {
            //noinspection ConstantConditions
            return backgroundDrawable.mutate().getConstantState().newDrawable();
        }
        return null;
    }

    /**
     * This is the method is responsible for showing next item when user clicks/taps on the right side of <br>
     * highlighted dot.
     */
    protected void showNext() {
        if (!isViewPagerInitialized()) {
            return;
        }

        int count = getViewPagerCount();
        if (!isCircularSwipeEnabled && selectedIndex == count - 1) {
            return;
        }

        viewPager.setCurrentItem((selectedIndex + 1) % count);
    }

    /**
     * This is the method is responsible for showing previous item when user clicks/taps on the left side of <br>
     * highlighted dot.
     */
    protected void showPrevious() {
        if (!isViewPagerInitialized()) {
            return;
        }

        if (!isCircularSwipeEnabled && selectedIndex == 0) {
            return;
        } else if (selectedIndex == 0) {
            selectedIndex = getViewPagerCount();
        }

        viewPager.setCurrentItem(selectedIndex - 1);
    }

    private int getViewPagerCount() {
        return viewPager.getAdapter().getCount();
    }

    @Nullable
    private View getSelectedChild() {
        int childCount = getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            View child = getChildAt(childIndex);
            if (child.isSelected()) return child;
        }
        return null;
    }

    private boolean isViewPagerInitialized() {
        return viewPager != null && viewPager.getAdapter() != null;
    }
}
