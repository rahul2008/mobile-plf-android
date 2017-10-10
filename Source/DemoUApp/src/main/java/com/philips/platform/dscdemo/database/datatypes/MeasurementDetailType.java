/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.dscdemo.database.datatypes;

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

        if (description.equalsIgnoreCase(LOCATION)) {
            return 75;
        } else {
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
