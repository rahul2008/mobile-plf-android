/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid

import com.google.gson.Gson
import org.junit.Before

class ThemeAttributeTest extends GroovyTestCase {

    public static final String LIGHT = "Light"
    ThemeAttribute themeAttribute
    ThemeValue themeValueObject

    @Before
    void setUp() {
        themeAttribute = new ThemeAttribute("content-primary")
        themeValueObject = new Gson().fromJson("{\"color\": \"white\", \"color-code\": 20, \"color-range\": \"gray\", \"opacity\": 0.10, \"reference\": \"control-primary\", \"offset\": 10}", ThemeValue.class)
    }

    void testConstructor() {
        assertEquals(themeAttribute.attrName, "content-primary")
    }

    void testTonalRangeInitializingColor() {
        assertEquals(themeValueObject.getColor(), "white")
        assertEquals(themeValueObject.getColorCode(), "20")
        assertEquals(themeValueObject.getColorRange(), "gray")
        assertEquals(themeValueObject.getOpacity(), "0.10")
        assertEquals(themeValueObject.getReference(), "control-primary")
        assertEquals(themeValueObject.getOffset(), "10")
    }

    void testAddTonalRange() {
        themeAttribute.addTonalRange(LIGHT, themeValueObject)
        assertEquals(themeAttribute.attributeMap.size(), 1)
        TonalRange tonalRange = ((TonalRange) themeAttribute.getAttributeMap().get(LIGHT))
        assertEquals("white", tonalRange.getColor())
        assertEquals("20", tonalRange.getColorCode())
        assertEquals("gray", tonalRange.getColorRange())
        assertEquals("0.10", tonalRange.getOpacity())
        assertEquals("control-primary", tonalRange.getReference())
        assertEquals("10", tonalRange.getOffset())
    }

    void testAddTonalRangeWithReference() {
        themeAttribute.addTonalRange(LIGHT, "content-reference")
        assertEquals(themeAttribute.attributeMap.size(), 1)
        TonalRange tonalRange = ((TonalRange) themeAttribute.getAttributeMap().get(LIGHT))
        assertEquals("content-reference", tonalRange.getReference())
    }

