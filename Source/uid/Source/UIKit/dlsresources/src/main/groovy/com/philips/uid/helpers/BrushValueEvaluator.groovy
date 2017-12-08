package com.philips.uid.helpers

import com.philips.uid.model.brush.Brush
import com.philips.uid.model.brush.BrushValue
import com.philips.uid.model.color.Colors

import static junit.framework.Assert.assertNotNull
import static junit.framework.Assert.assertNull

class BrushValueEvaluator {
    static def getValue(BrushValue brushValue, List<Brush> brushes, colorRange, tonalRange) {

        //Can happen if value is missing from validation json
        if (brushValue.colorRange == "validation") {
            return "@null"
        }

        if (containsOnlyOffset(brushValue)) {
            nullifyOffsetWithColorCode(brushValue)
        }

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
                brushValue.colorRange = colorRange
            }

            //Add offset before calculating final value, should not be possible. Only for white it should occur
            def colorCode = addOffsetIfAny(brushValue)

            //We have opacity, we have to give hardcoded color
            if (brushValue.opacity) {
                def color = Colors.instance.getColorForRange(brushValue.colorRange ?: colorRange, colorCode)
                return applyOpacityOnColor(color, brushValue.opacity)
            }
            return Colors.instance.getColorNameForXmlItem(brushValue.colorRange ?: colorRange, colorCode)
        } else if (brushValue.color) {
            //We consider white as colorCode 0. So add offset if present
            if (brushValue.color == "white" && brushValue.offset) {
                nullifyOffsetWithColorCode(brushValue)
                return getValue(brushValue, brushes, colorRange, tonalRange)
            }
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
        clonedBrushValue.color = brushValue.color ?: clonedBrushValue.color
        clonedBrushValue.colorCode = brushValue.colorCode ?: clonedBrushValue.colorCode
        clonedBrushValue.offset = brushValue.offset ?: clonedBrushValue.offset
        clonedBrushValue.colorRange = brushValue.colorRange ?: clonedBrushValue.colorRange
        clonedBrushValue.opacity = brushValue.opacity ?: clonedBrushValue.opacity
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

    //Don't consider offset for this comparison
    static def containsOnlyOpacity(BrushValue brushValue) {
        return ((brushValue.opacity)
                && (brushValue.color == null
                && brushValue.reference == null
                && brushValue.colorCode == null
                && brushValue.colorRange == null))
    }

    //Don't consider opacity for this comparison
    static def containsOnlyOffset(BrushValue brushValue) {
        return ((brushValue.offset)
                && (brushValue.color == null
                && brushValue.reference == null
                && brushValue.colorCode == null
                && brushValue.colorRange == null))
    }

    static def nullifyOffsetWithColorCode(BrushValue brushValue) {
        assertNull(brushValue.colorCode)
        assertNotNull(brushValue.offset)
        brushValue.colorCode = brushValue.offset
        brushValue.offset = null
    }

    static def getBrushFormatNameFromBrushValue(BrushValue brushValue) {
        if (brushValue && containsOnlyOpacity(brushValue)) {
            return "reference|float"
        }
        return "reference|color"
    }
}