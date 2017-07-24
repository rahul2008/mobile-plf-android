package com.philips.uid

import com.philips.uid.helpers.NameConversionHelper
import com.philips.uid.model.color.Colors
import com.philips.uid.model.component.ControlValues
import groovy.xml.MarkupBuilder

/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

class AccentRangeGenerator {

    def generateAccentRanges() {
        DLSResourceConstants.COLOR_RANGES.each {
            def writer = new StringWriter()
            def xml = new MarkupBuilder(writer)
            xml.setDoubleQuotes(true)
            def colorRange = "${it.value}".toString()
            xml.resources() {
                def accentTheme = NameConversionHelper.removeHyphensAndCapitalize(colorRange)
                xml.style("${DLSResourceConstants.ITEM_NAME}": "Accent" + accentTheme) {
                    item("${DLSResourceConstants.ITEM_NAME}": "uidAccentRange", accentTheme)
                    5.step(95, 5) {
                        def colorValue = Colors.instance.getColorForRange(colorRange, "${it}")
                        item("${DLSResourceConstants.ITEM_NAME}": "${DLSResourceConstants.LIB_PREFIX}AccentLevel${it}", colorValue)
                    }
                }

                DLSResourceConstants.TONAL_RANGES.each {
                    def tonalRange = it.toString()
                    def tonalRangeTheme = "Accent${accentTheme}." + NameConversionHelper.removeHyphensAndCapitalize("${tonalRange}")
                    xml.style("${DLSResourceConstants.ITEM_NAME}": "${tonalRangeTheme}") {
                        ControlValues.instance.controlValueList.findAll {
                            it.isAccentControl() && it.colorRange == colorRange && it.tonalRange == tonalRange
                        }.each {
                            item(name: "${it.uidFormattedName}", "${it.finalValue}")
//                        getMkp().comment("${result[2]}")
                        }
                    }

                }
            }
            def accentFile = new File(DLSResourceConstants.getAccentFilePath(colorRange))
            if (accentFile.exists()) {
                accentFile.delete()
            }

            accentFile.createNewFile()
            accentFile.write(writer.toString())

        }

    }
}