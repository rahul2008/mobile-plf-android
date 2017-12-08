package com.philips.uid

import com.philips.uid.helpers.NameConversionHelper
import com.philips.uid.model.navigation.NavigationAttribute
import groovy.xml.MarkupBuilder

@Singleton
class NavigationStylesGenerator {
    List<NavigationAttribute> navigationList = new ArrayList<>()

    def addNavigationAttribute(navAttribute) {
        if (!navigationList.contains(navAttribute)) {
            navigationList.add(navAttribute)
        }
    }

    void generateNavigationStyles() {
        def writer = new StringWriter()
        def colorXML = new MarkupBuilder(writer)
        colorXML.setDoubleQuotes(true)
        colorXML.resources() {
            DLSResourceConstants.TONAL_RANGES.each {
                def tonalRange = it.toString()
                def themeTonalRange = NameConversionHelper.removeHyphensAndCapitalize("$it")
                colorXML.style("${DLSResourceConstants.ITEM_NAME}": "UIDNavigationbar" + themeTonalRange) {
                    item("${DLSResourceConstants.ITEM_NAME}": "uidNavigationRange", "${themeTonalRange}")
                    navigationList.findAll { it.tonalRange == tonalRange }.each {
                        item("${DLSResourceConstants.ITEM_NAME}": it.componentName, it.getColorValue())
                    }
                }
            }
        }

        def colorFile = new File(DLSResourceConstants.PATH_OUT_NAVIGATION_FILE)
        if (colorFile.exists()) {
            colorFile.delete()
        }

        colorFile.createNewFile()
        colorFile.write(writer.toString())
    }
}
