/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.themesettings;

import com.philips.platform.uid.thememanager.AccentRange;

import java.util.ArrayList;

public class AccentOptions {
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
