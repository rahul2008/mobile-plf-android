package com.philips.cdp.uikit.com.philips.cdp.uikit.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
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

    public static TabLayout.Tab newTab(Context context, TabLayout tabLayout, int titleResID, int imageDrawable, final int badgeCount) {
        TabLayout.Tab newTab = tabLayout.newTab();
        View customView = null;
        if (imageDrawable > 0) {
            customView = LayoutInflater.from(context).inflate(R.layout.tab_with_image, null);
            newTab.setCustomView(customView);
            //Set icon for the tab
            ImageView iconView = (ImageView) customView.findViewById(R.id.tab_icon);
            Drawable image = ResourcesCompat.getDrawable(context.getResources(), imageDrawable, null);
            iconView.setImageDrawable(image);
        } else {
            customView = LayoutInflater.from(context).inflate(R.layout.tab_textonly, null);
        }

        //Set title text
        TextView title = (TextView) customView.findViewById(R.id.tab_title);
        if (titleResID <= 0) {
            title.setVisibility(View.GONE);
        } else {
            title.setText(titleResID);
            title.setTextColor(getTextSelector(context));
        }
        return newTab;
    }

    private static ColorStateList getTextSelector(Context context) {
        int[][] states = {{android.R.attr.state_selected}, {android.R.attr.state_enabled}};
        int[] colors = {getSelectedColor(context),Color.WHITE};
        return new ColorStateList(states, colors);
    }
    //Focussed color is the base color of the current theme.
    private static int getSelectedColor(Context context) {
        TypedArray array = context.obtainStyledAttributes(R.styleable.PhilipsUIKit);
        int color = array.getColor(R.styleable.PhilipsUIKit_baseColor,0);
        array.recycle();
        return color;
    }
}