package com.philips.uid

import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

parseColorsAndCreateColorsXml()

void parseColorsAndCreateColorsXml() {
    def jsonSlurper = new JsonSlurper()
    def obj = jsonSlurper.parseText(new File("../res/color_ranges.json").text)

    def sw = new StringWriter()
    def xml = new MarkupBuilder(sw)
    xml.setDoubleQuotes(true)
    xml.resources() {
        obj.each {
            key, value ->
                value.each {
                    level, clr ->
                        def newKey = key.replaceAll("-", "_").toLowerCase()
                        def newLevel = level.replaceAll("-", "_").toLowerCase()
                        if (newKey == newLevel) {
                            color(name: "uid_${newKey}", "${clr}")
                        } else {
                            color(name: "uid_${newKey}_${newLevel}", "${clr}")
                        }
                }
        }
    }
//    println sw
    createColorsXml(sw)
}

private void createColorsXml(StringWriter sw) {
    def colorFile = new File("../out/uid_colors.xml")
    if (colorFile.exists()) {
        colorFile.delete()
    }

    colorFile.createNewFile();
    colorFile.write(sw.toString())
}