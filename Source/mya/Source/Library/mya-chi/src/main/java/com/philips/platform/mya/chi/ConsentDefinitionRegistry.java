package com.philips.platform.mya.chi;

import com.philips.platform.mya.chi.datamodel.ConsentDefinition;

import java.util.HashMap;
import java.util.Map;

public class ConsentDefinitionRegistry {

    private static Map<String, ConsentDefinition> consents = new HashMap<>();

    public static void register(String identifier, ConsentDefinition consentDefinition) {
        consents.put(identifier, consentDefinition);
    }

    public static ConsentDefinition get(String identifier) {
        return consents.get(identifier);
    }
}
