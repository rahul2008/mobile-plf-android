package com.philips.uid

import com.philips.uid.helpers.NameConversionHelper

/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
import com.philips.uid.model.color.Colors
import groovy.xml.MarkupBuilder

class AccentRangeGenerator {

    def generateAccentRanges() {

        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.setDoubleQuotes(true)
        xml.resources() {
            DLSResourceConstants.COLOR_RANGES.each {
                def colorRange = "${it.value}".toString()
                def theme = NameConversionHelper.removeHyphensAndCapitalize(colorRange)
                xml.style("${DLSResourceConstants.ITEM_NAME}": "Accent" + theme) {
                    5.step(95, 5) {
                        def colorValue = Colors.instance.getColorForRange(colorRange, "${it}")
                        item("${DLSResourceConstants.ITEM_NAME}": "${DLSResourceConstants.LIB_PREFIX}AccentLevel${it}", colorValue)
                    }
                }
            }
        }
        def themeFile = new File(DLSResourceConstants.PATH_OUT_ACCENT_FILE)
        if (themeFile.exists()) {
            themeFile.delete()
        }

        themeFile.createNewFile()
        themeFile.write(writer.toString())
    }
}