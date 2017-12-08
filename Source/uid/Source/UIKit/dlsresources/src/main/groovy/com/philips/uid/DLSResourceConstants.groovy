/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.uid

import com.philips.uid.helpers.NameConversionHelper

class DLSResourceConstants {
    //Theme specific constants
    static final String LIB_PREFIX = "uid"
    static final String HIPHEN = "-"
    static final String UNDERSCORE = "_"
    static final String THEME_PREFIX = "Theme.DLS"
    static final String THEME_DECLARED_ID = "PhilipsUID"
    static final String THEME_DECLARED_STYLEABLE = "declare-styleable"
    static final String FORMAT_REF_OR_COLOR = "reference|color"
    static final String ITEM_NAME = "name"
    static final String ITEM_FORMAT = "format"
    static final String LEVEL = "level"
    static final String ATTR = "?attr/"
    static final String COLOR_REFERENCE = "@color/"
    static final int COLOR_OFFSET = 5
    static final COLOR_RANGES = [GroupBlue: 'group-blue', Blue: 'blue', Aqua: 'aqua',
                                 Green    : 'green', Orange: 'orange', Pink: 'pink', Purple: 'purple', Gray: 'gray']

    static final TONAL_RANGES = ['ultra-light', 'very-light'/*, 'light'*/, 'bright', 'very-dark']

    //use this for debugging in Intellij
    //static final String PATH_RES = "../../../../resources"

    //Path constants
    static final String PATH_RES = "src/main/resources"
    static final String PATH_SEMANTIC_BRUSH_JSON = "${PATH_RES}/brushes.json"
    static final String PATH_COMPONENT_JSON = "${PATH_RES}/components.json"
    static final String PATH_COLOR_RANGES_JSON = "${PATH_RES}/color_ranges.json"
    static final String PATH_VALIDATION_JSON = "${PATH_RES}/validation.json"

    static final String PATH_OUT = "generated"
    static final String PATH_OUT_THEME_FILE = "${PATH_OUT}/uid_theme_%s.xml"
    static final String PATH_OUT_THEME_WHITE_FILE = "${PATH_OUT}/uid_theme_white.xml"
    static final String PATH_OUT_ACCENT_FILE = "${PATH_OUT}/uid_accent_%s.xml"
    static final String PATH_OUT_COLORS_FILE = "${PATH_OUT}/uid_colors.xml"
    static final String PATH_OUT_NAVIGATION_FILE = "${PATH_OUT}/uid_navigation.xml"
    static final String PATH_OUT_NAVIGATION__TOP_FILE = "${PATH_OUT}/uid_navigation_top.xml"
//    static final String PATH_OUT_ACCENT_FILE = "${PATH_OUT}/uid_accent_themes.xml"
    static final String PATH_OUT_ATTRS_FILE = "${PATH_OUT}/uid_attrs.xml"
    public static final String HOVER = "Hover"

    static def getThemeFilePath(fileName) {
        String.format(PATH_OUT_THEME_FILE, NameConversionHelper.replaceHyphenWithUnderScores("${fileName}"))
    }

    static def getWhiteThemeFilePath() {
        PATH_OUT_THEME_WHITE_FILE
    }

    static def getNavigationTopFilePath() {
        PATH_OUT_NAVIGATION__TOP_FILE
    }

    static def getAccentFilePath(fileName) {
        String.format(PATH_OUT_ACCENT_FILE, NameConversionHelper.replaceHyphenWithUnderScores("${fileName}"))
    }
}