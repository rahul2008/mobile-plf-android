/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.themesettings;

import android.content.Context;
import android.content.res.Resources;

import com.philips.platform.catalogapp.R;
import com.philips.platform.uit.thememanager.ColorRange;

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

    public int[] getContentColorsArray(final Resources resources, final String colorRangeResourceName, final String packageName) {
        return new int[]{
                getColorResourceId(resources, colorRangeResourceName, "75", packageName),
                getColorResourceId(resources, colorRangeResourceName, "50", packageName),
                getColorResourceId(resources, colorRangeResourceName, "35", packageName),
                getColorResourceId(resources, colorRangeResourceName, "20", packageName),
                R.color.uitColorWhite
        };
    }

    public int getColorResourceId(final Resources resources, final String basecolor, final String level, final String packageName) {
        return resources.getIdentifier(String.format(Locale.getDefault(), "uit_%s_level_%s", basecolor, level), "color", packageName);
    }

    public List<ColorModel> getContentColorModelList(final ColorRange colorRange, final Context context) {
        final List<ColorModel> tonalRangelist = new ArrayList<>();
        final String color = colorRange.name().toLowerCase();
        final int colorResourceId75 = getColorResourceId(context.getResources(), color, "75", context.getPackageName());
        tonalRangelist.add(new ColorModel("VD", color, R.color.uitColorWhite, R.color.uitColorWhite, 75, 70));
        tonalRangelist.add(new ColorModel("B", color, R.color.uitColorWhite, R.color.uitColorWhite, 45, 40));
        tonalRangelist.add(new ColorModel("L", color, R.color.uitColorWhite, R.color.uitColorWhite, 30, 25));
        tonalRangelist.add(new ColorModel("VL", color, colorResourceId75, colorResourceId75, 15, 10));
        tonalRangelist.add(new ColorModel("UL", color, colorResourceId75, colorResourceId75, 05, 0));
        return tonalRangelist;
    }

    public List<ColorModel> getColorRangeItemsList() {
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

    public List<ColorModel> getNavigationColorModelsList(final ColorRange colorRange, final Context context) {
        final List<ColorModel> navigationColorModelList = new ArrayList<>();

        final String color = colorRange.name().toLowerCase();
        final int colorResourceId75 = getColorResourceId(context.getResources(), color, "75", context.getPackageName());

        final int[] navigationColors = getNavigationColorsArray(color, context.getPackageName(), context.getResources());
        navigationColorModelList.add(new ColorModel("VD", color, navigationColors[0], R.color.uitColorWhite));
        navigationColorModelList.add(new ColorModel("B", color, navigationColors[1], R.color.uitColorWhite));
        navigationColorModelList.add(new ColorModel("L", color, navigationColors[2], R.color.uitColorWhite));
        navigationColorModelList.add(new ColorModel("VL", color, navigationColors[3], navigationColors[3]));
        navigationColorModelList.add(new ColorModel("UL", color, navigationColors[4], colorResourceId75));
        return navigationColorModelList;
    }

    private int[] getNavigationColorsArray(final String colorResourcePlaceHolder, final String packageName, final Resources resources) {
        return new int[]{
                getColorResourceId(resources, colorResourcePlaceHolder, "65", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "40", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "20", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "05", packageName),
                R.color.uitColorWhite
        };
    }

    public List<ColorModel> getAccentColorsList(final Context context, final ColorRange colorRange) {
        return getAccentColorRangeItemsList(colorRange);
    }

    List<ColorModel> getAccentColorRangeItemsList(final ColorRange colorRange) {
        final List<ColorModel> colorRangeModelsList = new ArrayList<>();
        int[] colorsArray = getAccentColorRangeArray();
        final String colorName = colorRange.name().toLowerCase();
        colorRangeModelsList.add(new ColorModel("GB", colorName, colorsArray[0], colorsArray[0]));
        colorRangeModelsList.add(new ColorModel("Bl", colorName, colorsArray[1], colorsArray[1]));
        colorRangeModelsList.add(new ColorModel("Aq", colorName, colorsArray[2], colorsArray[2]));
        colorRangeModelsList.add(new ColorModel("Gr", colorName, colorsArray[3], colorsArray[3]));
        colorRangeModelsList.add(new ColorModel("Or", colorName, colorsArray[4], colorsArray[4]));
        colorRangeModelsList.add(new ColorModel("Pi", colorName, colorsArray[5], colorsArray[4]));
        colorRangeModelsList.add(new ColorModel("Pu", colorName, colorsArray[6], colorsArray[6]));
        colorRangeModelsList.add(new ColorModel("Gr", colorName, colorsArray[7], colorsArray[7]));

        return colorRangeModelsList;
    }

    private int[] getAccentColorRangeArray() {
        return getColorRangeArray();
    }
}

