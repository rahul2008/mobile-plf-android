/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.customviews;

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
import com.philips.cdp.uikit.dotnavigation.PageIndicator;
import com.philips.cdp.uikit.dotnavigation.onTouchUnSelectedViews;

/**
 * CircleIndicator is a component which is used associated to Viewpager, we have provided a component named CircleIndicator to support Dot Navigation.
 * <br>
 <p>
 <br>
 *         Steps to use CircleIndicator:
 *              <pre>

 <br>
 1.Please include in XML with below convention as per your required display metrics
 <br>
 &lt;com.philips.cdp.uikit.customviews.CircleIndicator
 android:layout_height="wrap_content"
 android:layout_width="wrap_content"
 android:padding="10dp" </b>/&gt
 <br>
 2.In Activity/Fragment please bind your View pager object as per below code
 <br>
 ViewPager pager = (ViewPager) findViewById(R.id.pager);
 pager.setAdapter(adaptor);
 CircleIndicator  indicator = (CircleIndicator) findViewById(R.id.indicator);
 indicator.setViewPager(pager);
 </p>
 */
public class CircleIndicator extends LinearLayout implements PageIndicator, com.philips.cdp.uikit.dotnavigation.onTouchUnSelectedViews {

    private final View parentView;
    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener pageChangeListener;
    private boolean enableStrokeBackground;

    private int selectedCircleWidth;
    private int selectedCircleHeight;
    private int unSelectedCircleWidth;
    private int unSelectedCircleHeight;
    private int themeBaseColor;
    private int currentPage;
    private int distanceBetweenCircles;
    private int scrollState;
    private onTouchUnSelectedViews onTouchUnSelectedViews;
    private GradientDrawable unSelectedGradientDrawable;
    private int strokeColor;

    public CircleIndicator(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        final Resources resources = getResources();
        parentView = LayoutInflater.from(context).inflate(
                R.layout.uikit_indicator, null);
        processAttributes(context, resources);
        reDrawView();
    }

    public CircleIndicator(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final Resources resources = getResources();
        parentView = LayoutInflater.from(context).inflate(
                R.layout.uikit_indicator, null);
        processAttributes(context, resources);
        reDrawView();
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    private void processAttributes(final Context context, final Resources resources) {
        selectedCircleWidth = (int) resources.getDimension(R.dimen.uikit_dot_navigation_selected_width);
        selectedCircleHeight = (int) resources.getDimension(R.dimen.uikit_dot_navigation_selected_height);
        unSelectedCircleWidth = (int) resources.getDimension(R.dimen.uikit_dot_navigation_unselected_width);
        unSelectedCircleHeight = (int) resources.getDimension(R.dimen.uikit_dot_navigation_unselected_height);
        distanceBetweenCircles = (int) resources.getDimension(R.dimen.uikit_dot_navigation_distance_between_circles);
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor, R.attr.uikit_enableStroke, R.attr.uikit_strokeColor});
        themeBaseColor = a.getColor(0, resources.getColor(R.color.uikit_philips_blue));
        enableStrokeBackground = a.getBoolean(1, false);
        strokeColor = a.getColor(2, themeBaseColor);

