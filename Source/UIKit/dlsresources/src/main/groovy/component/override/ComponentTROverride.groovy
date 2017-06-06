package component.override

import com.philips.uid.BrushParser
import com.philips.uid.DLSResourceConstants

public class ComponentTROverride {
    String name
    String brush
    HashMap<String, String> overrideMap = new HashMap<>();

    public ComponentTROverride(String name, String brush) {
        this.name = name
        this.brush = brush
        generateDefaultOverrides(DLSResourceConstants.TONAL_RANGES[0])
//        println( name + ":" + overrideMap)
    }

    public void putOverride(String origTonalRange, String overridenTonalRange) {
        overrideMap.put(origTonalRange, overridenTonalRange);
    }

    public String getOverridenTonalRange(String forTonalRange) {
        String result = forTonalRange;
        if (overrideMap.containsKey(forTonalRange)) {
            result = overrideMap.get(forTonalRange);
        }
        return result;
    }

    public void generateDefaultOverrides(String targetTonalRange) {
        DLSResourceConstants.TONAL_RANGES.each {
            overrideMap.put(BrushParser.getCapitalizedValue("$it"), BrushParser.getCapitalizedValue(targetTonalRange));
        }
    }

    @Override
    int hashCode() {
        return name.hashCode()
    }

    @Override
    boolean equals(Object o) {
        return name == (o)
    }

    @Override
    String toString() {
        return name
    }
}