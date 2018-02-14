/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.pif.chi;

import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

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