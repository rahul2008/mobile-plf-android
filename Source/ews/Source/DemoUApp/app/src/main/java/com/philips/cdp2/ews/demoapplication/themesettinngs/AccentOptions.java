/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.demoapplication.themesettinngs;

import com.philips.platform.uid.thememanager.AccentRange;

import java.util.ArrayList;

public final class AccentOptions {
    public static ArrayList<AccentRange> getGroupBlueAccentList() {
        ArrayList<AccentRange> list = new ArrayList<>();
        list.add(AccentRange.AQUA);
        list.add(AccentRange.GREEN);
        list.add(AccentRange.ORANGE);
        list.add(AccentRange.PINK);
        list.add(AccentRange.PURPLE);
        return list;
    }

    public static ArrayList<AccentRange> getAquaAccentList() {
        ArrayList<AccentRange> list = new ArrayList<>();
        list.add(AccentRange.BLUE);
        list.add(AccentRange.ORANGE);
        list.add(AccentRange.PINK);
        list.add(AccentRange.PURPLE);
        return list;
    }

    public static ArrayList<AccentRange> getGreenAccentList() {
        ArrayList<AccentRange> list = new ArrayList<>();
        list.add(AccentRange.ORANGE);
        list.add(AccentRange.PINK);
        list.add(AccentRange.PURPLE);
        return list;
    }

    public static ArrayList<AccentRange> getOrangeAccentList() {
        ArrayList<AccentRange> list = new ArrayList<>();
        list.add(AccentRange.BLUE);
        list.add(AccentRange.AQUA);
        list.add(AccentRange.PURPLE);
        return list;
    }

    public static ArrayList<AccentRange> getPinkAccentList() {
        ArrayList<AccentRange> list = new ArrayList<>();
        list.add(AccentRange.ORANGE);
        list.add(AccentRange.PURPLE);
        return list;
    }

    public static ArrayList<AccentRange> getPurpleAccentList() {
        ArrayList<AccentRange> list = new ArrayList<>();
        list.add(AccentRange.BLUE);
        list.add(AccentRange.AQUA);
        list.add(AccentRange.GREEN);
        list.add(AccentRange.ORANGE);
        list.add(AccentRange.PINK);
        return list;
    }

    public static ArrayList<AccentRange> getGrayAccentList() {
        ArrayList<AccentRange> list = new ArrayList<>();
        list.add(AccentRange.GROUP_BLUE);
        list.add(AccentRange.BLUE);
        list.add(AccentRange.AQUA);
        list.add(AccentRange.GREEN);
        list.add(AccentRange.ORANGE);
        list.add(AccentRange.PINK);
        list.add(AccentRange.PURPLE);
        return list;
    }
}