    void testGetTonalRangeValueColor() {
        resetThemeValueObject()
        themeValueObject.color = "white"
        themeAttribute.addTonalRange(LIGHT, themeValueObject)
        TonalRange tonalRange = ((TonalRange) themeAttribute.getAttributeMap().get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        ArrayList list = new ArrayList()
        list.add(new ThemeAttribute("uidContentPrimary"))
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        assertEquals("@color/uid_level_white", colorValue)
    }

    void testGetTonalRangeValueColorWithOpacity() {
        resetThemeValueObject()
        themeValueObject.color = "white"
        themeValueObject.opacity = "0.10"
        themeAttribute.addTonalRange(LIGHT, themeValueObject)
        TonalRange tonalRange = ((TonalRange) themeAttribute.getAttributeMap().get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        ArrayList list = new ArrayList()
        list.add(new ThemeAttribute("uidContentPrimary"))
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        assertEquals("#1AFFFFFF", colorValue)
    }

    void testGetTonalRangeValueColorCode() {
        resetThemeValueObject()
        themeValueObject.colorCode = 20
        themeAttribute.addTonalRange(LIGHT, themeValueObject)
        TonalRange tonalRange = ((TonalRange) themeAttribute.getAttributeMap().get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        ArrayList list = new ArrayList()
        list.add(new ThemeAttribute("uidContentPrimary"))
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        assertEquals("@color/uid_blue_level_20", colorValue)
    }

    void testGetTonalRangeValueColorCodeAndColorRange() {
        resetThemeValueObject()
        themeValueObject.colorCode = 20
        themeValueObject.colorRange = "gray"
        themeAttribute.addTonalRange(LIGHT, themeValueObject)
        TonalRange tonalRange = ((TonalRange) themeAttribute.getAttributeMap().get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        ArrayList list = new ArrayList()
        list.add(new ThemeAttribute("uidContentPrimary"))
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        assertEquals("@color/uid_gray_level_20", colorValue)
    }

    void testGetTonalRangeValueColorCodeAndColorRangeAndOpacity() {
        resetThemeValueObject()
        themeValueObject.colorCode = 20
        themeValueObject.colorRange = "gray"
        themeValueObject.opacity = "0.10"
        themeAttribute.addTonalRange(LIGHT, themeValueObject)
        TonalRange tonalRange = ((TonalRange) themeAttribute.getAttributeMap().get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        ArrayList list = new ArrayList()
        list.add(new ThemeAttribute("uidContentPrimary"))
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        assertEquals("#1AC4C4C4", colorValue)
    }

    void testGetTonalRangeValueColorCodeAndOpacity() {
        resetThemeValueObject()
        themeValueObject.colorCode = 20
        themeValueObject.opacity = "0.10"
        themeAttribute.addTonalRange(LIGHT, themeValueObject)
        TonalRange tonalRange = ((TonalRange) themeAttribute.getAttributeMap().get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        ArrayList list = new ArrayList()
        list.add(new ThemeAttribute("uidContentPrimary"))
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        assertEquals("#1AABCBF7", colorValue)
    }

    void testGetTonalRangeValueFromReference() {
        resetThemeValueObject()
        themeValueObject.reference = "uidContentPrimary"
        themeAttribute.addTonalRange(LIGHT, themeValueObject)

        def themeValue = new ThemeValue();
        themeValue.color = "white"

        def attribute = new ThemeAttribute("uidContentPrimary")
        attribute.addTonalRange(LIGHT, themeValue)

        ArrayList list = new ArrayList()
        list.add(attribute)

        TonalRange tonalRange = ((TonalRange) themeAttribute.attributeMap.get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        assertEquals("@color/uid_level_white", colorValue)
    }

    void testGetTonalRangeValueFromReferenceAndOpacity() {
        resetThemeValueObject()
        themeValueObject.reference = "uidContentPrimary"
        themeAttribute.addTonalRange(LIGHT, themeValueObject)

        def themeValue = new ThemeValue();
        themeValue.color = "white"
        themeValue.opacity = "0.20"

        def attribute = new ThemeAttribute("uidContentPrimary")
        attribute.addTonalRange(LIGHT, themeValue)

        ArrayList list = new ArrayList()
        list.add(attribute)

        TonalRange tonalRange = ((TonalRange) themeAttribute.attributeMap.get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        assertEquals("#33FFFFFF", colorValue)
    }

    void testGetTonalRangeValueFromReferenceAndOffset() {
        resetThemeValueObject()
        themeValueObject.reference = "uidContentPrimary"
        themeAttribute.addTonalRange(LIGHT, themeValueObject)

        def themeValue = new ThemeValue();
        themeValue.colorCode = "50"
        themeValue.offset = "10"

        def attribute = new ThemeAttribute("uidContentPrimary")
        attribute.addTonalRange(LIGHT, themeValue)

        ArrayList list = new ArrayList()
        list.add(attribute)

        TonalRange tonalRange = ((TonalRange) themeAttribute.attributeMap.get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        assertEquals("@color/uid_blue_level_60", colorValue)
    }

    void testGetTonalRangeValueFromReferenceAndOffsetOverridingFromSemantic() {
        resetThemeValueObject()
        themeValueObject.reference = "uidContentPrimary"
        themeValueObject.colorCode = "40"
        themeValueObject.offset = "30"
        themeAttribute.addTonalRange(LIGHT, themeValueObject)

        def themeValue = new ThemeValue();
        themeValue.colorCode = "50"
        themeValue.offset = "10"

        def attribute = new ThemeAttribute("uidContentPrimary")
        attribute.addTonalRange(LIGHT, themeValue)

        ArrayList list = new ArrayList()
        list.add(attribute)

        TonalRange tonalRange = ((TonalRange) themeAttribute.attributeMap.get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        //Offset should not be taken into account in this test, thats why it is color_level_40
        assertEquals("@color/uid_blue_level_40", colorValue)
    }

    void testGetTonalRangeValueFromReferenceAndOpacityAndOffset() {
        resetThemeValueObject()
        themeValueObject.reference = "uidContentPrimary"
        themeAttribute.addTonalRange(LIGHT, themeValueObject)

        def themeValue = new ThemeValue();
        themeValue.colorCode = 60
        themeValue.opacity = "0.50"
        themeValue.offset = 20

        def attribute = new ThemeAttribute("uidContentPrimary")
        attribute.addTonalRange(LIGHT, themeValue)

        ArrayList list = new ArrayList()
        list.add(attribute)

        TonalRange tonalRange = ((TonalRange) themeAttribute.attributeMap.get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        assertEquals("#80092F52", colorValue)
    }

    void testGetTonalRangeValueFromReferenceAndOpacityAndOffsetOveridingFromSemantic() {
        resetThemeValueObject()
        themeValueObject.reference = "uidContentPrimary"
        themeValueObject.offset = 30
        themeValueObject.opacity = "0.10"
        themeAttribute.addTonalRange(LIGHT, themeValueObject)

        def referenceValue = new ThemeValue();
        referenceValue.colorCode = 60
        referenceValue.opacity = "0.50"

        def attribute = new ThemeAttribute("uidContentPrimary")
        attribute.addTonalRange(LIGHT, referenceValue)

        ArrayList list = new ArrayList()
        list.add(attribute)

        TonalRange tonalRange = ((TonalRange) themeAttribute.attributeMap.get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        assertEquals("#1A02101B", colorValue)
    }

    void testGetTonalRangeValueFromReferenceAndOpacityAccent() {
        resetThemeValueObject()
        themeValueObject.colorRange = "accent"
        themeValueObject.colorCode = "20"
        themeAttribute.addTonalRange(LIGHT, themeValueObject)

        ArrayList list = new ArrayList()

        TonalRange tonalRange = ((TonalRange) themeAttribute.attributeMap.get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        assertEquals("?attr/uidAccentLevel20", colorValue)
    }

    void testReferecenceAndOffset() {
        resetThemeValueObject()
        themeValueObject.reference = "uidContentPrimary"
        themeValueObject.offset = "-15"
        themeAttribute.addTonalRange(LIGHT, themeValueObject)

        def referenceValue = new ThemeValue();
        referenceValue.colorCode = "35"

        ArrayList list = new ArrayList()
        def attribute = new ThemeAttribute("uidContentPrimary")
        attribute.addTonalRange(LIGHT, referenceValue)
        list.add(attribute)

        TonalRange tonalRange = ((TonalRange) themeAttribute.attributeMap.get(LIGHT))
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        def colorValue = tonalRange.getValue("blue", colorsXmlInput, list)

        assertEquals("@color/uid_blue_level_20", colorValue)
    }

    private void resetThemeValueObject() {
        themeValueObject.color = null
        themeValueObject.colorCode = null
        themeValueObject.offset = null
        themeValueObject.reference = null
        themeValueObject.colorRange = null
        themeValueObject.opacity = null
    }
}
