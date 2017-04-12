/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.uid

import com.google.gson.Gson
import groovy.json.JsonSlurper

class BrushParser {

    public static void main(String[] args) {

        def jsonSlurper = new JsonSlurper()
        def brushesMap = jsonSlurper.parseText(new File(DLSResourceConstants.PATH_SEMANTIC_BRUSH_JSON).text)
        def componentMap = jsonSlurper.parse(new File(DLSResourceConstants.PATH_COMPONENT_JSON))

        def allBrushAttributes = generateBrushAttributes(brushesMap)
        def allComponentAttributes = generateComponentAttributes(componentMap)

        new AttributeGenerator().flushAttrsFile(allBrushAttributes, allComponentAttributes)
        new ThemeGenerator().createThemeXml(allBrushAttributes, allComponentAttributes)
        new NavigationStyleCreator().create(allBrushAttributes, allComponentAttributes)
        new AccentRangeGenerator().generateAccentRanges()
    }

    static def generateBrushAttributes(brushesMap) {
        def allAttributes = new ArrayList()

        Gson gson = new Gson();
        brushesMap = brushesMap[0]
        brushesMap.each {
            semanticName, colorRange ->
                def attributeName = "${DLSResourceConstants.LIB_PREFIX}" + semanticName.split("${DLSResourceConstants.HIPHEN}").collect {
                    it.capitalize()
                }.join("")
                def themeAttr = new ThemeAttribute(attributeName)
                colorRange.each {
                    theme ->
                        def name = getCapitalizedValue(theme.key)
                        def themeValue = theme.value;
                        ThemeValue themValueObject = gson.fromJson(themeValue.toString(), ThemeValue.class)
                        if (themValueObject.reference != null) {
                            themValueObject.reference = "${DLSResourceConstants.LIB_PREFIX}${themValueObject.reference.split("${DLSResourceConstants.HIPHEN}").collect { it.capitalize() }.join("")}"
                        }
                        themeAttr.addTonalRange(name, themValueObject)
                }
                allAttributes.add(themeAttr)
        }
        return allAttributes
    }

    static def generateComponentAttributes(componentMap) {
        def allComponentAttributes = new ArrayList()
        Gson gson = new Gson();

        componentMap.each {
            componentData ->
                Control component = gson.fromJson(componentData.toString(), Control.class)
                String[] parent = component.getParent()
                def propertyValue = getAttributeName(component.getProperty().getValue())

                if (parent.length > 0) {
                    parent.each {
                        parentName ->
                            def attrName = getAttributeName(component.getAttributeName(parentName));
                            def themeAttr = new ThemeAttribute(attrName)
                            themeAttr.addTonalRange(attrName, propertyValue)
                            allComponentAttributes.add(themeAttr)
                    }
                } else {
                    def attrName = getAttributeName(component.getAttributeName(null));
                    def themeAttr = new ThemeAttribute(attrName)
                    themeAttr.addTonalRange(attrName, propertyValue)
                    allComponentAttributes.add(themeAttr)
                }
        }
        return allComponentAttributes;
    }

    static def getAttributeName(String attrName) {
        def attributeName = "${DLSResourceConstants.LIB_PREFIX}" + getCapitalizedValue(attrName)
        return attributeName

    }

    static def getCapitalizedValue(def attribute) {
        def capitalizedAttribute = attribute.replaceAll("${DLSResourceConstants.HIPHEN}", "${DLSResourceConstants.UNDERSCORE}").replaceAll(" ", "").toLowerCase().split("${DLSResourceConstants.UNDERSCORE}").collect {
            it.capitalize()
        }.join("")
        return capitalizedAttribute;
    }

    public static boolean isSupportedAction(Object name) {
        return !name.contains(DLSResourceConstants.HOVER)
    }
}