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

    enum ColorName {
        GB("group_blue"),
        Bl("blue"),
        Aq("aqua"),
        GR("green"),
        Or("orange"),
        Pi("pink"),
        Pu("purple"),
        Gr("gray");

        public String color;

        ColorName(final String color) {
            this.color = color;
        }

        public static ColorName getRangeByValue(final String title) {
            for (ColorName colorName : ColorName.values()) {
                if (colorName.name().equals(title))
                    return colorName;
            }
            return null;
        }
    }

    public int getColorResourceId(final Resources resources, final String basecolor, final String level, final String packageName) {
        return resources.getIdentifier(String.format(Locale.getDefault(), "uid_%s_level_%s", basecolor, level), "color", packageName);
    }

    public List<ColorModel> getContentColorModelList(final ColorRange colorRange, final Context context) {
        final List<ColorModel> tonalRangelist = new ArrayList<>();
        final String color = colorRange.name().toLowerCase();
        final int colorResourceId75 = getColorResourceId(context.getResources(), color, "75", context.getPackageName());
        tonalRangelist.add(new ColorModel("VD", color, getUidColorWhite(), 75, 70));
        tonalRangelist.add(new ColorModel("B", color, getUidColorWhite(), 45, 40));
        tonalRangelist.add(new ColorModel("L", color, getUidColorWhite(), 30, 25));
        tonalRangelist.add(new ColorModel("VL", color, colorResourceId75, 15, 10));
        tonalRangelist.add(new ColorModel("UL", color, colorResourceId75, 5, 0));
        return tonalRangelist;
    }

    public List<ColorModel> getColorRangeItemsList() {
        final List<ColorModel> colorRangeModelsList = new ArrayList<>();
        for (ColorName colorName : ColorName.values()) {
            colorRangeModelsList.add(new ColorModel(colorName.name(), colorName.color, getUidColorWhite(), 50, 35));
        }
        return colorRangeModelsList;
    }

    private int getUidColorWhite() {
        return R.color.uidColorWhite;
    }

    public List<ColorModel> getNavigationColorModelsList(final ColorRange colorRange, final Context context) {
        final List<ColorModel> navigationColorModelList = new ArrayList<>();

        final String color = colorRange.name().toLowerCase();
        final int colorResourceId75 = getColorResourceId(context.getResources(), color, "75", context.getPackageName());

        final int[] navigationColors = getNavigationColorsArray(color, context.getPackageName(), context.getResources());
        navigationColorModelList.add(new ColorModel("VD", color, navigationColors[0], getUidColorWhite()));
        navigationColorModelList.add(new ColorModel("B", color, navigationColors[1], getUidColorWhite()));
        navigationColorModelList.add(new ColorModel("L", color, navigationColors[2], getUidColorWhite()));
        navigationColorModelList.add(new ColorModel("VL", color, navigationColors[3], colorResourceId75));
        navigationColorModelList.add(new ColorModel("UL", color, navigationColors[4], colorResourceId75));
        return navigationColorModelList;
    }

    private int[] getNavigationColorsArray(final String colorName, final String packageName, final Resources resources) {
        return new int[]{
                getColorResourceId(resources, colorName, "65", packageName),
                getColorResourceId(resources, colorName, "40", packageName),
                getColorResourceId(resources, colorName, "20", packageName),
                getColorResourceId(resources, colorName, "5", packageName),
                getUidColorWhite()
        };
    }

    public List<ColorModel> getAccentColorsList(final ColorRange colorRange, final Resources resources, final String packageName) {
        final List<ColorModel> colorRangeModelsList = new ArrayList<>();
        final String colorName = colorRange.name().toLowerCase();
        for (ColorRange name : ColorRange.values()) {
            if (name != colorRange) {
                final String shortName = getShortName(name.name());
                int id = getColorResourceId(resources, name.name().toLowerCase(), "45", packageName);
                colorRangeModelsList.add(new ColorModel(shortName, colorName, id, getUidColorWhite()));
            }
        }

        return colorRangeModelsList;
    }

    private String getShortName(final String name) {
        final String[] split = name.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        if (split.length > 1) {
            stringBuilder.append(split[0].substring(0, 1)).append(split[1].substring(0, 1));
        } else {
            stringBuilder.append(name.substring(0, 1)).append(name.substring(1, 2).toLowerCase());
        }
        return stringBuilder.toString();
    }
}

