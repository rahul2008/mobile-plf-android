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

    Map getAttributesMap() {
        return attributeMap
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
    }
}