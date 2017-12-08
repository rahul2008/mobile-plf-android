/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid

import com.philips.uid.helpers.NameConversionHelper
import com.philips.uid.model.component.ControlValues
import groovy.xml.MarkupBuilder

@Singleton
class NavigationTopThemeGenerator {
    final def NAVIGATION_BACKGROUND_TOP = "background-top"
    def generate() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.setDoubleQuotes(true)

        xml.resources() {
            DLSResourceConstants.COLOR_RANGES.each {
                def colorRange = it.value
                def colorRangeXMLName = it.key
                DLSResourceConstants.TONAL_RANGES.each {
                    def tonalRange = "${it}"
                    def tonalRangeXMLName = NameConversionHelper.removeHyphensAndCapitalize(tonalRange)
                    xml.style("${DLSResourceConstants.ITEM_NAME}": "Navigation${colorRangeXMLName}${tonalRangeXMLName}TopTheme") {
                        ControlValues.instance.controlValueList.findAll {
                            (it.item  == NAVIGATION_BACKGROUND_TOP && it.tonalRange == tonalRange && it.colorRange == colorRange)
                        }.each {
                            item(name: "${it.uidFormattedName}", "${it.finalValue}")
                        }
                    }
                }
            }

        }
        def whiteThemePath = new File(DLSResourceConstants.getNavigationTopFilePath())
        if (whiteThemePath.exists()) {
            whiteThemePath.delete()
        }

        whiteThemePath.createNewFile()
        whiteThemePath.write(writer.toString())
    }
}