package com.philips.uid

import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

parseColorsAndCreateColorsXml()

void parseColorsAndCreateColorsXml() {
    def jsonSlurper = new JsonSlurper()

    def colorRangeMappings = jsonSlurper.parseText(new File(DLSResourceConstants.PATH_COLOR_RANGES_JSON).text)

    def writer = new StringWriter()
    def colorXML = new MarkupBuilder(writer)
    colorXML.setDoubleQuotes(true)
    colorXML.resources() {
        colorRangeMappings.each {
            colorRange, colorLevelMap ->
                colorLevelMap.each {
                    colorLevel, colorCode ->
                        def lowerCaseColorKey = colorRange.replaceAll("-", "_").toLowerCase()
                        def lowerCaseColorLevel = colorLevel.replaceAll("-", "_").toLowerCase()
                        if (lowerCaseColorKey == lowerCaseColorLevel) {
                            color(name: "${DLSResourceConstants.LIB_PREFIX}_${lowerCaseColorKey}", "${colorCode}")
                        } else {
                            color(name: "${DLSResourceConstants.LIB_PREFIX}_${lowerCaseColorKey}_${lowerCaseColorLevel}", "${colorCode}")
                        }
                }
        }
    }
    createColorsXml(writer)
}

private void createColorsXml(StringWriter sw) {
    def outDir = new File(DLSResourceConstants.PATH_OUT)

    if (!outDir.exists()) {
        outDir.mkdir()
    } else {
        outDir.delete()
        outDir.mkdir()
    }

    def colorFile = new File(DLSResourceConstants.PATH_OUT_COLORS_FILE)
    if (colorFile.exists()) {
        colorFile.delete()
    }

    colorFile.createNewFile()
    colorFile.write(sw.toString())
}