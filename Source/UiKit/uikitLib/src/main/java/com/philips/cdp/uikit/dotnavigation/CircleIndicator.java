package com.philips.cdp.uikit.dotnavigation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CircleIndicator extends LinearLayout implements PageIndicator, onTouchUnSelectedDots {

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;
    private boolean enableStrokeBackground;

    private int selectedCircleWidth;
    private int selectedCircleHeight;
    private int unSelectedCircleWidth;
    private int unSelectedCircleHeight;
    private int themeBaseColor;
    private int mCurrentPage;
    private int distanceBetweenCircles;
    private int mScrollState;
    private onTouchUnSelectedDots onTouchUnSelectedDots;

    public CircleIndicator(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        final Resources resources = getResources();
        processAttributes(context, resources);
        drawCircles();
    }

    public CircleIndicator(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final Resources resources = getResources();
        processAttributes(context, resources);
        drawCircles();
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    private void processAttributes(final Context context, final Resources resources) {
        selectedCircleWidth = (int) resources.getDimension(R.dimen.uikit_dot_navigation_selected_width);
        selectedCircleHeight = (int) resources.getDimension(R.dimen.uikit_dot_navigation_selected_height);
        unSelectedCircleWidth = (int) resources.getDimension(R.dimen.uikit_dot_navigation_unselected_width);
        unSelectedCircleHeight = (int) resources.getDimension(R.dimen.uikit_dot_navigation_unselected_height);
        distanceBetweenCircles = (int) resources.getDimension(R.dimen.uikit_dot_navigation_distance_between_circles);
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor, R.attr.enableStroke});
        themeBaseColor = a.getColor(0, resources.getColor(R.color.uikit_philips_blue));
        enableStrokeBackground = a.getBoolean(1, false);
    }

    private void drawCircles() {
        this.post(new Runnable() {
            @Override
            public void run() {
                Context context = getContext();

                if (mViewPager == null) {
                    return;
                }
                final int count = mViewPager.getAdapter().getCount();
                if (count == 0) {
                    return;
                }

                if (mCurrentPage >= count) {
                    setCurrentItem(count - 1);
                    return;
                }

                final LinearLayout parent = getParentLayout(context);
                drawDots(context, count, parent);
            }
        });

    }

    private void drawDots(final Context context, final int count, final LinearLayout parent) {
        for (int i = 0; i < count; i++) {
            View view = new View(context);

            GradientDrawable gradientDrawable = getShapeDrawable();
            if (i == mCurrentPage) {
                applySelectedMetrics(view, gradientDrawable);
            } else {
                applyUnselectedMetrics(view, gradientDrawable);
                setOnClickListener(i, view);
            }
            parent.addView(view);
        }
    }

    private void setOnClickListener(final int position, final View view) {
        view.setClickable(true);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickUnSelectedCircle(v, position);
                if (onTouchUnSelectedDots != null) {
                    onTouchUnSelectedDots.onClickUnSelectedCircle(v, position);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    private void applyUnselectedMetrics(final View view, final GradientDrawable gradientDrawable) {
        gradientDrawable.setAlpha((int) 76.5);
        LayoutParams vp = new LayoutParams(unSelectedCircleWidth, unSelectedCircleHeight);
        vp.setMargins(0, 0, distanceBetweenCircles, 0);
        if (enableStrokeBackground) {
            gradientDrawable.setStroke(2, themeBaseColor);
            gradientDrawable.setColor(getResources().getColor(android.R.color.transparent));
        }
        view.setLayoutParams(vp);
        view.setBackgroundDrawable(gradientDrawable);
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getDrawable(): sticking with deprecated API for now
    private void applySelectedMetrics(final View view, final GradientDrawable gradientDrawable) {
        LayoutParams vp = new LayoutParams(selectedCircleWidth, selectedCircleHeight);
        vp.setMargins(0, 0, distanceBetweenCircles, 0);
        view.setLayoutParams(vp);
        view.setBackgroundDrawable(gradientDrawable);
    }

    @NonNull
    private LinearLayout getParentLayout(final Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.uikit_circle_indicator, null);
        this.removeAllViews();
        this.addView(view);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.uikit_linear);
        linearLayout.removeAllViews();
        return linearLayout;
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getDrawable(): sticking with deprecated API for now
    private GradientDrawable getShapeDrawable() {
        Resources resources = getResources();
        final GradientDrawable gradientDrawable = (GradientDrawable) resources.getDrawable(R.drawable.uikit_dot_circle);
        gradientDrawable.setColor(themeBaseColor);
        gradientDrawable.mutate();
        return gradientDrawable;
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+
    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        if (view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        mViewPager.setOnPageChangeListener(this);
        drawCircles();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        drawCircles();
    }

    @Override
    public void notifyDataSetChanged() {
        drawCircles();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;

        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mCurrentPage = position;
        drawCircles();
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position;
            drawCircles();
        }
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    public int getSelectedCircleWidth() {
        return selectedCircleWidth;
    }

    public void setSelectedCircleWidth(final int selectedCircleWidth) {
        this.selectedCircleWidth = selectedCircleWidth;
    }

    public int getSelectedCircleHeight() {
        return selectedCircleHeight;
    }

    public void setSelectedCircleHeight(final int selectedCircleHeight) {
        this.selectedCircleHeight = selectedCircleHeight;
    }

    public int getUnSelectedCircleWidth() {
        return unSelectedCircleWidth;
    }

    public void setUnSelectedCircleWidth(final int unSelectedCircleWidth) {
        this.unSelectedCircleWidth = unSelectedCircleWidth;
    }

    public int getUnSelectedCircleHeight() {
        return unSelectedCircleHeight;
    }

    public void setUnSelectedCircleHeight(final int unSelectedCircleHeight) {
        this.unSelectedCircleHeight = unSelectedCircleHeight;
    }

    @Override
    public void onClickUnSelectedCircle(final View view, final int position) {
        mViewPager.setCurrentItem(position);
    }

    public void setOnTouchUnSelectedDots(onTouchUnSelectedDots onTouchUnSelectedDots) {
        this.onTouchUnSelectedDots = onTouchUnSelectedDots;
    }

    public int getFilledColor() {
        return themeBaseColor;
    }

}
