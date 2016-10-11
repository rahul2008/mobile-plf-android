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

    public int[] getTonalColors(final Resources resources, final String colorRangeResourceName, final String packageName) {
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

    List<ColorModel> getTonalRangeItemsList(final String changedColorRange, final Context context) {
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
        navigationColorModelList.add(new ColorModel("UL", navigationColors[0]));
        navigationColorModelList.add(new ColorModel("VL", navigationColors[1]));
        navigationColorModelList.add(new ColorModel("L", navigationColors[2]));
        navigationColorModelList.add(new ColorModel("B", navigationColors[3]));
        navigationColorModelList.add(new ColorModel("VD", navigationColors[4]));
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

    public List<ColorModel> getDimColors(final String packageName, final Resources resources, final String colorResourcePlaceHolder) {

        final List<ColorModel> dimColorsList = new ArrayList<>();

        final int[] color = getDimColorArray(resources, packageName, colorResourcePlaceHolder);
        dimColorsList.add(new ColorModel("90", color[0]));
        dimColorsList.add(new ColorModel("85", color[1]));
        return dimColorsList;
    }

    private int[] getDimColorArray(final Resources resources, final String packageName, final String colorResourcePlaceHolder) {
        return new int[]{
                getColorResourceId(resources, colorResourcePlaceHolder, "90", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "85", packageName),
        };
    }

    public List<ColorModel> getPrimaryColors(final Resources resources, final String colorResourcePlaceHolder, final String packageName) {
        final List<ColorModel> primaryColors = new ArrayList<>();
        final int[] color = getPrimaryControlColors(resources, colorResourcePlaceHolder, packageName);
        primaryColors.add(new ColorModel("UL", color[0]));
        primaryColors.add(new ColorModel("VL", color[1]));
        primaryColors.add(new ColorModel("L", color[2]));
        primaryColors.add(new ColorModel("B", color[3]));
        primaryColors.add(new ColorModel("VD", color[4]));
        return primaryColors;
    }

    private int[] getPrimaryControlColors(final Resources resources, final String colorResourcePlaceHolder, final String packageName) {
        return new int[]{
                getColorResourceId(resources, colorResourcePlaceHolder, "45", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "45", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "75", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "75", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "45", packageName),
        };
    }

    public List<ColorModel> getAccentColorsList(final Context context, final String colorRange) {
        return getAccentColorRangeItemsList();
    }

    List<ColorModel> getAccentColorRangeItemsList() {
        final List<ColorModel> colorRangeModelsList = new ArrayList<>();
        int[] color = getColorRangeArray();
        colorRangeModelsList.add(new ColorModel("GB", color[0]));
        colorRangeModelsList.add(new ColorModel("Bl", color[1]));
        colorRangeModelsList.add(new ColorModel("Aq", color[2]));
        colorRangeModelsList.add(new ColorModel("Gr", color[3]));
        colorRangeModelsList.add(new ColorModel("Or", color[4]));
        colorRangeModelsList.add(new ColorModel("Pi", color[5]));
        colorRangeModelsList.add(new ColorModel("Pu", color[6]));
        colorRangeModelsList.add(new ColorModel("Gr", color[7]));

        return colorRangeModelsList;
    }
}

