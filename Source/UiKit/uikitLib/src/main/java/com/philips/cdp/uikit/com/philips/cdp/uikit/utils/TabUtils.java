package com.philips.cdp.uikit.com.philips.cdp.uikit.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TabUtils {

    private int selectedColor;
    private Context context;
    private TabLayout tabLayout;

    public static class TabIconConfig {
        int drawableResID = -1;
        Drawable srcDrawable;
        boolean useThemeSelector = false;

        TabIconConfig(int drawableResID, boolean useThemeSelector) {
            this.drawableResID = drawableResID;
            this.useThemeSelector = useThemeSelector;
        }

        TabIconConfig(Drawable srcDrawable, boolean useThemeSelector) {
            this.srcDrawable = srcDrawable;
            this.useThemeSelector = useThemeSelector;
        }
    }

    public TabUtils(Context context, TabLayout tabLayout) {
        this.context = context;
        this.tabLayout = tabLayout;
        selectedColor = getSelectedColor();
    }

    public TabLayout.Tab newTab(int titleResID, int imageDrawable, final int badgeCount) {
        TabLayout.Tab newTab = tabLayout.newTab();
        View customView = null;
        if (imageDrawable > 0) {
            customView = LayoutInflater.from(context).inflate(R.layout.tab_with_image, null);
            newTab.setCustomView(customView);
            //Set icon for the tab
            ImageView iconView = (ImageView) customView.findViewById(R.id.tab_icon);
            iconView.setImageDrawable(getTabIconSelector(selectedColor, imageDrawable));
        } else {
            customView = LayoutInflater.from(context).inflate(R.layout.tab_textonly, null);
        }

        //Set title text
        TextView title = (TextView) customView.findViewById(R.id.tab_title);
        if (titleResID <= 0) {
            title.setVisibility(View.GONE);
        } else {
            title.setText(titleResID);
            title.setTextColor(getTextSelector());
        }
        return newTab;
    }

    private ColorStateList getTextSelector() {
        int[][] states = {{android.R.attr.state_selected}, {}};
        int[] colors = {selectedColor, Color.WHITE};
        return new ColorStateList(states, colors);
    }

    //Focussed color is the base color of the current theme.
    private int getSelectedColor() {
        TypedArray array = context.obtainStyledAttributes(R.styleable.PhilipsUIKit);
        int color = array.getColor(R.styleable.PhilipsUIKit_baseColor, 0);
        array.recycle();
        return color;
    }

    private Drawable getTabIconSelector(int color, int resID) {
        Drawable enabled = ResourcesCompat.getDrawable(context.getResources(), resID, null);
        //We need to mutate if we want as a differnet drawable
        Drawable selected = ResourcesCompat.getDrawable(context.getResources(), resID, null).mutate();
        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        ColorFilterStateListDrawable selector = new ColorFilterStateListDrawable();

        selector.addState(new int[]{android.R.attr.state_selected}, selected, filter);
        selector.addState(new int[]{}, enabled);

        return selector;
    }
}