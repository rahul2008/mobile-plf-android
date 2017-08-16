package com.philips.platform.uid.thememanager;


class AccentValidator {

    private final static String GROUP_BLUE_COLOR_RANGE = "GROUPBLUE";

    private final static AccentRange[] GROUP_BLUE = {AccentRange.AQUA, AccentRange.GREEN,
            AccentRange.ORANGE, AccentRange.PINK, AccentRange.PURPLE};

    private final static AccentRange[] BLUE = {AccentRange.AQUA, AccentRange.GREEN,
            AccentRange.ORANGE, AccentRange.PINK, AccentRange.PURPLE};

    private final static AccentRange[] AQUA = {AccentRange.BLUE, AccentRange.ORANGE,
            AccentRange.PINK, AccentRange.PURPLE};

    private final static AccentRange[] GREEN = {AccentRange.ORANGE, AccentRange.PINK, AccentRange.PURPLE};

    private final static AccentRange[] ORANGE = {AccentRange.BLUE, AccentRange.AQUA, AccentRange.PURPLE};

    private final static AccentRange[] PINK = {AccentRange.ORANGE, AccentRange.PURPLE};

    private final static AccentRange[] PURPLE = {AccentRange.BLUE, AccentRange.AQUA,
            AccentRange.GREEN, AccentRange.ORANGE, AccentRange.PINK, AccentRange.PURPLE};

    private final static AccentRange[] GRAY = {AccentRange.GROUP_BLUE, AccentRange.BLUE,
            AccentRange.AQUA, AccentRange.GREEN, AccentRange.ORANGE,
            AccentRange.PINK, AccentRange.PURPLE};

    static AccentRange fixAccent(String colorRange, AccentRange accentRange) {
        AccentRange[] validArray = null;
        AccentRange resultAccent = accentRange;
        if (colorRange.equals(GROUP_BLUE_COLOR_RANGE)) {
            validArray = GROUP_BLUE;
        } else if (colorRange.equals(ColorRange.BLUE)) {
            validArray = BLUE;
        } else if (colorRange.equals(ColorRange.AQUA)) {
            validArray = AQUA;
        } else if (colorRange.equals(ColorRange.GREEN)) {
            validArray = GREEN;
        } else if (colorRange.equals(ColorRange.ORANGE)) {
            validArray = ORANGE;
        } else if (colorRange.equals(ColorRange.PINK)) {
            validArray = PINK;
        } else if (colorRange.equals(ColorRange.PURPLE)) {
            validArray = PURPLE;
        } else if (colorRange.equals(ColorRange.GRAY)) {
            validArray = GRAY;
        }
        boolean validAccent = arrayContainsElement(validArray, resultAccent);
        if(!validAccent) {
            resultAccent = validArray[0];
        }
        return resultAccent;
    }

    private static <T, U> boolean arrayContainsElement(T[] origObject, U searchObject) {
        boolean found = false;
        for (T object : origObject) {
            if (object.equals(searchObject)) {
                found = true;
                break;
            }
        }
        return found;
    }
}
