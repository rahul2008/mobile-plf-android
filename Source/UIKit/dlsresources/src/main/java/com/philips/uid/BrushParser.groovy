package com.philips.uid

import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

def jsonSlurper = new JsonSlurper()
def brushObject = jsonSlurper.parseText(new File("../res/semantic_brushes_generated.json").text)

def allAttributes = new ArrayList()

brushObject.each {
    key, value ->
        def themeAttr = new ThemeAttribute(key)
        value.each {
            entry ->
                def name = entry.key
                def colorNumber = entry.value.get("colorNumber")
                def alpha = entry.value.get("alpha")
                def reference = entry.value.get("reference")
                def rangeName = entry.value.get("rangeName")
                themeAttr.addTonalRange(name, colorNumber, alpha, reference, rangeName)
        }
        allAttributes.add(themeAttr)
}

flushAttrsFile(allAttributes)

def flushAttrsFile(attrList) {
    def sw = new StringWriter()
    def xml = new MarkupBuilder(sw)
    xml.setDoubleQuotes(true)
    xml.resources() {
        xml.'declare-styleable'(name: "PhilipsUID") {

            attrList.each {
                def attrName = "uid" + it.attrName.split("-").collect { it.capitalize() }.join("")
                attr(name: attrName, format: "reference|color")
            }
        }
    }

    def attrFile = new File("../out/uid_attrs.xml")
    if (attrFile.exists()) {
        attrFile.delete()
    }

    attrFile.createNewFile();
    attrFile.write(sw.toString())
}