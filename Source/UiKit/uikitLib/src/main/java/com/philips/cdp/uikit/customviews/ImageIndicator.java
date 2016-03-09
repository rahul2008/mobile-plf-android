/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.dotnavigation.PageIndicator;
import com.philips.cdp.uikit.dotnavigation.onTouchUnSelectedViews;
import com.philips.cdp.uikit.utils.UikitUtils;

/**
 * ImageIndicator is a component which is used associated to Viewpager, we have provided a component named ImageIndicator to support Image Navigation.
 * <br>
 <p>
 <br>
 *         Steps to use ImageIndicator:
 *              <pre>

 <br>
 1.Please include in XML with below convention as per your required display metrics
 <br>
 &lt;com.philips.cdp.uikit.customviews.ImageIndicator
 android:layout_height="wrap_content"
 android:layout_width="wrap_content"
 android:padding="10dp" </b>/&gt
 <br>
 2.In Activity/Fragment please bind your View pager object as per below code
 <br>
 ViewPager pager = (ViewPager) findViewById(R.id.pager);
 pager.setAdapter(adaptor);
 ImageIndicator  indicator = (ImageIndicator) findViewById(R.id.indicator);
 indicator.setViewPager(pager);
 Drawable drawables[] = {ResourcesCompat.getDrawable(getResources(), R.drawable.apple, null),
 ResourcesCompat.getDrawable(getResources(), R.drawable.alarm, null),
 ResourcesCompat.getDrawable(getResources(), R.drawable.barchart, null),
 ResourcesCompat.getDrawable(getResources(), R.drawable.gear, null)};
 indicator.setImages(drawables);
 </p>
 */
public class ImageIndicator extends LinearLayout implements PageIndicator, onTouchUnSelectedViews {

    private final View parentView;
    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener pageChangeListener;
    private int themeBaseColor;
    private int currentPage;
    private int distanceBetweenCircles;
    private int scrollState;
    private onTouchUnSelectedViews onTouchUnSelectedViews;
    private Drawable unSelectedDrawable;
    private Drawable[] drawables;

    public ImageIndicator(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        parentView = LayoutInflater.from(context).inflate(
                R.layout.uikit_indicator, null);
        processAttributes(context, context.getResources());
    }

    public ImageIndicator(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parentView = LayoutInflater.from(context).inflate(
                R.layout.uikit_indicator, null);
        processAttributes(context, context.getResources());
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    private void processAttributes(final Context context, final Resources resources) {
        distanceBetweenCircles = (int) resources.getDimension(R.dimen.uikit_dot_navigation_distance_between_circles);
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor});
        themeBaseColor = a.getColor(0, resources.getColor(R.color.uikit_philips_blue));
    }

    private void reDrawView(final Drawable[] drawables) {
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
                drawViews(context, parent, drawables);
            }
        });

    }

    private void drawViews(final Context context, final LinearLayout parent, Drawable[] drawables) {
        if (drawables != null) {
            for (int i = 0; i < drawables.length; i++) {
                ImageView view = new ImageView(context);
                if (i == currentPage) {
                    applySelectedMetrics(view, drawables[i]);
                } else {
                    applyUnselectedMetrics(view, drawables[i], context);
                    setOnClickListener(i, view);
                }
                parent.addView(view);
            }
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
    private void applyUnselectedMetrics(final ImageView view, final Drawable drawable, Context context) {
        LayoutParams vp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vp.setMargins(0, 0, distanceBetweenCircles, 0);
        this.unSelectedDrawable = drawable;
        view.setLayoutParams(vp);
        view.setImageDrawable(drawable);
        view.getDrawable().setColorFilter(adjustAlpha(themeBaseColor, 0.5f), PorterDuff.Mode.SRC_ATOP);
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getDrawable(): sticking with deprecated API for now
    private void applySelectedMetrics(final ImageView view, final Drawable drawable) {
        LayoutParams vp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vp.setMargins(0, 0, distanceBetweenCircles, 0);
        view.setLayoutParams(vp);
        view.setImageDrawable(drawable);
        view.getDrawable().setColorFilter(themeBaseColor, PorterDuff.Mode.SRC_ATOP);
    }

    private int adjustAlpha(int color, float factor) {
        return UikitUtils.adjustAlpha(color, factor);
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
    //we need to support API lvl 14+
    /**
     *This API should be called to bind ViewPager to ImageIndicator
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
        reDrawView(drawables);
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
     *
     * @param item index to Set
     */
    @Override
    public void setCurrentItem(int item) {
        if (viewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        viewPager.setCurrentItem(item);
        currentPage = item;
        reDrawView(drawables);
    }

    /**
     * API to Re-draw Dots when required
     */
    @Override
    public void notifyDataSetChanged() {
        reDrawView(drawables);
    }

    /**
     * Callback When Scroll State is Changed
     *
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
        reDrawView(drawables);
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
            reDrawView(drawables);
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

    @Override
    public void onClickUnSelectedView(final View view, final int position) {
        viewPager.setCurrentItem(position, true);
    }

    public void setOnTouchUnSelectedViews(onTouchUnSelectedViews onTouchUnSelectedViews) {
        this.onTouchUnSelectedViews = onTouchUnSelectedViews;
    }

    /**
     * API to get filled color of image which is selected
     * @return Returns the value of color in integer
     */
    public int getFilledColor() {
        return themeBaseColor;
    }

    /**
     * API to set Color which is selected
     *
     * @param color
     */
    public void setFilledColor(int color) {
        this.themeBaseColor = color;
        reDrawView(drawables);
    }

    /**
     * API which returns Drawable of UnSelected Views
     *
     * @return
     */
    public Drawable getUnSelectedView() {
        return unSelectedDrawable;
    }

    /**
     * API to set Images to Support Image Navigation
     *
     * @param drawables - Configurable Drawables to Navigate
     */
    public void setImages(Drawable[] drawables) {
        this.drawables = drawables;
    }

}
