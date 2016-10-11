/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp;

import android.content.Context;
import android.content.res.Resources;

import com.philips.platform.catalogapp.themesettings.ColorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ThemeColorHelper {
    public int[] getColorRangeArray() {
        return new int[]{
                R.color.uit_group_blue_level_45,
                R.color.uit_blue_level_45,
                R.color.uit_aqua_level_45,
                R.color.uit_green_level_45,
                R.color.uit_orange_level_45,
                R.color.uit_pink_level_45,
                R.color.uit_purple_level_45,
                R.color.uit_gray_level_45};
    }

    public int[] getContentTonalColors(final Resources resources, final String colorRangeResourceName, final String packageName) {
        return new int[]{
                getColorResourceId(resources, colorRangeResourceName, "80", packageName),
                getColorResourceId(resources, colorRangeResourceName, "50", packageName),
                getColorResourceId(resources, colorRangeResourceName, "35", packageName),
                getColorResourceId(resources, colorRangeResourceName, "20", packageName),
                R.color.uitColorWhite
        };
    }

    public int getColorResourceId(final Resources resources, final String basecolor, final String level, final String packageName) {
        return resources.getIdentifier(String.format(Locale.getDefault(), "uit_%s_level_%s", basecolor, level), "color", packageName);
    }

    List<ColorModel> getContentTonalRangeItemsList(final String changedColorRange, final Context context) {
        final List<ColorModel> tonalRangelist = new ArrayList<>();
        final int colorResourceId75 = getColorResourceId(context.getResources(), changedColorRange, "75", context.getPackageName());
        tonalRangelist.add(new ColorModel("VD", changedColorRange, R.color.uitColorWhite, R.color.uitColorWhite, 75, 70));
        tonalRangelist.add(new ColorModel("B", changedColorRange, R.color.uitColorWhite, R.color.uitColorWhite, 45, 40));
        tonalRangelist.add(new ColorModel("L", changedColorRange, R.color.uitColorWhite, R.color.uitColorWhite, 30, 25));
        tonalRangelist.add(new ColorModel("VL", changedColorRange, colorResourceId75, colorResourceId75, 15, 10));
        tonalRangelist.add(new ColorModel("UL", changedColorRange, colorResourceId75, colorResourceId75, 05, 0));
        return tonalRangelist;
    }

    List<ColorModel> getColorRangeItemsList() {
        final List<ColorModel> colorRangeModelsList = new ArrayList<>();
        int[] color = getColorRangeArray();
        colorRangeModelsList.add(new ColorModel("GB", "group_blue", color[0], color[0], 50, 35));
        colorRangeModelsList.add(new ColorModel("Bl", "blue", color[1], color[1], 50, 35));
        colorRangeModelsList.add(new ColorModel("Aq", "aqua", color[2], color[2], 50, 35));
        colorRangeModelsList.add(new ColorModel("Gr", "green", color[3], color[3], 50, 35));
        colorRangeModelsList.add(new ColorModel("Or", "orange", color[4], color[4], 50, 35));
        colorRangeModelsList.add(new ColorModel("Pi", "pink", color[5], color[5], 50, 35));
        colorRangeModelsList.add(new ColorModel("Pu", "purple", color[6], color[6], 50, 35));
        colorRangeModelsList.add(new ColorModel("Gr", "gray", color[7], color[7], 50, 35));

        return colorRangeModelsList;
    }

    public List<ColorModel> getNavigationColorRangeItemsList(final String colorRange, final Context context) {
        final List<ColorModel> navigationColorModelList = new ArrayList<>();

        final int[] navigationColors = getNavigationColors(colorRange, context.getPackageName(), context.getResources());
        navigationColorModelList.add(new ColorModel("VD", colorRange, navigationColors[0], R.color.uitColorWhite));
        navigationColorModelList.add(new ColorModel("B", colorRange, navigationColors[1], R.color.uitColorWhite));
        navigationColorModelList.add(new ColorModel("L", colorRange, navigationColors[2], R.color.uitColorWhite));
        navigationColorModelList.add(new ColorModel("VL", colorRange, navigationColors[3], navigationColors[3]));
        navigationColorModelList.add(new ColorModel("UL", colorRange, navigationColors[4], navigationColors[4]));
        return navigationColorModelList;
    }

    private int[] getNavigationColors(final String colorResourcePlaceHolder, final String packageName, final Resources resources) {
        return new int[]{
                getColorResourceId(resources, colorResourcePlaceHolder, "65", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "40", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "20", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "05", packageName),
                R.color.uitColorWhite
        };
    }

    public List<ColorModel> getAccentColorsList(final Context context, final String colorRange) {
        return getAccentColorRangeItemsList(colorRange);
    }

    List<ColorModel> getAccentColorRangeItemsList(final String colorRange) {
        final List<ColorModel> colorRangeModelsList = new ArrayList<>();
        int[] color = getAccentColorRangeArray();
        colorRangeModelsList.add(new ColorModel("GB", colorRange, color[0], color[0]));
        colorRangeModelsList.add(new ColorModel("Bl", colorRange, color[1], color[1]));
        colorRangeModelsList.add(new ColorModel("Aq", colorRange, color[2], color[2]));
        colorRangeModelsList.add(new ColorModel("Gr", colorRange, color[3], color[3]));
        colorRangeModelsList.add(new ColorModel("Or", colorRange, color[4], color[4]));
        colorRangeModelsList.add(new ColorModel("Pi", colorRange, color[5], color[4]));
        colorRangeModelsList.add(new ColorModel("Pu", colorRange, color[6], color[6]));
        colorRangeModelsList.add(new ColorModel("Gr", colorRange, color[7], color[7]));

        return colorRangeModelsList;
    }

    private int[] getAccentColorRangeArray() {
        return getColorRangeArray();
    }
}

