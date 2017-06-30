package com.philips.uid

import com.philips.uid.attribute.AttributeManager
import com.philips.uid.helpers.BrushValueEvaluator
import com.philips.uid.helpers.NameConversionHelper
import com.philips.uid.model.brush.BrushValue
import com.philips.uid.model.color.Colors
import com.philips.uid.model.component.Control
import com.philips.uid.model.navigation.NavigationAttribute
import com.philips.uid.parser.BrushParser
import com.philips.uid.parser.ColorParser
import com.philips.uid.parser.ComponentParser
import com.philips.uid.parser.ValidationParser
import groovy.xml.MarkupBuilder

//Generate Colors
ColorParser colorParser = new ColorParser()
colorParser.generateColorsXML()

//Generate Brushes
BrushParser brushParser = new BrushParser()

//Generate components
ComponentParser componentParser = new ComponentParser()

//Generate validation parser
ValidationParser validationParser = new ValidationParser()

//Add color and accent attributes
Colors.addColorAttributes()
Colors.addAccentColorAttributes()

//Generate accent colors
new AccentRangeGenerator().generateAccentRanges()

//Generate and write component files
generateTheme(componentParser, brushParser, validationParser)

//Generate Navigation styles, must be called when all the components are created.
NavigationStylesGenerator.instance.generateNavigationStyles()

//flush all the attributes
flushAllAttributes()

def generateTheme(ComponentParser componentParser, BrushParser brushParser, ValidationParser validationparser) {

    DLSResourceConstants.COLOR_RANGES.each {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.setDoubleQuotes(true)

        def colorRange = it.value.toString()
        def colorRangeTheme = colorRange.&capitalize
        def baseTheme = "${DLSResourceConstants.THEME_PREFIX}." + NameConversionHelper.removeHyphensAndCapitalize("${colorRangeTheme}")
        xml.resources() {
            xml.style("${DLSResourceConstants.ITEM_NAME}": "${baseTheme}") {
                5.step(95, 5) {
                    item("${DLSResourceConstants.ITEM_NAME}": "uidColorLevel${it}", "${Colors.getColorNameForXmlItem(colorRange, it.toString())}")
                }
            }

            //Add individual attributes
            DLSResourceConstants.TONAL_RANGES.each {
                def tonalRange = it.toString()
                def tonalRangeTheme = "${baseTheme}." + NameConversionHelper.removeHyphensAndCapitalize("${tonalRange}")
                xml.style("${DLSResourceConstants.ITEM_NAME}": "${tonalRangeTheme}") {
                    componentParser.controls.each {
                        def result
                        if (it.parent) {
                            Control control = it
                            it.parent.each {
                                result = getComponentsValues(control, it, brushParser, validationparser, colorRange, tonalRange)
                                if (AttributeManager.instance.isAttributeAllowed("${result[0]}")) {
                                    item(name: "${result[0]}", "${result[1]}")
                                    getMkp().comment("${result[2]}")
                                }
                            }
                        } else {
                            result = getComponentsValues(it, null, brushParser, validationparser, colorRange, tonalRange)
                            if (AttributeManager.instance.isAttributeAllowed("${result[0]}")) {
                                item(name: "${result[0]}", "${result[1]}")
                                getMkp().comment("${result[2]}")
                            }
                        }
                    }
                }
            }
        }
        def themeFile = new File(DLSResourceConstants.getThemeFilePath(colorRange))
        if (themeFile.exists()) {
            themeFile.delete()
        }

        themeFile.createNewFile()
        themeFile.write(writer.toString())
    }
}

private
def getComponentsValues(Control control, parent, brushParser, validationparser, colorRange, tonalRange) {
    def brushName = control.getBrushName()
    BrushValue brushValue = brushParser.getBrushValueFromBrushName(brushName, tonalRange)
    brushValue = validationparser.decorateBrush(brushValue, colorRange, tonalRange, brushName)
    def val = BrushValueEvaluator.getValue(brushValue, brushParser.getBrushes(), colorRange, tonalRange).toString()
    def controlName = NameConversionHelper.getControlAttributeName(control, parent).toString()

    //Update list of attributes
    AttributeManager.instance.addAtrribute(controlName, brushValue)

    //Update NavigationItems
    addNavigationItems(tonalRange, controlName, control.component, val)

    if(val == null || val == "@null") {
        println("invalid brush ${colorRange}:${tonalRange}:${brushName}  ${NameConversionHelper.getControlName(control, parent)}")
    }

    return [controlName, val, brushName]
}

def flushAllAttributes() {
    File attrsFile = new File(DLSResourceConstants.PATH_OUT_ATTRS_FILE)
    if (attrsFile.exists())
        attrsFile.delete()

    attrsFile.createNewFile()

    def writer = new StringWriter()
    def attrsXML = new MarkupBuilder(writer)
    attrsXML.setDoubleQuotes(true)
    attrsXML.resources() {
        attrsXML."${DLSResourceConstants.THEME_DECLARED_STYLEABLE}"("${DLSResourceConstants.ITEM_NAME}": DLSResourceConstants.THEME_DECLARED_ID) {
            AttributeManager.instance.getAttributesList().each {
                attr(name: "${it.name}", format: "${it.refType}")
            }
        }
    }
    attrsFile.write(writer.toString())
}

def addNavigationItems(tonalRange, controlName, component, value) {
    if (component == "navigation") {
        def navAttribute = new NavigationAttribute(tonalRange: tonalRange, componentName: controlName, value: value)
        NavigationStylesGenerator.instance.addNavigationAttribute(navAttribute)
    }
}