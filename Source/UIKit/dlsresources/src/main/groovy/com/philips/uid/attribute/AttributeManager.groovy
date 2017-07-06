package com.philips.uid.attribute

import com.philips.uid.helpers.BrushValueEvaluator

@Singleton
class AttributeManager {
    private List<AttributeModel> attributesList = new ArrayList<>()

    def addAtrribute(componentName, brushValue) {
        def formatString = BrushValueEvaluator.getBrushFormatNameFromBrushValue(brushValue)
        def model = new AttributeModel(name: componentName, refType: formatString)
        addAttribute(model)
    }

    def addAttribute(AttributeModel model) {
        if (isAttributeAllowed(model.name) && !attributesList.contains(model)) {
            attributesList.add(model)
        }
    }

    def getAttributesList() {
        attributesList.clone()
    }

    def isAttributeAllowed(name) {
        !name.toLowerCase().contains("hover")
    }
}