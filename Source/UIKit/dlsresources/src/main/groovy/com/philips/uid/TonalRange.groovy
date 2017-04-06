/*
 * Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.uid

class TonalRange {
    def name
    def colorCode
    def color
    def reference
    def colorRange
    def opacity
    def offset

    TonalRange(
            def name,
            def colorCode, def reference, def colorRange, def color, def opacity, def offset) {
        this.name = name
        this.colorCode = colorCode
        this.reference = reference
        this.colorRange = colorRange
        this.color = color
        this.opacity = opacity
        this.offset = offset
    }

    TonalRange(name, referenceValue) {
        this.name = name
        this.reference = referenceValue
    }
    //color range in format of group_blue or blue or aqua , having match with uid_colors.xml
    def getValue(color_range, colorsXmlInput, allAttributes) {
        if (reference != null) {
            def index = allAttributes.indexOf(new ThemeAttribute(reference))

//                println("all attr: " + allAttributes.toListString() + " index: " + index)

            if (index > -1) {
                ThemeAttribute referenceValue = allAttributes.get(index)
                if (referenceValue != null) {
                    TonalRange themeReferenceValue = referenceValue.attributeMap.get(name);
                    if (themeReferenceValue != null) {
//                            println("###  222 referenceValue " + name + " Att " + referenceValue.attrName + " " + themeReferenceValue.color + " " + themeReferenceValue.colorCode + " "
//                                    + themeReferenceValue.colorRange + " " + themeReferenceValue.offset + " " + themeReferenceValue.opacity + " " + themeReferenceValue.reference +
//                                    " value " + themeReferenceValue.getValue(color_range, colorsXmlInput, allAttributes))
                        if (color == null) {
                            color = themeReferenceValue.color
                        }
                        if (offset == null) {
                            offset = themeReferenceValue.offset
                        }
                        if (colorCode == null) {
                            colorCode = themeReferenceValue.colorCode

                            if (offset != null && colorCode != null) {
                                colorCode = Integer.valueOf(colorCode) + Integer.valueOf(offset)
                            }
                            if (offset != null && colorCode == null) {
                                colorCode = Integer.valueOf(offset)
                            }
                        }
                        if (colorRange == null) {
                            colorRange = themeReferenceValue.colorRange
                        }
                        if (opacity == null) {
                            opacity = themeReferenceValue.opacity
                        }
                        if (themeReferenceValue.reference != null) {
                            reference = themeReferenceValue.reference
                        } else {
                            reference = null
                        }
                        return getValue(color_range, colorsXmlInput, allAttributes)
                    }
                }
            } else {
                return "@null"
            }
        } else if (colorCode != null) {
            if (colorRange == "accent") {
                return "?attr/uidAccentLevel" + colorCode
            }
            if (colorRange == "validation")
                return "@null"

            def hexAlpha = ""
            if (opacity != null) {
                hexAlpha = alphaToHex(Float.valueOf(opacity))
            }

            def colorReference = "${DLSResourceConstants.LIB_PREFIX}_" + color_range + "_${DLSResourceConstants.LEVEL}_" + colorCode
            if (colorRange != null) {
                colorReference = "${DLSResourceConstants.LIB_PREFIX}_" + colorRange + "_${DLSResourceConstants.LEVEL}_" + colorCode
            }
            colorReference = colorReference.replaceAll("${DLSResourceConstants.HIPHEN}", "${DLSResourceConstants.UNDERSCORE}")
            def colorValue = getColorValue(colorsXmlInput, colorReference)
            if (colorValue == "@null") {
                return "?attr/" + BrushParser.getAttributeName("Color_${DLSResourceConstants.LEVEL}_" + colorCode)
            }
            if (hexAlpha.isEmpty() && colorValue != null) {
//                println("colorReference: #" + colorReference + "#" + " colorValue " + colorValue)
                return "@color/${colorReference}";
            }
            def colorWithHex = colorValue.replace("#", "#${hexAlpha}")
            return colorWithHex
        } else if (color != null) {
            def colorReference = "${DLSResourceConstants.LIB_PREFIX}_level_${color}";
//            println("colorReference: #" + colorReference +"#" +" name " + name)

            def colorValue = getColorValue(colorsXmlInput, colorReference)
            if (opacity != null) {
                def hexAlpha = alphaToHex(Float.valueOf(opacity))
                def colorWithHex = colorValue.replace("#", "#${hexAlpha}")
                return colorWithHex;
            }
            return "@color/${colorReference}"
        }
//        println(" Invalid combination " + this.toString())
        return "@null"
    }

    def getAttributeValue(allAttributes) {
        def index = allAttributes.indexOf(new ThemeAttribute(reference))

        if (index > -1) {
            return "${DLSResourceConstants.ATTR}" + reference
        }
//        println(" Invalid Attribute " + reference)

        return "@null"
    }

    def getAttributeName(allAttributes) {
        def index = allAttributes.indexOf(new ThemeAttribute(reference))

        if (index > -1) {
            return reference
        }
//        println(" Invalid Attribute " + reference)

        return "@null"
    }

    def alphaToHex(alpha) {
        int roundAlpha = (int) Math.round(alpha * 255)
        String hexAlpha = Integer.toHexString(roundAlpha).toUpperCase();
        if (hexAlpha.length() == 1) {
            hexAlpha = "0${hexAlpha}"
        }
        return hexAlpha
    }

    def getColorValue(colorsXmlInput, colorReference) {
        try {
            return colorsXmlInput.findAll {
                it.@name == colorReference
            }*.text().get(0)
        } catch (IndexOutOfBoundsException exception) {
//            println("invalid colorCode with colorName: " + colorReference)
            return "@null"
        }
    }

    @Override
    public String toString() {
        return "TonalRange{" +
                "name=" + name +
                ", colorCode=" + colorCode +
                ", color=" + color +
                ", reference=" + reference +
                ", colorRange=" + colorRange +
                ", opacity=" + opacity +
                ", offset=" + offset +
                '}';
    }
}
