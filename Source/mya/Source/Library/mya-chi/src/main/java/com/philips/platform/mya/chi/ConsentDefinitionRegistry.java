package com.philips.platform.mya.chi;

import com.philips.platform.mya.chi.datamodel.ConsentDefinition;

import java.util.ArrayList;
import java.util.List;

public class ConsentDefinitionRegistry {

    private static List<ConsentDefinition> consentDefinitions = new ArrayList<>();

    public static void add(ConsentDefinition consentDefinition) {
        consentDefinitions.add(consentDefinition);
    }

    public static ConsentDefinition getDefinitionByConsentType(String consentType) {
        for (ConsentDefinition cd : consentDefinitions) {
            if (cd.getTypes().contains(consentType)) {
                return cd;
            }
        }
        return null;
    }
}
