package com.philips.cdp.uikit.costumviews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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

    public CircleIndicator(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.actionButtonStyleRef);
    }

    public CircleIndicator(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
        processAttributes(context, attrs, defStyle, getResources());
        invalidate();
    }

    private void processAttributes(final Context context, final AttributeSet attrs, final int defStyle, final Resources resources) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.PageIndicator, defStyle,
                R.style.Philips_dotNavigation);
        selectedCircleWidth = typedArray.getInt(R.styleable.PageIndicator_selectedCircleWidth, 4);
        selectedCircleHeight = typedArray.getInt(R.styleable.PageIndicator_selectedCircleHeight, 4);
        unSelectedCircleWidth = typedArray.getInt(R.styleable.PageIndicator_unSelectedCircleWidth, 4);
        unSelectedCircleHeight = typedArray.getInt(R.styleable.PageIndicator_unSelectedCircleHeight, 4);
        distanceBetweenCircles = typedArray.getInt(R.styleable.PageIndicator_distanceBetweenCircle, 4);
        setBaseColor(context, resources);
    }

    private void setBaseColor(final Context context, final Resources resources) {
        TypedArray a = context.getTheme().obtainStyledAttributes(R.style.Theme_Philips, new int[]{R.attr.baseColor});
        themeBaseColor = a.getColor(0, resources.getColor(R.color.uikit_philips_blue));
    }

    @Override
    public void invalidate() {
        super.invalidate();
        drawCircles();
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

        View view = LayoutInflater.from(context).inflate(
                R.layout.uikit_circle_indicator, null);
        this.addView(view);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.uikit_linear);
        linearLayout.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundDrawable(getSelectedShapeDrawable(i));
            LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(selectedCircleWidth, selectedCircleHeight);
            vp.setMargins(0, 0, (int) getResources().getDimension(R.dimen.uikit_dot_navigation_distance_between_circles), 0);
            imageView.setLayoutParams(vp);
            linearLayout.addView(imageView);
        }
    }

    private GradientDrawable getSelectedShapeDrawable(final int i) {
        Resources resources = getResources();
        final GradientDrawable gradientDrawable = (GradientDrawable) resources.getDrawable(R.drawable.uikit_dot_circle);
//        gradientDrawable.setColor(resources.getColor(android.R.color.transparent));
//        gradientDrawable.setStroke(2,resources.getColor(R.color.uikit_philips_Red));
        if (i == 5) {
            gradientDrawable.setColor(resources.getColor(R.color.uikit_philips_dark_aqua));
            gradientDrawable.setStroke(0, resources.getColor(android.R.color.transparent));
        }
        gradientDrawable.mutate();
        return gradientDrawable;
    }

    @Override
    public void setViewPager(final ViewPager view) {

    }

    @Override
    public void setViewPager(final ViewPager view, final int initialPosition) {

    }

    @Override
    public void setCurrentItem(final int item) {

    }

    @Override
    public void setOnPageChangeListener(final ViewPager.OnPageChangeListener listener) {

    }

    @Override
    public void notifyDataSetChanged() {

    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(final int position) {

    }

    @Override
    public void onPageScrollStateChanged(final int state) {

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
}
