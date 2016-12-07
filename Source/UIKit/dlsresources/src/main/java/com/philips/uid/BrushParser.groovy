package com.philips.uid

import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

def jsonSlurper = new JsonSlurper()
def brushObject = jsonSlurper.parseText(new File("../res/semantic_brushes_generated.json").text)

def allAttributes = new ArrayList()

brushObject.each {
    key, value ->
        def joinedKey = "uid" + key.split("-").collect { it.capitalize() }.join("")
        def themeAttr = new ThemeAttribute(joinedKey)
        value.each {
            entry ->
                def name = "uid" + entry.key.split("-").collect { it.capitalize() }.join("")
                def colorNumber = entry.value.get("colorNumber")
                def alpha = entry.value.get("alpha")
                def reference = entry.value.get("reference")
                if (reference != null) {
                    reference = "uid" + entry.value.get("reference").split("-").collect {
                        it.capitalize()
                    }.join("")
                }
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
                attr(name: it.attrName, format: "reference|color")
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