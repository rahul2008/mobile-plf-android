package com.philips.platform.uid.thememanager;


class AccentValidator {

    private final static String GROUP_BLUE_COLOR_RANGE = "GROUPBLUE";

    private final static String[] GROUP_BLUE = {AccentRange.AQUA.name(), AccentRange.GREEN.name(),
            AccentRange.ORANGE.name(), AccentRange.PINK.name(), AccentRange.PURPLE.name()};

    private final static String[] BLUE = {AccentRange.AQUA.name(), AccentRange.GREEN.name(),
            AccentRange.ORANGE.name(), AccentRange.PINK.name(), AccentRange.PURPLE.name()};

    private final static String[] AQUA = {AccentRange.BLUE.name(), AccentRange.ORANGE.name(),
            AccentRange.PINK.name(), AccentRange.PURPLE.name()};

    private final static String[] GREEN = {AccentRange.ORANGE.name(), AccentRange.PINK.name(), AccentRange.PURPLE.name()};

    private final static String[] ORANGE = {AccentRange.BLUE.name(), AccentRange.AQUA.name(), AccentRange.PURPLE.name()};

    private final static String[] PINK = {AccentRange.ORANGE.name(), AccentRange.PURPLE.name()};

    private final static String[] PURPLE = {AccentRange.BLUE.name(), AccentRange.AQUA.name(),
            AccentRange.GREEN.name(), AccentRange.ORANGE.name(), AccentRange.PINK.name(), AccentRange.PURPLE.name()};

    private final static String[] GRAY = {AccentRange.GROUP_BLUE.name(), AccentRange.BLUE.name(),
            AccentRange.AQUA.name(), AccentRange.GREEN.name(), AccentRange.ORANGE.name(),
            AccentRange.PINK.name(), AccentRange.PURPLE.name()};

    static boolean isValidAccent(String colorRange, String accentRange) {
        String[] validArray = null;
        if (colorRange.equals(GROUP_BLUE_COLOR_RANGE)) {
            validArray = GROUP_BLUE;
        } else if (colorRange.equals(ColorRange.BLUE.name())) {
            validArray = BLUE;
        } else if (colorRange.equals(ColorRange.AQUA.name())) {
            validArray = AQUA;
        } else if (colorRange.equals(ColorRange.GREEN.name())) {
            validArray = GREEN;
        } else if (colorRange.equals(ColorRange.ORANGE.name())) {
            validArray = ORANGE;
        } else if (colorRange.equals(ColorRange.PINK.name())) {
            validArray = PINK;
        } else if (colorRange.equals(ColorRange.PURPLE.name())) {
            validArray = PURPLE;
        } else if (colorRange.equals(ColorRange.GRAY.name())) {
            validArray = GRAY;
        }
        return arrayContainsElement(validArray, accentRange);
    }

    private static boolean arrayContainsElement(String[] array, String searchStr) {
        boolean found = false;
        for (String str : array) {
            if (str.equals(searchStr)) {
                found = true;
                break;
            }
        }
        return found;
    }
}
