/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid

import groovy.json.JsonSlurper
import org.junit.Before
import org.junit.Test

class DataValidationTest extends GroovyTestCase {
    DataValidation dataValidation;
    def datavalidationMap

    @Before
    public void setUp() throws Exception {
        def jsonSlurper = new JsonSlurper();
        datavalidationMap = jsonSlurper.parseText(new File(DLSResourceConstants.PATH_VALIDATION_JSON).text)

        dataValidation = new DataValidation();
    }

    @Test
    void testReturnedThemeValueList() {
        def allAttributes = dataValidation.getAllAttributes(datavalidationMap)
        GroovyTestCase.assertEquals(DLSResourceConstants.COLOR_RANGES.size(), allAttributes.size())
    }

    @Test
    void testReturnedThemeValueItems() {
        def allAttributes = dataValidation.getAllAttributes(datavalidationMap)
        DLSResourceConstants.COLOR_RANGES.each {
            colorRange ->
                def value = allAttributes.get(BrushParser.getCapitalizedValue(colorRange.value))
                assertNotNull(value)
                assertEquals(4, value.size())
        }
    }
}