import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

public class BrushParser {

    public static void main(String[] args) {
        def jsonSlurper = new JsonSlurper()
        def brushesMap = jsonSlurper.parseText(new File(DLSResourceConstants.PATH_SEMANTIC_BRUSH_JSON).text)

        def allAttributes = new ArrayList()

        brushesMap.each {
            semanticName, themesMap ->
                def attributeName = "${DLSResourceConstants.LIB_PREFIX}" + semanticName.split("-").collect {
                    it.capitalize()
                }.join("")
                def themeAttr = new ThemeAttribute(attributeName)
                themesMap.each {
                    theme ->
                        def name = theme.key
                        def colorNumber = theme.value.get(DLSResourceConstants.JSON_KEY_COLOR_NUMBER)
                        def alpha = theme.value.get(DLSResourceConstants.JSON_KEY_ALPHA)
                        def reference = theme.value.get(DLSResourceConstants.JSON_KEY_REFERENCE)
                        if (reference != null) {
                            reference = "${DLSResourceConstants.LIB_PREFIX}${theme.value.get(DLSResourceConstants.JSON_KEY_REFERENCE).split("-").collect { it.capitalize() }.join("")}"
                        }
                        def rangeName = theme.value.get(DLSResourceConstants.JSON_KEY_RANGE_NAME)
                        themeAttr.addTonalRange(name, colorNumber, alpha, reference, rangeName)
                }
                allAttributes.add(themeAttr)
        }

        flushAttrsFile(allAttributes)
        createThemeXml(allAttributes)

    }

    private static void flushAttrsFile(attrList) {
        def sw = new StringWriter()
        def xml = new MarkupBuilder(sw)
        xml.setDoubleQuotes(true)
        xml.resources() {
            xml."${DLSResourceConstants.THEME_DECLARED_STYLEABLE}"("${DLSResourceConstants.ITEM_NAME}": DLSResourceConstants.THEME_DECLARED_ID) {

                attrList.each {
                    attr("${DLSResourceConstants.ITEM_NAME}": it.attrName, "${DLSResourceConstants.ITEM_FORMAT}": DLSResourceConstants.FORMAT_REF_OR_COLOR)
                }
            }
        }

        def attrFile = new File(DLSResourceConstants.PATH_OUT_ATTRS_FILE)
        if (attrFile.exists()) {
            attrFile.delete()
        }

        attrFile.createNewFile()
        attrFile.write(sw.toString())
    }

    private static void createThemeXml(allAttributes) {
        def colorsXmlInput = new XmlParser().parseText(new File(DLSResourceConstants.PATH_OUT_COLORS_FILE).text)
        DLSResourceConstants.COLOR_RANGES.each {
            theme, color_name ->

                def writer = new StringWriter()
                def xml = new MarkupBuilder(writer)
                xml.setDoubleQuotes(true)

                xml.resources() {
                    DLSResourceConstants.TONAL_RANGES.each {
                        def tonalRange = "$it"
                        xml.style("${DLSResourceConstants.ITEM_NAME}": "${DLSResourceConstants.THEME_PREFIX}.${theme}${it}") {

                            allAttributes.each {
                                def tr = tonalRange.toString()
                                if (it.attributeMap.containsKey(tr)) {
                                    item("${DLSResourceConstants.ITEM_NAME}": it.attrName, it.attributeMap.get(tr).getValue("${color_name}", colorsXmlInput))
                                }
                            }
                        }

                    }
                }
                def themeFile = new File(DLSResourceConstants.getThemeFilePath(color_name))
                if (themeFile.exists()) {
                    themeFile.delete()
                }

                themeFile.createNewFile()
                themeFile.write(writer.toString())
        }
    }
}