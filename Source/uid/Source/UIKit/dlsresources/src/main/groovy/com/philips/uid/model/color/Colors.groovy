package com.philips.uid.model.color

import com.philips.uid.DLSResourceConstants
import com.philips.uid.attribute.AttributeManager
import com.philips.uid.attribute.AttributeModel
import com.philips.uid.helpers.NameConversionHelper

@Singleton
class Colors {
    static Map<String, ColorRange> colorRangeMap = new TreeMap<>();

    static def getColorForRange(colorRange, colorcode) {
        colorRangeMap.get(colorRange).colorValueMap.get(colorcode)
    }

    static def getColorNameForXmlItem(colorRange, colorCode) {
        def level = colorCode.number ? "level_": ""
        def alignedColorName = NameConversionHelper.replaceHyphenWithUnderScores(colorRange).toLowerCase()
        return "@color/${DLSResourceConstants.LIB_PREFIX}_${alignedColorName}_${level}${colorCode}"
    }

    //Only for hardcode colors which have no entry in brushes.json
    def getColorNameForHardCodedColor(color) {
        if (color.startsWith("#")) {
            return color
        }
        return "@color/uid_level_${color}"
    }

    //Only for hardcode colors which have no entry in brushes.json
    def getColorValueHardCodedColor(color) {
        if (color == "white") {
            return "#FFFFFF"
        } else if (color == "black") {
            return "#000000"
        }
        return color
    }

    static def addColorAttributes() {
        5.step(95, 5) {
            AttributeManager.instance.addAttribute(new AttributeModel(name:"uidColorLevel${it}", refType: "reference|color"))
        }
    }

    static def addAccentColorAttributes() {
        5.step(95, 5) {
            AttributeManager.instance.addAttribute(new AttributeModel(name:"uidAccentLevel${it}", refType: "reference|color"))
        }
    }

    static def addColorAndTonalRanges() {
        AttributeManager.instance.addAttribute(new AttributeModel(name: "uidColorRange", refType: "reference|string"))
        AttributeManager.instance.addAttribute(new AttributeModel(name: "uidTonalRange", refType: "reference|string"))
        AttributeManager.instance.addAttribute(new AttributeModel(name: "uidAccentRange", refType: "reference|string"))
        AttributeManager.instance.addAttribute(new AttributeModel(name: "uidNavigationRange", refType: "reference|string"))
    }
}