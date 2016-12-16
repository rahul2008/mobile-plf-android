package com.philips.uid

class ThemeAttribute {
    def attrName
    def attributeMap = new HashMap()

    ThemeAttribute(attributeName) {
        attrName = attributeName;
    }

    void addTonalRange(name, colorNumber, alpha, reference, rangeName) {
        attributeMap.put(name, new TonalRange(name, colorNumber, alpha, reference, rangeName))
    }

    class TonalRange {
        def name
        def colorNumber
        def alpha
        def reference
        def rangeName

        TonalRange(name, colorNumber, alpha, reference, rangeName) {
            this.name = name
            this.colorNumber = colorNumber
            this.alpha = alpha
            this.reference = reference
            this.rangeName = rangeName
        }

        //color range in format of group_blue or blue or aqua , having match with uid_colors.xml
        def getValue(colorRange, colorsXmlInput) {
            if (alpha == null && reference != null) {
                return "?attr/${reference}"
            } else if (alpha == null) {
                return "@color/uid_${colorRange}_${colorNumber}"
            } else if (colorNumber != null) {
                def hexAlpha = alphaToHex(alpha)
                def colorCode = "uid_${colorRange}_${colorNumber}"
                def color = colorsXmlInput.findAll { it.@name == colorCode }*.text().get(0)
                return color.replace("#", "#${hexAlpha}")
            }
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
    }
}