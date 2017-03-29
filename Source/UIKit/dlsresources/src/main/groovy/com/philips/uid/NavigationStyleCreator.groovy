/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

import com.philips.uid.BrushParser
import com.philips.uid.DLSResourceConstants
import com.philips.uid.ThemeAttribute
import groovy.xml.MarkupBuilder


class NavigationStyleCreator {
    void create(allBrushAttributes, allComponentAttributes) {
        println("Creating NavigationStyle")
        def writer = new StringWriter()
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)


        def colorXML = new MarkupBuilder(writer)
        colorXML.setDoubleQuotes(true)
        colorXML.resources() {
            DLSResourceConstants.TONAL_RANGES.each {
                tonalRange ->
                    tonalRange = BrushParser.getCapitalizedValue(tonalRange)
                    colorXML.style("${DLSResourceConstants.ITEM_NAME}": "UIDNavigationbar" + tonalRange) {


                        DLSResourceConstants.NAVIGATION_ATTRIBUTES.each {
                            attribute ->
                                def index = allComponentAttributes.indexOf(new ThemeAttribute(attribute))

                                if (index >= 0) {
                                    ThemeAttribute themeAttr = allComponentAttributes.get(index);
//                                    println("themeAttr  " + themeAttr + " value " + themeAttr.attributeMap.get(themeAttr.attrName).getAttributeName(allBrushAttributes))
                                    def attName = themeAttr.attributeMap.get(themeAttr.attrName).getAttributeName(allBrushAttributes)
                                    def themeAttrIndex = allBrushAttributes.indexOf(new ThemeAttribute(attName))
                                    if (themeAttrIndex >= 0) {
                                        ThemeAttribute themeAttrValue = allBrushAttributes.get(themeAttrIndex)
//                                        println("themeAttrValue  " + themeAttrValue + " tonalRange " + tonalRange)
                                        def tonalRangeValue = themeAttrValue.attributeMap.get(tonalRange)
                                        def value = tonalRangeValue.getValue("color", colorsXmlInput, allBrushAttributes)
                                        item("${DLSResourceConstants.ITEM_NAME}": attribute, value)
                                    }

                                } else {
                                    println("themeAttr  " + attribute + " not found")
                                }
                        }
                    }
            }
        }
        def outDir = new File(DLSResourceConstants.PATH_OUT)

        if (!outDir.exists()) {
            outDir.mkdir()
        } else {
            outDir.delete()
            outDir.mkdir()
        }

        def colorFile = new File(DLSResourceConstants.PATH_OUT_NAVIGATION_FILE)
        if (colorFile.exists()) {
            colorFile.delete()
        }

        colorFile.createNewFile()
        colorFile.write(writer.toString())
    }

}