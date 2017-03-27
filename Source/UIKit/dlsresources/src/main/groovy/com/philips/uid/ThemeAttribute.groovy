/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid

class ThemeAttribute {
    def attrName
    def attributeMap = new HashMap()

    ThemeAttribute(attributeName) {
        attrName = attributeName;
    }

    @Override
    boolean equals(final Object obj) {
        if (obj instanceof ThemeAttribute) {
            ThemeAttribute range = (ThemeAttribute) obj
            return range.attrName == attrName
        }
        return false;
    }

    void addTonalRange(attrName, ThemeValue themeValue) {
        TonalRange tonalRange = new TonalRange(attrName, themeValue.colorCode, themeValue.reference, themeValue.colorRange, themeValue.color, themeValue.opacity, themeValue.offset)
        attributeMap.put(attrName, tonalRange)
    }


    void addTonalRange(name, reference) {
        attributeMap.put(name, new TonalRange(name, reference))
    }

    @Override
    public String toString() {
        return "ThemeAttribute{" +
                "attrName=" + attrName +
                ", attributeMap=" + attributeMap +
                '}';
    }


}