package com.philips.uid.parser

import com.philips.uid.DLSResourceConstants
import com.philips.uid.helpers.NameConversionHelper
import com.philips.uid.model.color.ColorRange
import com.philips.uid.model.color.Colors
import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

import static groovy.util.GroovyTestCase.assertEquals

class ColorParser {
    ColorParser() {
        initColors()
        assertValues()
    }

    def initColors() {
        def colorsJSON = new JsonSlurper().parseText(readColorRangesJSON())[0]
        colorsJSON.each {
            ColorRange range = new ColorRange()
            range.colorName = it.key
            it.each {
                it.value.each {
                    range.colorValueMap.put(it.key, it.value.toString())
                }
            }
            Colors.colorRangeMap.put(it.key, range)
        }
    }

    private void assertValues() {
        assertEquals("#061316", Colors.instance.getColorForRange("aqua", "90"))
        assertEquals("#191D4F", Colors.instance.getColorForRange("group-blue", "85"))
    }

    def readColorRangesJSON() {
        new File(DLSResourceConstants.PATH_COLOR_RANGES_JSON).text
    }

    def generateColorsXML() {
        def outDir = new File(DLSResourceConstants.PATH_OUT)
        if(outDir.exists()) {
            outDir.delete()
        }
        outDir.mkdir()

        File colorXml = new File(DLSResourceConstants.PATH_OUT_COLORS_FILE)
        if(colorXml.exists())
            colorXml.delete()

        colorXml.createNewFile()

        def writer = new StringWriter()
        def colorXML = new MarkupBuilder(writer)
        colorXML.setDoubleQuotes(true)
        colorXML.resources() {
            Colors.instance.colorRangeMap.each {
                it.value.each {
                    def colorName = NameConversionHelper.replaceHyphenWithUnderScores(it.colorName).toLowerCase()
                    it.colorValueMap.each {
                        def code = NameConversionHelper.replaceHyphenWithUnderScores(it.key).toLowerCase()
                        def level = it.key.number ? "level_": ""
                        color("name":"${DLSResourceConstants.LIB_PREFIX}_${colorName}_${level}${code}", it.value)
                    }
                }
            }
            //Inject white and black colors
            color("name":"uid_level_white", "#FFFFFF")
            color("name":"uid_level_black", "#000000")
        }

        colorXml.write(writer.toString())
    }
}