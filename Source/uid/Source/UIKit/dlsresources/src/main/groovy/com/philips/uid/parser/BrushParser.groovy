package com.philips.uid.parser

import com.google.gson.Gson
import com.philips.uid.DLSResourceConstants
import com.philips.uid.model.brush.Brush
import com.philips.uid.model.brush.BrushValue
import groovy.json.JsonSlurper

class BrushParser {
    List<Brush> brushes = new ArrayList<>()

    BrushParser() {
        this(readBrushesJSON())
    }

    //Helps for testing
    BrushParser(jsonData) {
        createBrushes(jsonData)
    }

    def createBrushes(jsonData) {
        new JsonSlurper().parseText(jsonData).each {
            //Brush name
            it.each {
                Brush brush = new Brush();
                brush.brushName = it.key
                //Maps of tonal ranges
                it.value.each {
                    if(it.value.get("color")?.startsWith("#")) {
                        BrushValue brushValue = new BrushValue()
                        brushValue.color = it.value.get("color")
                        brushValue.colorCode = it.value.get("color-code")
                        brushValue.colorRange = it.value.get("color-range")
                        brushValue.opacity = it.value.get("opacity")
                        brushValue.reference = it.value.get("reference")
                        brushValue.offset = it.value.get("offset")
                        brush.brushValueMap.put(it.key.toString(), brushValue)
                    } else {
                        brush.brushValueMap.put(it.key.toString(), new Gson().fromJson(it.value.toString(), BrushValue.class))
                    }
                }
                brushes.add(brush)
            }
        }
    }

    static def readBrushesJSON() {
        new File(DLSResourceConstants.PATH_SEMANTIC_BRUSH_JSON).text
    }

    def getBrushValueFromBrushName(brushName, tonalRange) {
        brushes.find{it.brushName == brushName}?.brushValueMap?.get(tonalRange)
    }
}