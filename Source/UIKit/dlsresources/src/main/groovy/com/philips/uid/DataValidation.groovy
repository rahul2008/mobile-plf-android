/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid

class DataValidation {

    private final static COLOR = "COLOR";
    private final static COLOR_CODE = "COLOR_CODE";
    private final static COLOR_RANGE = "COLOR_RANGE";
    private final static REFERENCE = "REFERENCE";
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
        if (theme.color == "validation") {
            assignValidationValues(theme, validationMap, attrname, colorRange, tonalRange, COLOR)
        }
        if (theme.colorRange == "validation") {
            assignValidationValues(theme, validationMap, attrname, colorRange, tonalRange, COLOR_RANGE)
        }
        if (theme.colorCode == "validation") {
            assignValidationValues(theme, validationMap, attrname, colorRange, tonalRange, COLOR_CODE)
        }
        if (theme.reference == "validation") {
            assignValidationValues(theme, validationMap, attrname, colorRange, tonalRange, REFERENCE)
        }
    }

    void assignValidationValues(themeValue, validationMap, attrname, colorRange, tonalRange, type) {
        List attrsList = validationMap.get(BrushParser.getCapitalizedValue(colorRange))
        for (ThemeAttribute attr : attrsList) {
            if (attr.attrName == attrname) {
                for (Map.Entry entry : attr.attributeMap.entrySet()) {
                    if (entry.getKey() == tonalRange) {
//                        println(attrname + "\t"+  colorRange+":"+tonalRange+ "\t" +entry.value.colorRange )
                        getValueFromType(entry, themeValue, type)
                    }
                }
            }

        }
    }

    private void getValueFromType(Map.Entry entry, themeValue, type) {
        if (type == COLOR) {
            themeValue.color = entry.value.color
        } else if (type == COLOR_RANGE) {
            themeValue.colorRange = entry.value.colorRange
        } else if (type == COLOR_CODE) {
            themeValue.colorCode = entry.value.colorCode
        } else if (type == REFERENCE) {
            themeValue.reference = entry.value.reference
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