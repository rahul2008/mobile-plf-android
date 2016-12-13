package com.philips.uid

class DLSResourceConstants {
    static final LIB_PREFIX = "uid"

    static final COLOR_RANGES = [GroupBlue: 'group_blue', Blue: 'blue', Aqua: 'aqua',
                         Green    : 'green', Orange: 'orange', Pink: 'pink', Purple: 'purple', Gray: 'gray']

    static final TONAL_RANGES = ['UltraLight', 'VeryLight', 'Light', 'Bright', 'VeryDark']



    static final String PROJECT_BASE_PATH = "src/main/groovy/com/philips"
    static final String PATH_RES = "${PROJECT_BASE_PATH}/res"
    static final String PATH_SEMANTIC_BRUSH_JSON = "${PATH_RES}/semantic_brushes_generated.json"
    static final String PATH_COLOR_RANGES_JSON = "${PATH_RES}/color_ranges.json"

    static final String PATH_OUT = "${PROJECT_BASE_PATH}/out"
    static final String PATH_OUT_ATTRS_FILE = "${PATH_OUT}/uid_theme_%s.xml"
    static final String PATH_OUT_COLORS_FILE = "${PATH_OUT}/uid_colors.xml"

    static def getThemeFilePath(fileName) {
        String.format(PATH_OUT_ATTRS_FILE, "${fileName}")
    }

    //JSON key constants
    static final JSON_KEY_COLOR_NUMBER = "colorNumber"
    static final JSON_KEY_ALPHA = "alpha"
    static final JSON_KEY_REFERENCE = "reference"
    static final JSON_KEY_RANGE_NAME = "rangeName"
}