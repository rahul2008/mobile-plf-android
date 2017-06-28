package com.philips.uid

import com.philips.uid.attribute.AttributeManager
import com.philips.uid.helpers.BrushValueEvaluator
import com.philips.uid.helpers.NameConversionHelper
import com.philips.uid.model.brush.BrushValue
import com.philips.uid.model.color.Colors
import com.philips.uid.model.component.Control
import com.philips.uid.parser.BrushParser
import com.philips.uid.parser.ColorParser
import com.philips.uid.parser.ComponentParser
import com.philips.uid.parser.ValidationParser
import groovy.xml.MarkupBuilder

//Generate Colors
ColorParser colorParser = new ColorParser()
colorParser.generateColors()

//Generate Brushes
BrushParser brushParser = new BrushParser()

//Generate components
ComponentParser componentParser = new ComponentParser()

//Generate validation parser
ValidationParser validationParser = new ValidationParser()

//Add color and accent attributes
Colors.addColorAttributes()
Colors.addAccentColorAttributes()

//Generate and write component files
generateTheme(componentParser, brushParser, validationParser)

//flush all the attributes
flushAllAttributes()

def generateTheme(ComponentParser componentParser, BrushParser brushParser, ValidationParser validationparser) {

    DLSResourceConstants.COLOR_RANGES.each {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.setDoubleQuotes(true)

        def colorRange = it.key
        def baseTheme = "${DLSResourceConstants.THEME_PREFIX}.${colorRange}"
        xml.resources() {
            DLSResourceConstants.TONAL_RANGES.each {
                def tonalRange = "${it}"
                def tonalRangeTheme = "${baseTheme}.${tonalRange}"
                xml.style("${DLSResourceConstants.ITEM_NAME}": "${tonalRangeTheme}") {
                    componentParser.controls.each {
                        def result
                        if (it.parent) {
                            Control control = it
                            it.parent.each {
                                result = getComponentsValues(control, it, brushParser, validationparser, colorRange, tonalRange)
                                item(name: "${result[0]}", "${result[1]}")
                                getMkp().comment("${result[2]}")
                            }
                        } else {
                            result = getComponentsValues(it, null, brushParser, validationparser, colorRange, tonalRange)
                            item(name: "${result[0]}", "${result[1]}")
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

//    File colorXml = new File("generated/uid_theme.xml")
//    if (colorXml.exists())
//        colorXml.delete()
//
//    colorXml.createNewFile()
//
//    def writer = new StringWriter()
//    def colorXML = new MarkupBuilder(writer)
//    colorXML.setDoubleQuotes(true)
//    colorXML.resources() {
//        componentParser.controls.each {
//            def result
//            if (it.parent) {
//                Control control = it
//                it.parent.each {
//                    result = getComponentsValues(control, it, componentParser, brushParser, validationparser)
//                    item(name: "${result[0]}", "${result[1]}")
//                    getMkp().comment("${result[2]}")
//                }
//            } else {
//                result = getComponentsValues(it, null, componentParser, brushParser, validationparser)
//                item(name: "${result[0]}", "${result[1]}")
//            }
//        }
//    }
//    colorXml.write(writer.toString())
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
        AttributeManager.instance.getAttributesList().each {
            attr(name: "${it.name}", format: "${it.refType}")
        }
    }
    attrsFile.write(writer.toString())
}