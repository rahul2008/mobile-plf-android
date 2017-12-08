/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.ColorFilterStateListDrawable;

/**
 * Helper class to initialize tabs and adjusts the modes.
 * For custom image or text background, the selector must be set before creating new tabs.
 * <br>
 * Tabs should fill the width on phones and centered on Tablets.
 */
public class TabUtils {

    boolean isTablet;
    private int selectedColor;
    //Provide theeming facility for text color.
    private int enabledColor;
    private Context context;
    private TabLayout tabLayout;
    private ColorFilter enabledFilter;
    private ColorFilter selectedFilter;
    private Drawable imageSelector;
    private boolean ignoreImageTheme;
    private ColorStateList textSelector;
    private int textColor = Integer.MIN_VALUE;

    public TabUtils(Context context, TabLayout tabLayout, boolean withIcon) {
        this.context = context;
        this.tabLayout = tabLayout;
        isTablet = context.getResources().getBoolean(R.bool.uikit_istablet);
        initSelectionColors();
        initIconColorFilters();
    }

    /**
     * This must be called in onResume.
     * It adjusts the mode (fill on phones and center on tablet) of the tabs.
     * Due to strange behavior of mode and gravity, we need this function.
     *
     * @param tabLayout
     * @param context
     */
    public static void adjustTabs(final TabLayout tabLayout, final Context context) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                //Measure width of 1st child. HSView by default has full screen view
                int tabLayoutWidth = tabLayout.getChildAt(0).getWidth();

                DisplayMetrics metrics = new DisplayMetrics();
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getMetrics(metrics);
                int windowWidth = metrics.widthPixels;

