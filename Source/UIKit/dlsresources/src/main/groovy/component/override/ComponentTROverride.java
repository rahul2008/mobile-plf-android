package component.overloading;

import java.util.HashMap;

public class Overload {
    String name;
    HashMap<String, String> overrideMap = new HashMap<>();

    public Overload(String name) {
        this.name = name;
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
}