        a.recycle();
    }

    private void reDrawView() {
        this.post(new Runnable() {
            @Override
            public void run() {
                Context context = getContext();

                if (viewPager == null) {
                    return;
                }
                final int count = viewPager.getAdapter().getCount();
                if (count == 0) {
                    return;
                }

                if (currentPage >= count) {
                    setCurrentItem(count - 1);
                    return;
                }

                final LinearLayout parent = getParentLayout();
                drawDots(context, count, parent);
            }
        });

    }

    private void drawDots(final Context context, final int count, final LinearLayout parent) {
        for (int i = 0; i < count; i++) {
            View view = new View(context);

            GradientDrawable gradientDrawable = getShapeDrawable();
            if (i == currentPage) {
                applySelectedMetrics(view, gradientDrawable);
            } else {
                applyUnselectedMetrics(view, gradientDrawable, context);
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
                onClickUnSelectedView(v, position);
                if (onTouchUnSelectedViews != null) {
                    onTouchUnSelectedViews.onClickUnSelectedView(v, position);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    private void applyUnselectedMetrics(final View view, final GradientDrawable gradientDrawable, Context context) {
        gradientDrawable.setAlpha(context.getResources().getInteger(R.integer.uikit_dot_navigation_unselected_alpha));
        LayoutParams vp = new LayoutParams(unSelectedCircleWidth, unSelectedCircleHeight);
        vp.setMargins(0, 0, distanceBetweenCircles, 0);
        if (enableStrokeBackground) {
            gradientDrawable.setStroke(2, strokeColor);
            gradientDrawable.setColor(getResources().getColor(android.R.color.transparent));
        }
        this.unSelectedGradientDrawable = gradientDrawable;
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
    private LinearLayout getParentLayout() {
        this.removeAllViews();
        this.addView(parentView);
        final LinearLayout linearLayout = (LinearLayout) parentView.findViewById(R.id.uikit_linear);
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
    /**
     *This API should be called to bind ViewPager to CircleIndicator
     * @param view Instance of ViewPager
     */
    @Override
    public void setViewPager(ViewPager view) {
        if (viewPager == view) {
            return;
        }
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(null);
        }
        if (view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        viewPager = view;
        viewPager.setOnPageChangeListener(this);
        reDrawView();
    }

    /**
     * API to set ViewPager with desired position
     *
     * @param view
     * @param initialPosition
     */
    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    /**
     * API to set Current Item
     * @param item index to Set
     */
    @Override
    public void setCurrentItem(int item) {
        if (viewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        viewPager.setCurrentItem(item);
        currentPage = item;
        reDrawView();
    }

    /**
     * API to Re-draw Dots when required
     */
    @Override
    public void notifyDataSetChanged() {
        reDrawView();
    }

    /**
     * Callback When Scroll State is Changed
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        scrollState = state;

        if (pageChangeListener != null) {
            pageChangeListener.onPageScrollStateChanged(state);
        }
    }


    /**
     * Callback When Page Scrolled
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentPage = position;
        reDrawView();
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    /**
     * Callback when Page is Selected
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
            currentPage = position;
            reDrawView();
        }
        if (pageChangeListener != null) {
            pageChangeListener.onPageSelected(position);
        }
    }

    /**
     * API to register interface for Callbacks
     * @param listener
     */
    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        pageChangeListener = listener;
    }

    /**
     * API to get Width of Circle which is currently selected
     * @return Returns value of width
     */
    public int getSelectedCircleWidth() {
        return selectedCircleWidth;
    }

    /**
     * API to set Selected Circle width
     * @param selectedCircleWidth Value of width in integer
     */
    public void setSelectedCircleWidth(final int selectedCircleWidth) {
        this.selectedCircleWidth = selectedCircleWidth;
        reDrawView();
    }

    /**
     * API to get Height of Circle which is currently selected
     * @return Returns value of height
     */
    public int getSelectedCircleHeight() {
        return selectedCircleHeight;
    }

    /**
     * API to set Selected Circle height
     * @param selectedCircleHeight Value of height in integer
     */
    public void setSelectedCircleHeight(final int selectedCircleHeight) {
        this.selectedCircleHeight = selectedCircleHeight;
        reDrawView();
    }

    /**
     * API to get Width of Circle which is not selected
     * @return Returns value of width
     */
    public int getUnSelectedCircleWidth() {
        return unSelectedCircleWidth;
    }

    /**
     * API to set Unselected Circle width
     * @param unSelectedCircleWidth Value of width in integer
     */
    public void setUnSelectedCircleWidth(final int unSelectedCircleWidth) {
        this.unSelectedCircleWidth = unSelectedCircleWidth;
        reDrawView();
    }

    /**
     * API to get Height of Circle which is not selected
     * @return Returns value of height
     */
    public int getUnSelectedCircleHeight() {
        return unSelectedCircleHeight;
    }

    /**
     * API to set Unselected Circle height
     * @param unSelectedCircleHeight Value of height in integer
     */
    public void setUnSelectedCircleHeight(final int unSelectedCircleHeight) {
        this.unSelectedCircleHeight = unSelectedCircleHeight;
        reDrawView();
    }

    /**
     * API to enable Stroke Background
     * @param enableStrokeBackground
     */
    public void setEnableStrokeBackground(boolean enableStrokeBackground) {
        this.enableStrokeBackground = enableStrokeBackground;
        reDrawView();
    }

    @Override
    public void onClickUnSelectedView(final View view, final int position) {
        viewPager.setCurrentItem(position, true);
    }

    public void setOnTouchUnSelectedViews(onTouchUnSelectedViews onTouchUnSelectedViews) {
        this.onTouchUnSelectedViews = onTouchUnSelectedViews;
    }

    /**
     * API to get filled color of circle which is selected
     * @return Returns the value of color in integer
     */
    public int getFilledColor() {
        return themeBaseColor;
    }

    /**
     * API to get Stroke color of circle which is selected
     * @return Returns the value of color in integer
     */
    public int getStrokeColor() {
        return strokeColor;
    }

    /**
     * API to set Stroke Color when Stroke is enabled
     * @param strokeColor
     */
    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        reDrawView();
    }

    /**
     * API to set Color which is selected
     * @param color
     */
    public void setFillColor(int color) {
        themeBaseColor = color;
        reDrawView();
    }

    /**
     * API used to support automation Testing
     *
     * @return
     */
    public GradientDrawable getUnSelectedDot() {
        return unSelectedGradientDrawable;
    }




}
