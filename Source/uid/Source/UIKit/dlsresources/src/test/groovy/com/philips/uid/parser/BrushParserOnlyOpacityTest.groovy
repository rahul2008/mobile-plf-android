package com.philips.uid.parser

class BrushParserOnlyOpacityTest extends GroovyTestCase {

    BrushParser brushParser

    @Override
    protected void setUp() throws Exception {
       brushParser = TestParserConstants.setUp(readBrushesOpacityBrushJSON())
    }

    void testContainsOneBrush() {
        assertEquals(1, brushParser.brushes.size())
    }

    void testOutputIsOnlyOpacity() {
        assertEquals("0.3", TestParserConstants.getColorValue(TestParserConstants.GROUP_BLUE, TestParserConstants.ULTRA_LIGHT))
    }

    def readBrushesOpacityBrushJSON() {
        new File("src/test/resources/brush_only_opacity.json").text
    }
}