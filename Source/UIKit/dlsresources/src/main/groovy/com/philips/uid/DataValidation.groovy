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
                                def attributeName = "${DLSResourceConstants.LIB_PREFIX}" + BrushParser.getCapitalizedValue(attribute.key)
                                def themeAttr = new ThemeAttribute(attributeName)
                                attribute.value.each {
                                    theme ->
                                        def name = BrushParser.getCapitalizedValue(theme.key)
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
                                }
                                allAttrs.add(themeAttr)
                        }
                        attributes.put(BrushParser.getCapitalizedValue(colorRange.key), allAttrs)
                }
        }
//        println(" attributes " + attributes)
        return attributes;
    }

    def decorateValidations(theme, validationMap, attrname, colorRange, tonalRange) {
        if (theme.colorRange == "validation") {
            return getColorRange(theme, validationMap, attrname, colorRange, tonalRange)
        } else if (theme.colorCode == "validation") {
            return getColorCode(theme, validationMap, attrname, colorRange, tonalRange)
        } else if (theme.reference == "validation") {
            return getReference(theme, validationMap, attrname, colorRange, tonalRange)
        }
    }

    void getColorRange(themeValue, validationMap, attrname, colorRange, tonalRange) {
        List attrsList = validationMap.get(BrushParser.getCapitalizedValue(colorRange))
        for (ThemeAttribute attr : attrsList) {
            if (attr.attrName == attrname) {
                for (Map.Entry entry : attr.attributeMap.entrySet()) {
                    if (entry.getKey() == tonalRange) {
//                        println(attrname + "\t"+  colorRange+":"+tonalRange+ "\t" +entry.value.colorRange )
                        themeValue.colorRange = entry.value.colorRange
                    }
                }
            }

        }
    }

    def getColorCode(themeValue, validationMap, attrname, colorRange, tonalRange) {
        List attrsList = validationMap.get(BrushParser.getCapitalizedValue(colorRange))
        for (ThemeAttribute attr : attrsList) {
            if (attr.attrName == attrname) {
                for (Map.Entry entry : attr.attributeMap.entrySet()) {
                    if (entry.getKey() == tonalRange) {
//                        println(attrname+ "\t"+ colorRange+":"+tonalRange+ "\t" +entry.value.colorCode )
                        themeValue.colorCode = entry.value.colorCode
                    }
                }
            }

        }
    }

    def getReference(themeValue, validationMap, attrname, colorRange, tonalRange) {
        List attrsList = validationMap.get(BrushParser.getCapitalizedValue(colorRange))
        for (ThemeAttribute attr : attrsList) {
            if (attr.attrName == attrname) {
                for (Map.Entry entry : attr.attributeMap.entrySet()) {
                    if (entry.getKey() == tonalRange) {
//                        println(attrname+ "\t"+ colorRange+":"+tonalRange+ "\t" +entry.value.reference )
                        themeValue.reference = entry.value.reference
                    }
                }
            }

        }
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