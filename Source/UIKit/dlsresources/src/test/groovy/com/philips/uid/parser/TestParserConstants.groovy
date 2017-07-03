package com.philips.uid.parser

import static com.philips.uid.helpers.BrushValueEvaluator.getValue

@groovy.lang.Singleton
class TestParserConstants {
    static def final GROUP_BLUE = "group-blue"
    static def final BLUE = "blue"
    static def final GREEN = "green"
    static def final ORANGE = "orange"
    static def final PINK = "pink"


    static def final RANDOM_RANGE = "random"


    static def final ULTRA_LIGHT = "ultra-light"
    static def final VERY_LIGHT = "very-light"
    static def final LIGHT = "light"
    static def final BRIGHT = "bright"

    private static BrushParser brushParser

    private static ColorParser colorParser

    static def setUp(jsonData) throws Exception {
        //Generate colors for testing
        if(!colorParser) {
            new ColorParser()
        }
        brushParser = new BrushParser(jsonData)
    }

    static def getColorValue(colorRange, tonalRange) {
        def brushValue = brushParser.brushes.get(0).brushValueMap.get(tonalRange)
        if(brushValue) {
            return getValue(brushValue, brushParser.brushes, colorRange, tonalRange)
        }
    }
}