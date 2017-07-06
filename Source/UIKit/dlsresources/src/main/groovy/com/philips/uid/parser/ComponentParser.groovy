package com.philips.uid.parser

import com.google.gson.Gson
import com.philips.uid.DLSResourceConstants
import com.philips.uid.model.component.Control

class ComponentParser {
    Control[] controls

    ComponentParser(Gson gson) {
        initComponents()
    }

    def initComponents() {
        Gson gson = new Gson()
        controls = gson.fromJson(readComponentsJSON(), Control[].class)
    }

    def readComponentsJSON() {
        new File(DLSResourceConstants.PATH_COMPONENT_JSON).text
    }
}