                boolean isTablet = context.getResources().getBoolean(R.bool.uikit_istablet);
                if (tabLayoutWidth <= windowWidth) {
                    tabLayout.setTabMode(TabLayout.MODE_FIXED);
                    int gravity = isTablet ? TabLayout.GRAVITY_CENTER : TabLayout.GRAVITY_FILL;
                    tabLayout.setTabGravity(gravity);
                } else {
                    if (tabLayout.getTabMode() != TabLayout.MODE_SCROLLABLE)
                        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                }
            }
        });
    }

    /**
     * In case UIKit default tabstyle is used this function must be called to hide the action bar
     * shadow.
     *
     * @param activity
     */
    public static void disableActionbarShadow(Activity activity) {
        if (activity == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (activity instanceof AppCompatActivity) {
                if (((AppCompatActivity) activity).getSupportActionBar() != null)
                    ((AppCompatActivity) activity).getSupportActionBar().setElevation(0);
            } else {
                if (activity.getActionBar() != null)
                    activity.getActionBar().setElevation(0);
            }
        } else {
            View content = activity.findViewById(android.R.id.content);
            if (content != null && content.getParent() instanceof ActionBarOverlayLayout) {
                ((ViewGroup) content.getParent()).setWillNotDraw(true);

                if (content instanceof FrameLayout) {
                    ((FrameLayout)content).setForeground(null);
                }
            }
        }
    }

    /**
     * Creates a new Tab. Initializes the tab with different layouts.
     * If image is required in tabs, it must be called with non zero value for image resource id
     * and later can be udpated with proper resourceID or drawable.
     * <br>
     * For custom background of image/Text please set the selector prior to creating new tab
     *
     * @param titleResID    ResourceId for the String.
     * @param imageDrawable Image Drawable resource ID. Must be greater than 0 if image is required in tab.
     *                      <br>
     *                      In case of drawable, provide any dummy drawable and call
     *                      {@link TabUtils#setIcon(TabLayout.Tab, Drawable, boolean)}  instead.
     * @param badgeCount    Badge count
     * @return
     */
    public TabLayout.Tab newTab(int titleResID, int imageDrawable, final int badgeCount) {
        TabLayout.Tab newTab = tabLayout.newTab();
        View customView;
        TextView countView;
        if (imageDrawable > 0) {
            customView = LayoutInflater.from(context).inflate(R.layout.uikit_tab_with_image, null);
            //Set icon for the tab
            ImageView iconView = (ImageView) customView.findViewById(R.id.tab_icon);
            if (imageSelector != null || ignoreImageTheme) {
                iconView.setImageDrawable(imageSelector);
            } else {
                Drawable d = ResourcesCompat.getDrawable(context.getResources(), imageDrawable, null);
                iconView.setImageDrawable(getTabIconSelector(d));
            }
            iconView.setVisibility(View.VISIBLE);

            //Update count
            countView = (TextView) customView.findViewById(R.id.tab_count);
            if (badgeCount > 0) {
                countView.setText(Integer.toString(badgeCount));
                countView.setVisibility(View.VISIBLE);
            }
        } else {
            customView = LayoutInflater.from(context).inflate(R.layout.uikit_tab_textonly, null);
        }

        //We must do this to get the full tab view width
        customView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //Set title text
        TextView title = (TextView) customView.findViewById(R.id.tab_title);
        if (titleResID <= 0) {
            title.setVisibility(View.GONE);
        } else {
            title.setText(titleResID);
        }

        if (textColor != Integer.MIN_VALUE) {
            title.setTextColor(textColor);
        } else if (textSelector != null) {
            title.setTextColor(textSelector);
        } else {
            title.setTextColor(getTextSelector());
        }

        /*Can we really achieve two side dividers in tablet?
            That makes tabs to take maxTabWidth which losses the sense of feel of center gravity.
        */
  /*
         int tabCount = tabLayout.getTabCount();
         if ((isTablet && tabCount > 0) || (!isTablet && tabCount == 0)) {
            customView.findViewById(R.id.tab_divider).setVisibility(View.GONE);
        }

        if (isTablet) {
            customView.findViewById(R.id.tab_divider_last).setVisibility(View.VISIBLE);
        }*/

        if (tabLayout.getTabCount() == 0) {
            customView.findViewById(R.id.tab_divider).setVisibility(View.GONE);
        }

        newTab.setCustomView(customView);
        return newTab;
    }

    /**
     * Sets custom selector the images and ignores the theme based image background
     * Either of the one should be set to use the custom drawable or ignoreTheme
     *
     * @param selector
     * @param ignoreTheme
     */
    public void setImageSelector(Drawable selector, boolean ignoreTheme) {
        imageSelector = selector;
        ignoreImageTheme = ignoreTheme;
    }

    /**
     * Sets the color for the tab text
     *
     * @param color Text color to be applied.
     */
    public void setTextSelector(int color) {
        textColor = color;
        textSelector = null;
    }

    /**
     * Sets the image drawable for the tab.
     *
     * @param tab      Tab on which the image needs to be applied.
     * @param drawable Drawable for the tab.
     * @param useTheme Flag to set theme based tinting over the image.
     */
    public void setIcon(TabLayout.Tab tab, Drawable drawable, boolean useTheme) {
        ImageView iconView = (ImageView) tab.getCustomView().findViewById(R.id.tab_icon);
        Drawable target = useTheme ? getTabIconSelector(drawable) : drawable;
        iconView.setImageDrawable(target);
        iconView.setVisibility(View.VISIBLE);
    }

    /**
     * Sets title for the tabs.
     *
     * @param tab   Target tab
     * @param title String resource for the title
     */
    public void setTitle(TabLayout.Tab tab, String title) {
        TextView titleView = (TextView) tab.getCustomView().findViewById(R.id.tab_title);
        titleView.setText(title);
        titleView.setVisibility(View.VISIBLE);
    }

    /**
     * Set the count on the tab.
     *
     * @param tab
     * @param count Must be a int value
     */
    public void setCount(TabLayout.Tab tab, int count) {
        TextView countView = (TextView) tab.getCustomView().findViewById(R.id.tab_count);
        if (countView != null) {
            int visibility = count == 0 ? View.GONE : View.VISIBLE;
            if (count > 0) {
                countView.setText(Integer.toString(count));
            }
            countView.setVisibility(visibility);
        }
    }

    /**
     * Sets the title on the tab with string resource id
     *
     * @param tab
     * @param resID String resource id for the title
     */
    public void setTitle(TabLayout.Tab tab, int resID) {
        TextView titleView = (TextView) tab.getCustomView().findViewById(R.id.tab_title);
        titleView.setText(resID);
        titleView.setVisibility(View.VISIBLE);
    }

    private ColorStateList getTextSelector() {
        int[][] states = {{android.R.attr.state_selected}, {}};
        int[] colors = {selectedColor, enabledColor};
        return new ColorStateList(states, colors);
    }

    /**
     * Sets the colorstate list for the text
     *
     * @param selector Sets statelistdrawable for the text.
     */
    public void setTextSelector(ColorStateList selector) {
        textSelector = selector;
        textColor = Integer.MIN_VALUE;
    }

    //Focussed color is the base color of the current theme.
    private void initSelectionColors() {
        enabledColor = ContextCompat.getColor(context, R.color.uikit_tab_text_enabled_color);
        //Selected Color
        TypedArray array = context.obtainStyledAttributes(R.styleable.PhilipsUIKit);
        selectedColor = array.getColor(R.styleable.PhilipsUIKit_uikit_baseColor, 0);
        array.recycle();
    }

    private void initIconColorFilters() {
        enabledFilter = new PorterDuffColorFilter(enabledColor, PorterDuff.Mode.SRC_ATOP);
        selectedFilter = new PorterDuffColorFilter(selectedColor, PorterDuff.Mode.SRC_ATOP);
    }

    private Drawable getTabIconSelector(Drawable drawable) {
        Drawable enabled = drawable.getConstantState().newDrawable().mutate();
        Drawable selected = drawable.getConstantState().newDrawable().mutate();

        ColorFilterStateListDrawable selector = new ColorFilterStateListDrawable();
        selector.addState(new int[]{android.R.attr.state_selected}, selected, selectedFilter);
        selector.addState(new int[]{}, enabled, enabledFilter);

        return selector;
    }
}