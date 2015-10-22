package com.philips.cdp.uikit.costumviews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CircleIndicator extends LinearLayout implements PageIndicator {

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
    private LinearLayout linearLayout;

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
//        this.setGravity(Gravity.CENTER_HORIZONTAL);
    }

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

        linearLayout = getLinearLayout(context);
        drawDots(context, count, linearLayout);
    }

    private void drawDots(final Context context, final int count, final LinearLayout linearLayout) {
        for (int i = 0; i < count; i++) {
            View imageView = new View(context);

            GradientDrawable gradientDrawable = getShapeDrawable();
            if (i == mCurrentPage) {
                applySelectedMetrics(imageView, gradientDrawable);
            } else {
                applyUnselectedMetrics(imageView, gradientDrawable);
                final int position = i;
                imageView.setClickable(true);
                imageView.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(final View v, final MotionEvent event) {
                        onClickUnSelectedCircle(v, position);
                        return false;
                    }
                });
            }
//            imageView.setScaleType(ImageView.ScaleType.CENTER);
            linearLayout.addView(imageView);
        }
    }

    private void applyUnselectedMetrics(final View imageView, final GradientDrawable gradientDrawable) {
        gradientDrawable.setAlpha((int) 178.5);
        LayoutParams vp = new LayoutParams(unSelectedCircleWidth, unSelectedCircleHeight);
        vp.setMargins(0, 0, distanceBetweenCircles, 0);
        if (enableStrokeBackground) {
            gradientDrawable.setStroke(2, themeBaseColor);
            gradientDrawable.setColor(getResources().getColor(android.R.color.transparent));
        }
        imageView.setLayoutParams(vp);
        imageView.setBackgroundDrawable(gradientDrawable);
    }

    private void applySelectedMetrics(final View imageView, final GradientDrawable gradientDrawable) {
        LayoutParams vp = new LayoutParams(selectedCircleWidth, selectedCircleHeight);
        vp.setMargins(0, 0, distanceBetweenCircles, 0);
        imageView.setLayoutParams(vp);
        imageView.setBackgroundDrawable(gradientDrawable);
    }

    @NonNull
    private LinearLayout getLinearLayout(final Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.uikit_circle_indicator, null);
        this.removeAllViews();
        this.addView(view);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.uikit_linear);
        linearLayout.removeAllViews();
        return linearLayout;
    }

    private GradientDrawable getShapeDrawable() {
        Resources resources = getResources();
        final GradientDrawable gradientDrawable = (GradientDrawable) resources.getDrawable(R.drawable.uikit_dot_circle);
        gradientDrawable.setColor(themeBaseColor);
        gradientDrawable.mutate();
        return gradientDrawable;
    }

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
//        changeSelectedPosition(item);
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
//        changeSelectedPosition(position);
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position;
            drawCircles();
//            changeSelectedPosition(position);
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

    private void changeSelectedPosition(int position) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (i == position) {
                View view = linearLayout.getChildAt(position);
                applySelectedMetrics(view, (GradientDrawable) view.getBackground());
                view.invalidate();
            } else {
                View view = linearLayout.getChildAt(position);
                applyUnselectedMetrics(view, (GradientDrawable) view.getBackground());
                view.invalidate();
            }
        }
    }


}
