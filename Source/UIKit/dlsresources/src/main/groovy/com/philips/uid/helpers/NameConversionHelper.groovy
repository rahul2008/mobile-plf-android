package com.philips.uid.helpers

import static org.junit.Assert.assertNotNull

class NameConversionHelper {
    static def getControlAttributeName(control, parent = null) {
        assertNotNull(control)
        def result = removeHyphensAndCapitalize(parent ?: "")
        return result
                .concat(removeHyphensAndCapitalize(control.component))
                .concat(removeHyphensAndCapitalize(control.getContext()))
                .concat(control.getState().sum { it.capitalize() } ?: "")
                .concat(removeHyphensAndCapitalize(control.getControlProperty().item))
                .concat(removeHyphensAndCapitalize(control.getControlProperty().type))
    }

    static def removeHyphensAndCapitalize(string) {
        string.split("-").collect { it.capitalize() }.join("")
    }

    static def replaceHyphenWithUnderScores(string) {
        string.replaceAll("-", "_")
    }
}