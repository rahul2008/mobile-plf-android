package com.philips.uid

class DLSResourceConstants {
    //Theme specific constants
    static final LIB_PREFIX = "uid"
    static final THEME_PREFIX = "Theme.DLS"
    static final THEME_DECLARED_ID = "PhilipsUID"
    static final THEME_DECLARED_STYLEABLE = "declare-styleable"
    static final FORMAT_REF_OR_COLOR = "reference|color"
    static final ITEM_NAME = "name"
    static final ITEM_FORMAT = "format"

    static final COLOR_RANGES = [GroupBlue: 'group_blue', Blue: 'blue', Aqua: 'aqua',
                         Green    : 'green', Orange: 'orange', Pink: 'pink', Purple: 'purple', Gray: 'gray']

    static final TONAL_RANGES = ['UltraLight', 'VeryLight', 'Light', 'Bright', 'VeryDark']

    //Path constants
    static final String PROJECT_BASE_PATH = "src/main/groovy/com/philips"
    static final String PATH_RES = "${PROJECT_BASE_PATH}/res"
    static final String PATH_SEMANTIC_BRUSH_JSON = "${PATH_RES}/semantic_brushes_generated.json"
    static final String PATH_COLOR_RANGES_JSON = "${PATH_RES}/color_ranges.json"

    static final String PATH_OUT = "${PROJECT_BASE_PATH}/out"
    static final String PATH_OUT_THEME_FILE = "${PATH_OUT}/uid_theme_%s.xml"
    static final String PATH_OUT_COLORS_FILE = "${PATH_OUT}/uid_colors.xml"
    static final String PATH_OUT_ATTRS_FILE = "${PATH_OUT}/uid_attrs.xml"

    static def getThemeFilePath(fileName) {
        String.format(PATH_OUT_THEME_FILE, "${fileName}")
    }

    //JSON key constants
    static final JSON_KEY_COLOR_NUMBER = "colorNumber"
    static final JSON_KEY_ALPHA = "alpha"
    static final JSON_KEY_REFERENCE = "reference"
    static final JSON_KEY_RANGE_NAME = "rangeName"
}