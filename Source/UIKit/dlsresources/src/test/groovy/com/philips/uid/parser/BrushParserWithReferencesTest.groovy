package com.philips.uid.parser

class BrushParserWithReferencesTest extends GroovyTestCase {

    BrushParser brushParser

    @Override
    protected void setUp() throws Exception {
        brushParser = TestParserConstants.setUp(readBrushesReferencesBrushJSON())
    }

    void testColorCodeOnlyInGBUL() {
        assertEquals("@color/uid_group_blue_level_65", TestParserConstants.getColorValue(TestParserConstants.GROUP_BLUE, TestParserConstants.ULTRA_LIGHT))
    }

    void testColorCodeOnlyInOrangeUL() {
        assertEquals("@color/uid_orange_level_65", TestParserConstants.getColorValue(TestParserConstants.ORANGE, TestParserConstants.ULTRA_LIGHT))
    }

    void testColorCodeWithOpacityInOrangeVL() {
        assertEquals("#99DE7510", TestParserConstants.getColorValue(TestParserConstants.ORANGE, TestParserConstants.VERY_LIGHT))
    }

    void testColorCodeWithOpacityInPinkVL() {
        assertEquals("#99E04A71", TestParserConstants.getColorValue(TestParserConstants.PINK, TestParserConstants.VERY_LIGHT))
    }

    void testReferenceWithOpacityCallingColorCodeinPink() {
        assertEquals("#99E04A71", TestParserConstants.getColorValue(TestParserConstants.PINK, TestParserConstants.LIGHT))
    }

    void testCallerWithOffsetAndRefWithColorCode() {
        assertEquals("@color/uid_green_level_65",TestParserConstants.getColorValue(TestParserConstants.GREEN, TestParserConstants.BRIGHT))
    }

    def readBrushesReferencesBrushJSON() {
        new File("src/test/resources/brush_references.json").text
    }
}