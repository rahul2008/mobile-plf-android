/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.mya.catk.exception;

import java.util.Collection;
import java.util.Set;

public class UnknownConsentType extends RuntimeException {

    public UnknownConsentType(String unknownType, Set<String> knownTypes) {
        super(buildMessage(unknownType, knownTypes));
    }

    private static String buildMessage(String unknowntype, Collection<String> knownTypes) {
        StringBuilder sB = new StringBuilder("unknown consent type: ");
        sB.append(unknowntype);
        sB.append(". Known types are:");
        for (String type : knownTypes) {
            sB.append(type);
            sB.append(",");
        }
        return sB.toString();
    }

}
