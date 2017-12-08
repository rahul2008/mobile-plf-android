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
        } else if (colorRange.equalsIgnoreCase(ColorRange.BLUE.name())) {
            validArray = BLUE;
        } else if (colorRange.equalsIgnoreCase(ColorRange.AQUA.name())) {
            validArray = AQUA;
        } else if (colorRange.equalsIgnoreCase(ColorRange.GREEN.name())) {
            validArray = GREEN;
        } else if (colorRange.equalsIgnoreCase(ColorRange.ORANGE.name())) {
            validArray = ORANGE;
        } else if (colorRange.equalsIgnoreCase(ColorRange.PINK.name())) {
            validArray = PINK;
        } else if (colorRange.equalsIgnoreCase(ColorRange.PURPLE.name())) {
            validArray = PURPLE;
        } else if (colorRange.equalsIgnoreCase(ColorRange.GRAY.name())) {
            validArray = GRAY;
        }
        boolean validAccent = arrayContainsElement(validArray, resultAccent);
        if(!validAccent && validArray != null) {
            resultAccent = validArray[0];
        }
        return resultAccent;
    }

    private static <T, U> boolean arrayContainsElement(T[] origObject, U searchObject) {
        if (origObject == null) {
            return false;
        }
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
