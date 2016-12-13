package com.philips.uid

import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

def jsonSlurper = new JsonSlurper()
def brushesMap = jsonSlurper.parseText(new File(DLSResourceConstants.PATH_SEMANTIC_BRUSH_JSON).text)

def allAttributes = new ArrayList()

brushesMap.each {
    key, value ->
        def joinedKey = "${DLSResourceConstants.LIB_PREFIX}" + key.split("-").collect { it.capitalize() }.join("")
        def themeAttr = new ThemeAttribute(joinedKey)
        value.each {
            entry ->
                def name = entry.key
                def colorNumber = entry.value.get(DLSResourceConstants.JSON_KEY_COLOR_NUMBER)
                def alpha = entry.value.get(DLSResourceConstants.JSON_KEY_ALPHA)
                def reference = entry.value.get(DLSResourceConstants.JSON_KEY_REFERENCE)
                if (reference != null) {
                    reference = "${DLSResourceConstants.LIB_PREFIX}${entry.value.get(DLSResourceConstants.JSON_KEY_REFERENCE).split("-").collect { it.capitalize() }.join("")}"
                }
                def rangeName = entry.value.get(DLSResourceConstants.JSON_KEY_RANGE_NAME)
                themeAttr.addTonalRange(name, colorNumber, alpha, reference, rangeName)
        }
        allAttributes.add(themeAttr)
}

flushAttrsFile(allAttributes)
createBlueDarkStyle(allAttributes)

def flushAttrsFile(attrList) {
    def sw = new StringWriter()
    def xml = new MarkupBuilder(sw)
    xml.setDoubleQuotes(true)
    xml.resources() {
        xml.'declare-styleable'(name: "PhilipsUID") {

            attrList.each {
                attr(name: it.attrName, format: "reference|color")
            }
        }
    }

    def attrFile = new File("${DLSResourceConstants.PROJECT_BASE_PATH}/out/uid_attrs.xml")
    if (attrFile.exists()) {
        attrFile.delete()
    }

    attrFile.createNewFile()
    attrFile.write(sw.toString())
}

def createBlueDarkStyle(allAttributes) {
    def colorsXmlInput = new XmlParser().parseText(new File("../out/uid_colors.xml").text)
    DLSResourceConstants.COLOR_RANGES.each {
        theme, color_name ->

            def sw = new StringWriter()
            def xml = new MarkupBuilder(sw)
            xml.setDoubleQuotes(true)

            xml.resources() {
                DLSResourceConstants.TONAL_RANGES.each {
                    def tonalRange = "$it"
                    xml.style(name: "Theme.Philips.${theme}${it}") {

                        allAttributes.each {
                            def tr = tonalRange.toString()
                            if (it.attributeMap.containsKey(tr)) {
                                item(name: it.attrName, it.attributeMap.get(tr).getValue("${color_name}", colorsXmlInput))
                            }
                        }
                    }

                }
            }
            def themeFile = new File(DLSResourceConstants.getThemeFilePath(color_name))
            if (themeFile.exists()) {
                themeFile.delete()
            }

            themeFile.createNewFile()
            themeFile.write(sw.toString())
    }
}