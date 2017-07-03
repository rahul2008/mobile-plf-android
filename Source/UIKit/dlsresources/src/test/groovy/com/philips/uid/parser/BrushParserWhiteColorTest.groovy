package com.philips.uid.parser

class BrushParserWhiteColorTest extends GroovyTestCase {

    BrushParser brushParser

    @Override
    protected void setUp() throws Exception {
       brushParser = TestParserConstants.setUp(readBrushesWhiteBrushJSON())
    }

    void testContainsOneBrush() {
        assertEquals(1, brushParser.brushes.size())
    }

    void testOutputIsOnlyWhite() {
        assertEquals("@color/uid_level_white", TestParserConstants.getColorValue(TestParserConstants.GROUP_BLUE, TestParserConstants.ULTRA_LIGHT))
    }

    void testBlueOutputIsWhiteWithOffset10() {
        assertEquals("@color/uid_blue_level_10", TestParserConstants.getColorValue(TestParserConstants.BLUE, TestParserConstants.VERY_LIGHT))
    }

    void testGreenOutputIsWhiteWithOffset10() {
        assertEquals("@color/uid_green_level_10", TestParserConstants.getColorValue(TestParserConstants.GREEN, TestParserConstants.VERY_LIGHT))
    }

    void testOutputIsWhiteWithOpacity35() {
        assertEquals("#59FFFFFF", TestParserConstants.getColorValue(TestParserConstants.GREEN, TestParserConstants.LIGHT))
    }

    void testOutputIsWhiteWithOffsetAndOpacity35InBlueBright() {
        assertEquals("#59DEEAFF", TestParserConstants.getColorValue(TestParserConstants.BLUE, TestParserConstants.BRIGHT))
    }

    void testOutputIsWhiteWithOffsetAndOpacity35InGreenBright() {
        assertEquals("#59ECF2AC", TestParserConstants.getColorValue(TestParserConstants.GREEN, TestParserConstants.BRIGHT))
    }

    def readBrushesWhiteBrushJSON() {
        new File("src/test/resources/brush_white.json").text
    }
}