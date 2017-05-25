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
    private int dotNavigationIconColorlist = -1;

    public DotNavigationIndicator(@NonNull Context context) {
        this(context, null);
    }

    public DotNavigationIndicator(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotNavigationIndicator(@NonNull final Context context, final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotNavigationIndicator(@NonNull final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDDotNavigation, defStyleAttr, R.style.UIDDotNavigation);
        backgroundDrawable = typedArray.getDrawable(R.styleable.UIDDotNavigation_uidDotNavigationDrawable);
        //Need to check if this works even for normal drawables and icons along with vector

        iconLeftSpacing = typedArray.getDimensionPixelSize(R.styleable.UIDDotNavigation_uidDotNavigationIconSpacingLeft, -1);
        iconRightSpacing = typedArray.getDimensionPixelSize(R.styleable.UIDDotNavigation_uidDotNavigationIconSpacingRight, -1);
        dotNavigationIconColorlist = typedArray.getResourceId(R.styleable.UIDDotNavigation_uidDotNavigationIconColorList, -1);

        typedArray.recycle();
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
        if (this.viewPager == viewPager) {
            return;
        }
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        drawIndicatorDots();
    }

    /**
     * @param state
     * @Inherit
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
     * Provide the view pager reference you want to bind with and provide a current position to be highlighted
     *
     * @param viewPager       viewpager to bind to indicator
     * @param initialPosition with initialPosition selected/highlited
     */
    @Override
    public void setViewPager(@NonNull final ViewPager viewPager, int initialPosition) {
        setViewPager(viewPager);
        setCurrentItem(initialPosition);
    }

    /**
     * Sets the current page in viewpager  and highlight the corresponding dot
     *
     * @param position to be shown on pager and corresponding dot to be highlighted
     */
    @Override
    public void setCurrentItem(int position) {
        isViewPagerInitialized();
        selectedIndex = position;
        viewPager.setCurrentItem(position);

        int childCount = getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            View child = getChildAt(childIndex);
            child.setSelected(childIndex == position);
        }
    }

    /**
     * Callback interface for responding to changing state of the selected page of viewpager
     *
     * @param listener
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
            handleTouchAndDeletePreviousNextToPager(event);
            return true;
        }
        return false;
    }

    private void resetIconTouch() {
        isIconActionDownDetected = false;
        isIconActionUpDetected = false;
    }

    private void handleTouchAndDeletePreviousNextToPager(final MotionEvent event) {
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
        removeAllViews();
        final PagerAdapter iconAdapter = viewPager.getAdapter();
        int count = iconAdapter.getCount();
        if (count > 1) {
            for (int itemIndex = 0; itemIndex < count; itemIndex++) {
                addView(getNavigationDisplayView());
            }
            setCurrentItem(selectedIndex);
            requestLayout();
        } else {
            setVisibility(GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    protected View getNavigationDisplayView() {
        final AppCompatImageView dotImageView = new AppCompatImageView(getContext(), null, R.attr.dotNavigationStyle);
        dotImageView.setBackground(getBackgroundDrawable());
        final LinearLayout.LayoutParams lp = generateDefaultLayoutParams();

        lp.leftMargin = iconLeftSpacing;
        lp.rightMargin = iconRightSpacing;
        dotImageView.setLayoutParams(lp);
        dotImageView.setBackgroundTintList(getDotTint());

        return dotImageView;
    }

    protected ColorStateList getDotTint() {
        return ThemeUtils.buildColorStateList(getResources(), getContext().getTheme(),
                dotNavigationIconColorlist != -1 ? dotNavigationIconColorlist : R.color.uid_dot_navigation_icon_color);
    }

    protected Drawable getBackgroundDrawable() {
        if (backgroundDrawable != null) {
            return backgroundDrawable.mutate().getConstantState().newDrawable();
        }
        return null;
    }

    /**
     * This is the method is responsible for showing next item when user clicks/taps on the right side of <br>
     * highlighted dot. If you want to provide circular behavior then please override this method
     */
    protected void showNext() {
        isViewPagerInitialized();

        if (selectedIndex == viewPager.getAdapter().getCount()) {
            return;
        }

        viewPager.setCurrentItem(selectedIndex + 1);
    }

    /**
     * This is the method is responsible for showing previous item when user clicks/taps on the left side of <br>
     * highlighted dot. If you want to provide circular behavior then please override this method
     */
    protected void showPrevious() {
        isViewPagerInitialized();

        if (selectedIndex == 0) {
            return;
        }
        viewPager.setCurrentItem(selectedIndex - 1);
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

    private void isViewPagerInitialized() {
        if (viewPager == null || viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or adapter not initialized.");
        }
    }
}
