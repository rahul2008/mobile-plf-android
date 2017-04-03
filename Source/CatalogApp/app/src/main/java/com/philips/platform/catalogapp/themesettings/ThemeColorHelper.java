/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.themesettings;

import android.content.Context;
import android.content.res.Resources;

import com.philips.platform.catalogapp.R;
import com.philips.platform.uid.thememanager.ColorRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ThemeColorHelper {
    public int[] getColorRangeArray() {
        return new int[]{
                R.color.uidColorWhite,
                R.color.uidColorWhite,
                R.color.uidColorWhite,
                R.color.uidColorWhite,
                R.color.uidColorWhite,
                R.color.uidColorWhite,
                R.color.uidColorWhite,
                R.color.uidColorWhite};
    }

    public int[] getContentColorsArray(final Resources resources, final String colorRangeResourceName, final String packageName) {
        return new int[]{
                getColorResourceId(resources, colorRangeResourceName, "75", packageName),
                getColorResourceId(resources, colorRangeResourceName, "50", packageName),
                getColorResourceId(resources, colorRangeResourceName, "35", packageName),
                getColorResourceId(resources, colorRangeResourceName, "20", packageName),
                R.color.uidColorWhite
        };
    }

    public int getColorResourceId(final Resources resources, final String basecolor, final String level, final String packageName) {
        return resources.getIdentifier(String.format(Locale.getDefault(), "uid_%s_level_%s", basecolor, level), "color", packageName);
    }

    public List<ColorModel> getContentColorModelList(final ColorRange colorRange, final Context context) {
        final List<ColorModel> tonalRangelist = new ArrayList<>();
        final String color = colorRange.name().toLowerCase();
        final int colorResourceId75 = getColorResourceId(context.getResources(), color, "75", context.getPackageName());
        tonalRangelist.add(new ColorModel("VD", color, R.color.uidColorWhite, 75, 70));
        tonalRangelist.add(new ColorModel("B", color, R.color.uidColorWhite, 45, 40));
        tonalRangelist.add(new ColorModel("L", color, R.color.uidColorWhite, 30, 25));
        tonalRangelist.add(new ColorModel("VL", color, colorResourceId75, 15, 10));
        tonalRangelist.add(new ColorModel("UL", color, colorResourceId75, 5, 0));
        return tonalRangelist;
    }

    public List<ColorModel> getColorRangeItemsList() {
        final List<ColorModel> colorRangeModelsList = new ArrayList<>();
        int[] color = getColorRangeArray();
        colorRangeModelsList.add(new ColorModel("GB", "group_blue", color[0], 50, 35));
        colorRangeModelsList.add(new ColorModel("Bl", "blue", color[1], 50, 35));
        colorRangeModelsList.add(new ColorModel("Aq", "aqua", color[2], 50, 35));
        colorRangeModelsList.add(new ColorModel("Gr", "green", color[3], 50, 35));
        colorRangeModelsList.add(new ColorModel("Or", "orange", color[4], 50, 35));
        colorRangeModelsList.add(new ColorModel("Pi", "pink", color[5], 50, 35));
        colorRangeModelsList.add(new ColorModel("Pu", "purple", color[6], 50, 35));
        colorRangeModelsList.add(new ColorModel("Gr", "gray", color[7], 50, 35));

        return colorRangeModelsList;
    }

    public List<ColorModel> getNavigationColorModelsList(final ColorRange colorRange, final Context context) {
        final List<ColorModel> navigationColorModelList = new ArrayList<>();

        final String color = colorRange.name().toLowerCase();
        final int colorResourceId75 = getColorResourceId(context.getResources(), color, "75", context.getPackageName());

        final int[] navigationColors = getNavigationColorsArray(color, context.getPackageName(), context.getResources());
        navigationColorModelList.add(new ColorModel("VD", color, navigationColors[0], R.color.uidColorWhite));
        navigationColorModelList.add(new ColorModel("B", color, navigationColors[1], R.color.uidColorWhite));
        navigationColorModelList.add(new ColorModel("L", color, navigationColors[2], R.color.uidColorWhite));
        navigationColorModelList.add(new ColorModel("VL", color, navigationColors[3], colorResourceId75));
        navigationColorModelList.add(new ColorModel("UL", color, navigationColors[4], colorResourceId75));
        return navigationColorModelList;
    }

    private int[] getNavigationColorsArray(final String colorResourcePlaceHolder, final String packageName, final Resources resources) {
        return new int[]{
                getColorResourceId(resources, colorResourcePlaceHolder, "65", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "40", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "20", packageName),
                getColorResourceId(resources, colorResourcePlaceHolder, "5", packageName),
                R.color.uidColorWhite
        };
    }

    public List<ColorModel> getAccentColorsList(final Context context, final ColorRange colorRange) {
        return getAccentColorRangeItemsList(colorRange);
    }

    List<ColorModel> getAccentColorRangeItemsList(final ColorRange colorRange) {
        final List<ColorModel> colorRangeModelsList = new ArrayList<>();
        int[] colorsArray = getAccentColorRangeArray();
        final String colorName = colorRange.name().toLowerCase();
        colorRangeModelsList.add(new ColorModel("GB", colorName, colorsArray[0], R.color.uidColorWhite));
        colorRangeModelsList.add(new ColorModel("Bl", colorName, colorsArray[1], R.color.uidColorWhite));
        colorRangeModelsList.add(new ColorModel("Aq", colorName, colorsArray[2], R.color.uidColorWhite));
        colorRangeModelsList.add(new ColorModel("Gr", colorName, colorsArray[3], R.color.uidColorWhite));
        colorRangeModelsList.add(new ColorModel("Or", colorName, colorsArray[4], R.color.uidColorWhite));
        colorRangeModelsList.add(new ColorModel("Pi", colorName, colorsArray[5], R.color.uidColorWhite));
        colorRangeModelsList.add(new ColorModel("Pu", colorName, colorsArray[6], R.color.uidColorWhite));
        colorRangeModelsList.add(new ColorModel("Gr", colorName, colorsArray[7], R.color.uidColorWhite));

        return colorRangeModelsList;
    }

    private int[] getAccentColorRangeArray() {
        return new int[]{R.color.uid_group_blue_level_45,
                R.color.uid_blue_level_45,
                R.color.uid_aqua_level_45,
                R.color.uid_green_level_45,
                R.color.uid_orange_level_45,
                R.color.uid_pink_level_45,
                R.color.uid_purple_level_45,
                R.color.uid_gray_level_45};
    }
}

