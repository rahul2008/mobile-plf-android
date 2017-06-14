/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid

import component.override.ComponentOverrideManager
import groovy.xml.MarkupBuilder

class ThemeGenerator {

    def invalidComponentRefList = new ArrayList()

    def createThemeXml(
            def allBrushAttributes, def allComponentAttributes, def dataValidationThemeValues) {
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        DLSResourceConstants.COLOR_RANGES.each {
            theme, colorName ->

                def writer = new StringWriter()
                def xml = new MarkupBuilder(writer)
                xml.setDoubleQuotes(true)

                xml.resources() {
                    buildColorAndComponentsMapping(xml, colorName, allBrushAttributes, allComponentAttributes)
                    DLSResourceConstants.TONAL_RANGES.each {
                        buildBrushesAttributes(xml, allBrushAttributes, it, colorName, colorsXmlInput, dataValidationThemeValues, allComponentAttributes)
                    }
                }
                def themeFile = new File(DLSResourceConstants.getThemeFilePath(colorName))
                if (themeFile.exists()) {
                    themeFile.delete()
                }

                themeFile.createNewFile()
                themeFile.write(writer.toString())
        }
        println("Invalid component reference for " + invalidComponentRefList.toSet())
    }

    def buildColorAndComponentsMapping(xml, colorName, allBrushAttributes, allComponentAttributes) {
        def baseTheme = "${DLSResourceConstants.THEME_PREFIX}." + new BrushParser().getCapitalizedValue("${colorName}")

        xml.style("${DLSResourceConstants.ITEM_NAME}": "${baseTheme}") {
            for (int colorLevel = 5; colorLevel <= 90;) {

                item("${DLSResourceConstants.ITEM_NAME}": BrushParser.getAttributeName("Color_Level_" + colorLevel),
                        "${DLSResourceConstants.COLOR_REFERENCE}${DLSResourceConstants.LIB_PREFIX}_${colorName}_${DLSResourceConstants.LEVEL}_" + colorLevel)
                colorLevel = colorLevel + DLSResourceConstants.COLOR_OFFSET
            }

            /*  def compMngr = ComponentOverrideManager.getManagerInstance()
              allComponentAttributes.each {
                  if (BrushParser.isSupportedAction(it.attrName) && !compMngr.overridesTR(it.attrName)) {
                      def reference = it.attributeMap.get(it.attrName).getAttributeValue(allBrushAttributes)
                      if (reference == "@null") {
                          invalidComponentRefList.add(it.attrName)
                      }
                      item("${DLSResourceConstants.ITEM_NAME}": it.attrName, reference)
                  }
              }*/
        }
    }

    def buildBrushesAttributes(xml, List allBrushAttributes, it, colorName, colorsXmlInput, dataValidationThemeValues, allComponentAttributes) {
        def defaultTonalRange = "$it".toString()
        def tonalRange = BrushParser.getCapitalizedValue("$it")
        def styleThemeName = "${DLSResourceConstants.THEME_PREFIX}." + BrushParser.getCapitalizedValue("${colorName}._${it}")

        def compMngr = ComponentOverrideManager.getManagerInstance()

        DataValidation dataValidation = new DataValidation();
        xml.style("${DLSResourceConstants.ITEM_NAME}": "${styleThemeName}") {

            /*  allBrushAttributes.each {
                  def tr = tonalRange.toString()
                  def value = "null"
                  TonalRange themeValue = null;
                  if (it.attributeMap.containsKey(tr)) {
                      themeValue = it.attributeMap.get(tr).clone()
                      dataValidation.decorateValidations(themeValue, dataValidationThemeValues, it.attrName, colorName, tr)
                      value = themeValue.getValue("${colorName}", colorsXmlInput, allBrushAttributes)
                  }
                  if (value == "@null") {
                      println(" Invalid combination Tonal Range " + tr + " Brush " + it.attrName + " Theme values " + themeValue.toString())
                  }
                  if (BrushParser.isSupportedAction(it.attrName)) {
                      item("${DLSResourceConstants.ITEM_NAME}": it.attrName, value)
                      if (it.attrName.contains("Accent")) {
                          if (themeValue.opacity != null) {
                              item("${DLSResourceConstants.ITEM_NAME}": it.attrName + "Alpha", getDimenName(themeValue.opacity))
                          }

                      }
                  }
              }*/
            //Add overridden component attributes
            /* ComponentOverrideManager.getManagerInstance().overrideList.each {
                 def brush = it.brush
                 def compName = it.name
                 def value = "null"
                 def newTonalRange = it.getOverridenTonalRange(tonalRange.toString())
                 //Search for the brush from list
                 allBrushAttributes.each {
                     if(it.attrName == brush) {
                         TonalRange themeValue =  it.attributeMap.get(newTonalRange).clone();
                         dataValidation.decorateValidations(themeValue, dataValidationThemeValues, it.attrName, colorName, newTonalRange)
                         value = themeValue.getValue("${colorName}", colorsXmlInput, allBrushAttributes)
                         item("${DLSResourceConstants.ITEM_NAME}": compName, value)
                     }
                 }
             }*/
            allComponentAttributes.each {
                def compName = it.attrName
                def value = "null"
                def attrsList = new ArrayList();
                def newTonalRange = tonalRange
                if (BrushParser.isSupportedAction(it.attrName)) {
                    def reference = it.attributeMap.get(it.attrName).getAttributeValue(allBrushAttributes)
                    def brushName = reference.substring(reference.indexOf("/") + 1)
//
                    //Take for overriden colors like Dialogs
                    if(compMngr.overridesTR(it.attrName)) {
                        newTonalRange = compMngr.getOverridenTonalRange("${it.attrName}",tonalRange)
                    }

                    allBrushAttributes.each {
                        if (it.attrName == brushName) {
//                            println(it.attrName + " " + brushName)
                            TonalRange themeValue = it.attributeMap.get(newTonalRange).clone();
                            dataValidation.decorateValidations(themeValue, dataValidationThemeValues, it.attrName, colorName, newTonalRange)
                            value = themeValue.getValue("${colorName}", colorsXmlInput, allBrushAttributes)
                            if (!attrsList.contains(compName)) {
                                item("${DLSResourceConstants.ITEM_NAME}": compName, value)
                            }
                        }
//                    item("${DLSResourceConstants.ITEM_NAME}": it.attrName, reference)
                    }

                }
            }
        }
    }

    def getDimenName(opacity) {
        opacity = (int) Math.round((Float.valueOf(opacity) * 100));
        return "@dimen/uid_alpha_per_" + opacity
    }
}