package com.philips.uid

import groovy.json.JsonSlurper

def jsonSlurper = new JsonSlurper()
def brushObject = jsonSlurper.parseText(new File("semantic_brushes_generated.json").text)

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

println "hello"