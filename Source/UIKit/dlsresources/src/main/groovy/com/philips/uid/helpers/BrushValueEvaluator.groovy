package com.philips.uid.helpers

import com.philips.uid.model.brush.Brush
import com.philips.uid.model.brush.BrushValue
import com.philips.uid.model.color.Colors

import static junit.framework.Assert.assertNull

class BrushValueEvaluator {
    static def getValue(BrushValue brushValue, List<Brush> brushes, colorRange, tonalRange) {
        if (containsOnlyOpacity(brushValue)) {
            return brushValue.opacity
        }
        if (brushValue.reference) {
            def refBrush = brushes.find { it.brushName == brushValue.reference }
            def clonedBrushValue = refBrush.brushValueMap.get(tonalRange).clone()
            updateCloneBrushWithRealValues(brushValue, clonedBrushValue)
            return getValue(clonedBrushValue, brushes, colorRange, tonalRange)
        } else if (brushValue.colorCode) {
            if (brushValue.colorRange == "accent") {
                return "?attr/uidAccentLevel" + brushValue.colorCode
            }

            //Add offset before calculating final value
            def colorCode = addOffsetIfAny(brushValue)

            //We have opacity, we have to give hardcoded color
            if (brushValue.opacity) {
                def color
                if (brushValue.colorRange) {
                    color = Colors.instance.getColorForRange(brushValue.colorRange, colorCode)
                } else {
                    color = Colors.instance.getColorForRange(colorRange, colorCode)
                }
                return applyOpacityOnColor(color, brushValue.opacity)
            }
            assertNull(brushValue.opacity)
            return Colors.instance.getColorNameForXmlItem(brushValue.colorRange?:colorRange, colorCode)
        } else if (brushValue.color) {
            def color = Colors.instance.getColorValueHardCodedColor(brushValue.color)
            if (brushValue.opacity) {
                return applyOpacityOnColor(color, brushValue.opacity)
            }
            return Colors.instance.getColorNameForHardCodedColor(brushValue.color)
        }
    }

    static def addOffsetIfAny(BrushValue brushValue) {
        if (brushValue.colorCode && brushValue.offset) {
            return String.valueOf(Integer.valueOf(brushValue.colorCode) + Integer.valueOf(brushValue.offset))
        }
        return brushValue.colorCode
    }

    static def updateCloneBrushWithRealValues(brushValue, clonedBrushValue) {
        clonedBrushValue.colorCode = brushValue.colorCode ?: clonedBrushValue.colorCode
        clonedBrushValue.offset = brushValue.offset ?: clonedBrushValue.offset
        clonedBrushValue.colorRange = brushValue.colorRange ?: clonedBrushValue.colorRange
        clonedBrushValue.opacity = brushValue.opacity ?: clonedBrushValue.opacity

        if (brushValue.colorCode == null) {
            def colorCode = clonedBrushValue.colorCode
            if (brushValue.offset != null && colorCode != null) {
                clonedBrushValue.colorCode = String.valueOf(Integer.valueOf(colorCode) + Integer.valueOf(brushValue.offset))
            }
            if (brushValue.offset != null && colorCode == null) {
                clonedBrushValue.colorCode = brushValue.offset
            }
        }
    }

    static private def applyOpacityOnColor(colorValue, opacity) {
        def hexAlpha = alphaToHex(Float.valueOf(opacity))
        return colorValue.replace("#", "#${hexAlpha}")
    }

    static private def alphaToHex(alpha) {
        int roundAlpha = (int) Math.round(alpha * 255)
        String hexAlpha = Integer.toHexString(roundAlpha).toUpperCase();
        if (hexAlpha.length() == 1) {
            hexAlpha = "0${hexAlpha}"
        }
        return hexAlpha
    }

    static private def containsOnlyOpacity(BrushValue brushValue) {
        return ((brushValue.opacity != null)
                && (brushValue.color == null
                && brushValue.color == null
                && brushValue.reference == null
                && brushValue.colorCode == null
                && brushValue.colorRange == null))
    }

    static def getBrushFormatNameFromBrushValue(BrushValue brushValue) {
        if (containsOnlyOpacity(brushValue)) {
            return "reference|float"
        }
        return "reference|color"
    }
}