/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid

import groovy.xml.MarkupBuilder

class ThemeGenerator {

    def createThemeXml(def allBrushAttributes, def allComponentAttributes) {
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        DLSResourceConstants.COLOR_RANGES.each {
            theme, colorName ->

                def writer = new StringWriter()
                def xml = new MarkupBuilder(writer)
                xml.setDoubleQuotes(true)

                xml.resources() {
                    buildThemeColorLevelMapping(xml, colorName)
                    DLSResourceConstants.TONAL_RANGES.each {
                        buildBrushesAndComponentAttributes(xml, allBrushAttributes, allComponentAttributes, it, colorName, colorsXmlInput)
                    }
                }
                def themeFile = new File(DLSResourceConstants.getThemeFilePath(colorName))
                if (themeFile.exists()) {
                    themeFile.delete()
                }

                themeFile.createNewFile()
                themeFile.write(writer.toString())
        }

    }

    def buildThemeColorLevelMapping(xml, colorName) {
        def baseTheme = "${DLSResourceConstants.THEME_PREFIX}." + new BrushParser().getCapitalizedValue("${colorName}")

        xml.style("${DLSResourceConstants.ITEM_NAME}": "${baseTheme}") {
            for (int colorLevel = 5; colorLevel <= 90;) {

                item("${DLSResourceConstants.ITEM_NAME}": BrushParser.getAttributeName("Color_Level_" + colorLevel),
                        "${DLSResourceConstants.COLOR_REFERENCE}${DLSResourceConstants.LIB_PREFIX}_${colorName}_${DLSResourceConstants.LEVEL}_" + colorLevel)
                colorLevel = colorLevel + DLSResourceConstants.COLOR_OFFSET
            }
        }
    }

    def buildBrushesAndComponentAttributes(xml, List allBrushAttributes, List allComponentAttributes, it, colorName, colorsXmlInput) {
        def defaultTonalRange = "$it".toString()
        def tonalRange = BrushParser.getCapitalizedValue("$it")
        def styleThemeName = "${DLSResourceConstants.THEME_PREFIX}." + BrushParser.getCapitalizedValue("${colorName}._${it}")

        xml.style("${DLSResourceConstants.ITEM_NAME}": "${styleThemeName}") {

            allBrushAttributes.each {
                def tr = tonalRange.toString()
                if (it.attributeMap.containsKey(tr)) {
                    item("${DLSResourceConstants.ITEM_NAME}": it.attrName, it.attributeMap.get(tr).getValue("${colorName}", colorsXmlInput, allBrushAttributes))
                } else {
                    item("${DLSResourceConstants.ITEM_NAME}": it.attrName, it.attributeMap.get(defaultTonalRange).getValue("${colorName}", colorsXmlInput, allBrushAttributes))
                }
            }

            allComponentAttributes.each {
                item("${DLSResourceConstants.ITEM_NAME}": it.attrName, it.attributeMap.get(it.attrName).getAttributeValue(allBrushAttributes))
            }
        }
    }

}
