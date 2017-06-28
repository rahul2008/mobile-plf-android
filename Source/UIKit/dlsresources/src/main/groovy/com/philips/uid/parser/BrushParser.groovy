package com.philips.uid.parser

import com.google.gson.Gson
import com.philips.uid.model.brush.Brush
import com.philips.uid.model.brush.BrushValue
import groovy.json.JsonSlurper

class BrushParser {
    List<Brush> brushes = new ArrayList<>()

    BrushParser() {
        createBrushes()
    }

    def createBrushes() {
        new JsonSlurper().parseText(readBrushesJSON()).each {
            //Brush name
            it.each {
                Brush brush = new Brush();
                brush.brushName = it.key
                //Maps of tonal ranges
                it.value.each {
                    brush.brushValueMap.put(it.key.toString(), new Gson().fromJson(it.value.toString(), BrushValue.class))
                }
                brushes.add(brush)
            }
        }
    }

    def readBrushesJSON() {
        new File("../../../../resources/brushes.json").text
    }

    def getBrushValueFromBrushName(brsuhName, tonalRange) {
        brushes.find{it.brushName == brsuhName}.brushValueMap.get(tonalRange)
    }
}