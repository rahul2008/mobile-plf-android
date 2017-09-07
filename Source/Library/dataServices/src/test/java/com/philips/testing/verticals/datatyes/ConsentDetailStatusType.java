package com.philips.testing.verticals.datatyes;

import java.util.ArrayList;
import java.util.List;

public class ConsentDetailStatusType {
    public static final String UNKNOWN = "UNKNOWN";
    public static final String ACCEPTED = "ACCEPTED";
    public static final String REFUSED = "REFUSED";


    public static int getIDFromDescription(String description) {
        if (description == null) {
            return -1;
        }
        switch (description.toUpperCase()) {
            case UNKNOWN:
                return -1;
            case ACCEPTED:
                return 201;
            case REFUSED:
                return 202;
            default:
                return -1;
        }
    }

    public static String getDescriptionFromID(int ID) {
        switch (ID) {

            case -1:
                return UNKNOWN;
            case 201:
                return ACCEPTED;
            case 202:
                return REFUSED;
            default:
                return UNKNOWN;
        }
    }

    public static List<String> getConsentDetailStatusType() {
        List<String> consentDetailStatusType = new ArrayList<>();
        consentDetailStatusType.add(UNKNOWN);
        consentDetailStatusType.add(ACCEPTED);
        consentDetailStatusType.add(REFUSED);
        return consentDetailStatusType;
    }
}
