/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid

class DataValidation {

    def getAllAttributes(datavalidationMap) {
        def attributes = new HashMap()
        datavalidationMap = datavalidationMap[0]
        datavalidationMap.each {
            item ->
//                println(" item " + item.key)
                item.each {
                    colorRange ->
//                        println(" colorRange " + colorRange.key)
                        def allAttrs = new ArrayList()
                        colorRange.value.each {
                            attribute ->
//                                println(" attribute " + attribute.value)
                                attribute.value.each {
                                    theme ->
                                        def attributeName = "${DLSResourceConstants.LIB_PREFIX}" + BrushParser.getCapitalizedValue(attribute.key)
                                        def themeAttr = new ThemeAttribute(attributeName)
                                        def name = theme.key
                                        def themeValue = theme.value;
                                        ThemeValue themValueObject = new ThemeValue()
                                        themValueObject.color = themeValue.get("color")
                                        themValueObject.colorCode = themeValue.get("color-code")
                                        themValueObject.colorRange = themeValue.get("color-range")
                                        themValueObject.reference = themeValue.get("reference")
                                        themValueObject.opacity = themeValue.get("opacity")
                                        themValueObject.offset = themeValue.get("offset")
//                                        println(" color " + themValueObject)
                                        themeAttr.addTonalRange(name, themValueObject)
//                                        println(themeAttr)
                                        allAttrs.add(themeAttr)
                                }

                        }
                        attributes.put(BrushParser.getCapitalizedValue(colorRange.key), allAttrs)
                }
        }
//        println(" attributes " + attributes)
        return attributes;
    }

    def getAttributeNames(datavalidationMap) {
        Set attributes = new HashSet()
        def item = datavalidationMap.get("Gray")
        item.each {
            def attributeName = it.attrName
            attributes.add(attributeName)
        }
        return attributes;
    }
}