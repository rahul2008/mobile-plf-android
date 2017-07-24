/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid

import com.philips.uid.model.component.ControlValues
import groovy.xml.MarkupBuilder

@Singleton
class WhiteThemeGenerator {
    def whiteElements = ["text-box", "track", "content-item", "list-item", "scrollbar"]

    def generateWhiteTheme() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.setDoubleQuotes(true)

        xml.resources() {
            DLSResourceConstants.COLOR_RANGES.each {
                def colorRange = it.value
                def colorRangeXMLName = it.key
                xml.style("${DLSResourceConstants.ITEM_NAME}": "Popup${colorRangeXMLName}Theme") {
                    ControlValues.instance.controlValueList.findAll {
                        (whiteElements.contains(it.component) || whiteElements.contains(it.context)) &&
                                it.tonalRange == "ultra-light" && it.colorRange == colorRange
                    }.each {
                        item(name: "${it.uidFormattedName}", "${it.finalValue}")
                    }
                }
            }
        }
        def whiteThemePath = new File(DLSResourceConstants.getWhiteThemeFilePath())
        if (whiteThemePath.exists()) {
            whiteThemePath.delete()
        }

        whiteThemePath.createNewFile()
        whiteThemePath.write(writer.toString())
    }
}