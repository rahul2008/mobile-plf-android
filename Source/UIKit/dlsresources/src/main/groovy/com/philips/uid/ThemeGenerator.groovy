/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid

import groovy.xml.MarkupBuilder

class ThemeGenerator {

    def invalidComponentRefList = new ArrayList()
    def createThemeXml(
            def allBrushAttributes, def allComponentAttributes, def dataValidationThemeValues) {
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        DLSResourceConstants.COLOR_RANGES.each {
            theme, colorName ->

                def writer = new StringWriter()
                def xml = new MarkupBuilder(writer)
                xml.setDoubleQuotes(true)

                xml.resources() {
                    buildThemeColorLevelMapping(xml, colorName)
                    DLSResourceConstants.TONAL_RANGES.each {
                        buildBrushesAndComponentAttributes(xml, allBrushAttributes, allComponentAttributes, it, colorName, colorsXmlInput, dataValidationThemeValues)
                    }
                }
                def themeFile = new File(DLSResourceConstants.getThemeFilePath(colorName))
                if (themeFile.exists()) {
                    themeFile.delete()
                }

                themeFile.createNewFile()
                themeFile.write(writer.toString())
        }
        println("Invalid component reference for " + invalidComponentRefList.toSet())
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

    def buildBrushesAndComponentAttributes(xml, List allBrushAttributes, List allComponentAttributes, it, colorName, colorsXmlInput, dataValidationThemeValues) {
        def defaultTonalRange = "$it".toString()
        def tonalRange = BrushParser.getCapitalizedValue("$it")
        def styleThemeName = "${DLSResourceConstants.THEME_PREFIX}." + BrushParser.getCapitalizedValue("${colorName}._${it}")

        DataValidation dataValidation = new DataValidation();
        xml.style("${DLSResourceConstants.ITEM_NAME}": "${styleThemeName}") {

            allBrushAttributes.each {
                def tr = tonalRange.toString()
                def value = "null"
                TonalRange themeValue = null;
                if (it.attributeMap.containsKey(tr)) {
                    themeValue = it.attributeMap.get(tr).clone()
                    dataValidation.decorateValidations(themeValue, dataValidationThemeValues, it.attrName, colorName, tr)
                    value = themeValue.getValue("${colorName}", colorsXmlInput, allBrushAttributes)
                }
                if (value == "@null") {
                    println(" Invalid combination Tonal Range " + tr + " Brush " + it.attrName + " Theme values " + themeValue.toString())
                }
                if (BrushParser.isSupportedAction(it.attrName)) {
                    item("${DLSResourceConstants.ITEM_NAME}": it.attrName, value)
                    if (it.attrName.contains("Accent")) {
                        if (themeValue.opacity != null) {
                            item("${DLSResourceConstants.ITEM_NAME}": it.attrName + "Alpha", getDimenName(themeValue.opacity))
                        }

                    }
                }
            }


            allComponentAttributes.each {
                if (BrushParser.isSupportedAction(it.attrName)) {
                    def reference = it.attributeMap.get(it.attrName).getAttributeValue(allBrushAttributes)
                    if (reference == "@null") {
                        invalidComponentRefList.add(it.attrName)
                    }
                    item("${DLSResourceConstants.ITEM_NAME}": it.attrName, reference)
                }
            }
        }
    }

    def getDimenName(opacity) {
        opacity = (int) Math.round((Float.valueOf(opacity) * 100));
        return "@dimen/uid_alpha_per_" + opacity
    }
}
