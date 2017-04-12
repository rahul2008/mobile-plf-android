/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid

import groovy.xml.MarkupBuilder

class AttributeGenerator {

    def flushAttrsFile(allBrushAttributes, allComponentAttributes) {
        def sw = new StringWriter()
        def xml = new MarkupBuilder(sw)
        xml.setDoubleQuotes(true)
        xml.resources() {
            xml."${DLSResourceConstants.THEME_DECLARED_STYLEABLE}"("${DLSResourceConstants.ITEM_NAME}": DLSResourceConstants.THEME_DECLARED_ID) {

                for (int colorLevel = 5; colorLevel <= 90;) {
                    attr("${DLSResourceConstants.ITEM_NAME}": BrushParser.getAttributeName("Color_Level_" + colorLevel), "${DLSResourceConstants.ITEM_FORMAT}": DLSResourceConstants.FORMAT_REF_OR_COLOR)
                    colorLevel = colorLevel + DLSResourceConstants.COLOR_OFFSET

                }
                for (int colorLevel = 5; colorLevel <= 90;) {
                    attr("${DLSResourceConstants.ITEM_NAME}": BrushParser.getAttributeName("Accent_Level_" + colorLevel), "${DLSResourceConstants.ITEM_FORMAT}": DLSResourceConstants.FORMAT_REF_OR_COLOR)
                    colorLevel = colorLevel + DLSResourceConstants.COLOR_OFFSET

                }

                allBrushAttributes.each {
                    if (BrushParser.isSupportedAction(it.attrName)) {
                        attr("${DLSResourceConstants.ITEM_NAME}": it.attrName, "${DLSResourceConstants.ITEM_FORMAT}": DLSResourceConstants.FORMAT_REF_OR_COLOR)
                        if (it.attrName.contains("Accent")) {
                            attr("${DLSResourceConstants.ITEM_NAME}": it.attrName + "Alpha", "${DLSResourceConstants.ITEM_FORMAT}": DLSResourceConstants.FORMAT_REF_OR_COLOR)
                        }
                    }
                }

                allComponentAttributes.each {
                    if (BrushParser.isSupportedAction(it.attrName)) {
                        attr("${DLSResourceConstants.ITEM_NAME}": it.attrName, "${DLSResourceConstants.ITEM_FORMAT}": DLSResourceConstants.FORMAT_REF_OR_COLOR)
                    }
                }

            }
        }

        def attrFile = new File(DLSResourceConstants.PATH_OUT_ATTRS_FILE)
        if (attrFile.exists()) {
            attrFile.delete()
        }

        attrFile.createNewFile()
        attrFile.write(sw.toString())
    }


}
