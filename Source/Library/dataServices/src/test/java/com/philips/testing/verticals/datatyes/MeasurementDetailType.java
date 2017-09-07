package com.philips.testing.verticals.datatyes;

import java.util.ArrayList;
import java.util.List;

public class MeasurementDetailType {

    public static final String UNKNOWN = "UNKNOWN";
    public static final String LOCATION = "LOCATION";
    public static final String BODY_PART = "BODY_PART";


    public static int getIDFromDescription(String description) {
        if (description == null) {
            return -1;
        }
        switch (description.toUpperCase()) {
            case UNKNOWN:
                return -1;
            case LOCATION:
                return 75;
            case BODY_PART:
                return 70;
            default:
                return -1;
        }
    }

    public static String getDescriptionFromID(int ID) {
        switch (ID) {

            case -1:
                return UNKNOWN;
            case 75:
                return LOCATION;
            case 70:
                return BODY_PART;
            default:
                return UNKNOWN;
        }
    }

    public static List<String> getMeasurementDetailTypes() {
        List<String> measurementDetailType = new ArrayList<>();
        measurementDetailType.add(UNKNOWN);
        measurementDetailType.add(LOCATION);
        measurementDetailType.add(BODY_PART);
        return measurementDetailType;
    }
}
