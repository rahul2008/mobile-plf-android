package com.philips.cdp.uikit.costumviews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
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
import com.philips.cdp.uikit.dotnavigation.onTouchUnSelectedDots;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ImageIndicator extends LinearLayout implements PageIndicator, onTouchUnSelectedDots {

    private final View parentView;
    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener pageChangeListener;
    private int themeBaseColor;
    private int currentPage;
    private int distanceBetweenCircles;
    private int scrollState;
    private onTouchUnSelectedDots onTouchUnSelectedDots;
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

    public Drawable getUnSelectedView() {
        return unSelectedDrawable;
    }

    public void setImages(Drawable[] drawables) {
        this.drawables = drawables;
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    private void processAttributes(final Context context, final Resources resources) {
        distanceBetweenCircles = (int) resources.getDimension(R.dimen.uikit_dot_navigation_distance_between_circles);
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor});
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
                drawDots(context, count, parent, drawables);
            }
        });

    }

    private void drawDots(final Context context, final int count, final LinearLayout parent, Drawable[] drawables) {
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
    private void applyUnselectedMetrics(final ImageView view, final Drawable drawable, Context context) {
//        drawable.setAlpha(context.getResources().getInteger(R.integer.uikit_dot_navigation_unselected_alpha));
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
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
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

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (viewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        viewPager.setCurrentItem(item);
        currentPage = item;
        reDrawView(drawables);
    }

    @Override
    public void notifyDataSetChanged() {
        reDrawView(drawables);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        scrollState = state;

        if (pageChangeListener != null) {
            pageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentPage = position;
        reDrawView(drawables);
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

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

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        pageChangeListener = listener;
    }

    @Override
    public void onClickUnSelectedCircle(final View view, final int position) {
        viewPager.setCurrentItem(position, true);
    }

    public void setOnTouchUnSelectedDots(onTouchUnSelectedDots onTouchUnSelectedDots) {
        this.onTouchUnSelectedDots = onTouchUnSelectedDots;
    }

    public int getFilledColor() {
        return themeBaseColor;
    }

}
