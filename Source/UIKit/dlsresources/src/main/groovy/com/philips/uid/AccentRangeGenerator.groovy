/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

import com.philips.uid.DLSResourceConstants
import groovy.xml.MarkupBuilder

class AccentRangeGenerator {

    private void generateAccentRanges() {

        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.setDoubleQuotes(true)
        xml.resources() {

            DLSResourceConstants.COLOR_RANGES.each {
                theme, colorName ->
                    xml.style("${DLSResourceConstants.ITEM_NAME}": "Accent" + theme) {

                        for (int colorLevel = 5; colorLevel <= 90;) {
                            def colorValue = getColorValue(colorsXmlInput, getColorName(colorLevel, colorName))
                            item("${DLSResourceConstants.ITEM_NAME}": "${DLSResourceConstants.LIB_PREFIX}" + "AccentLevel" + colorLevel, colorValue)
                            colorLevel = colorLevel + DLSResourceConstants.COLOR_OFFSET
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

    private String getColorName(int colorLevel, def colorName) {
        return "${DLSResourceConstants.LIB_PREFIX}_" + colorName + "_level_" + colorLevel
    }

    public def getColorValue(colorsXmlInput, colorReference) {
        try {
            return colorsXmlInput.findAll {
                it.@name == colorReference
            }*.text().get(0)
        } catch (IndexOutOfBoundsException exception) {
//            println("invalid colorCode with colorName: " + colorReference)
            return "@null"
        }
    }

}