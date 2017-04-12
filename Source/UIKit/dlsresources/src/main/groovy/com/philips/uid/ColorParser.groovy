/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid

import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

class ColorParser {

    public static void main(String[] args) {
        generateColors()
    }

    static void generateColors() {
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
                            def lowerCaseColorKey = colorRange.replaceAll("${DLSResourceConstants.HIPHEN}", "${DLSResourceConstants.UNDERSCORE}").toLowerCase()
                            def lowerCaseColorLevel = colorLevel.replaceAll("${DLSResourceConstants.HIPHEN}", "${DLSResourceConstants.UNDERSCORE}").toLowerCase()
                            if (lowerCaseColorKey == lowerCaseColorLevel) {
                                color("${DLSResourceConstants.ITEM_NAME}": "${DLSResourceConstants.LIB_PREFIX}_${lowerCaseColorKey}", "${colorCode}")
                            } else {
                                color("${DLSResourceConstants.ITEM_NAME}": "${DLSResourceConstants.LIB_PREFIX}_${lowerCaseColorKey}_${DLSResourceConstants.LEVEL}_${lowerCaseColorLevel}", "${colorCode}")
                            }
                    }
            }
            color("${DLSResourceConstants.ITEM_NAME}": "${DLSResourceConstants.LIB_PREFIX}_level_white", "#FFFFFF")
            color("${DLSResourceConstants.ITEM_NAME}": "${DLSResourceConstants.LIB_PREFIX}_level_black", "#000000")

        }
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
        colorFile.write(writer.toString())
    }
}