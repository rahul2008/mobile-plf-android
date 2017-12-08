package com.philips.uid.parser

import com.philips.uid.DLSResourceConstants
import com.philips.uid.model.brush.BrushValue
import com.philips.uid.model.validation.ValidationBrush
import com.philips.uid.model.validation.ValidationModel
import groovy.json.JsonSlurper

class ValidationParser {

    ValidationParser() {
        createValidationLists()
    }

    def createValidationLists() {
        def validations = new JsonSlurper().parseText(readValidationJSON())[0]

        validations.each {
            def colorRange = it.key
            List<ValidationBrush> validationBrushes = new ArrayList<>()
            it.value.each {
                def brushName = it.key
                def validationBrush = new ValidationBrush()
                it.value.each {
                    //Todo : JSON exception for # in string. So we create object manually.
                    //If we are able to fix the # issue, it should use fromJSON method
                    def tonalRange = it.key.toString()
                    BrushValue brushValue = new BrushValue()
                    brushValue.color = it.value.get("color")
                    brushValue.colorCode = it.value.get("color-code")
                    brushValue.colorRange = it.value.get("color-range")
                    brushValue.opacity = it.value.get("opacity")
                    brushValue.reference = it.value.get("reference")
                    brushValue.offset = it.value.get("offset")

                    validationBrush.brushName = brushName
                    validationBrush.validationBrushMap.put(tonalRange, brushValue)
                }
                validationBrushes.add(validationBrush)
            }
            ValidationModel.instance.validationModel.put(colorRange, validationBrushes)

        }
    }

    def decorateBrush(brushValue, colorRange, tonalRange, brushName) {
        BrushValue bv = ValidationModel.instance.validationModel?.get(colorRange)?.find {
            it.brushName == brushName
        }?.validationBrushMap?.get(tonalRange)
        BrushValue resultBrush = brushValue.clone()
        if (bv) {
            def clonedBrush = bv.clone()
            resultBrush.color = clonedBrush.color ?: resultBrush.color
            resultBrush.colorCode = clonedBrush.colorCode ?: resultBrush.colorCode
            resultBrush.opacity = clonedBrush.opacity ?: resultBrush.opacity
            resultBrush.offset = clonedBrush.offset ?: resultBrush.offset
            resultBrush.colorRange = clonedBrush.colorRange

            resultBrush.reference = clonedBrush.reference
        }
        return resultBrush
    }

    def readValidationJSON() {
        new File(DLSResourceConstants.PATH_VALIDATION_JSON).text
    }
}