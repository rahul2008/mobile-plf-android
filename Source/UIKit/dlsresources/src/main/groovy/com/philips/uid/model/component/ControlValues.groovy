package com.philips.uid.model.component

import com.philips.uid.attribute.AttributeManager

@Singleton
public class ControlValues {

    List<ControlValue> controlValueList = new ArrayList<>()

    def addControlValue(colorRange, tonalRange, control, uidFormattedName, finalValue) {
        if (AttributeManager.instance.isAttributeAllowed(uidFormattedName)) {
            controlValueList.add(new ControlValue(colorRange: colorRange, tonalRange: tonalRange, control: control,
                    uidFormattedName: uidFormattedName, finalValue: finalValue))
        }
    }

    static class ControlValue {
        def colorRange
        def tonalRange

        def uidFormattedName
        def finalValue

        Control control

        def isAccentControl() {
            control.getContext() == "accent"
        }

        def getComponent() {
            control.component
        }

        def getItem() {
            control.controlProperty.item
        }

        def getContext() {
            control.context
        }
    }

    def getControlValue(colorRange, tonalRange) {
        controlValueList.find { it.colorRange == it.colorRange && it.tonalRange == tonalRange }
    }
}