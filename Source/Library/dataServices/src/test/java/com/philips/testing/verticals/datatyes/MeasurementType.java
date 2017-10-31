package com.philips.testing.verticals.datatyes;

import java.util.ArrayList;
import java.util.List;

public class MeasurementType {
    public static final String UNKNOWN = "UNKNOWN";
    public static final String TEMPERATURE = "TEMPERATURE";
    public static final String AMOUNT = "AMOUNT";
    public static final String WEIGHT = "WEIGHT";
    public static final String DURATION = "DURATION";
    public static final String RELATIVE_HUMIDITY = "RELATIVE_HUMIDITY";
    public static final String LENGTH = "LENGTH";
    public static final String DST ="dst";
    public static final String TST ="tst";
    public static final String DST_SCORE ="dstScore";


    public static int getIDFromDescription(String description) {
        if (description == null) {
            return -1;
        }
        switch (description.toUpperCase()) {
            case UNKNOWN:
                return -1;
            case TEMPERATURE:
                return 41;
            case AMOUNT:
                return 42;
            case WEIGHT:
                return 43;
            case DURATION:
                return 44;
            case RELATIVE_HUMIDITY:
                return 45;
            case LENGTH:
                return 46;
            case DST:
                return 47;
            case TST:
                return 48;
            case DST_SCORE:
                return 49;
            default:
                return -1;
        }
    }

    public static String getUnitFromDescription(String description) {
        if (description == null) {
            return "default";
        }
        switch (description.toUpperCase()) {
            case UNKNOWN:
                return "UnknownUnit";
            case TEMPERATURE:
                return "\u2103"; /** Unicode of Degree Celsius */
            case AMOUNT:
                return "ml";
            case WEIGHT:
                return "kg";
            case DURATION:
                return "seconds";
            case RELATIVE_HUMIDITY:
                return "RelativeHumidity";
            case LENGTH:
                return "cm";
            case DST:
                return "ms";
            case TST:
                return "ms";
            case DST_SCORE:
                return "percentage";


            default:
                return "UnknownUnit";
        }
    }

    public static String getDescriptionFromID(int ID) {
        switch (ID) {

            case -1:
                return UNKNOWN;
            case 41:
                return TEMPERATURE;
            case 42:
                return AMOUNT;
            case 43:
                return WEIGHT;
            case 44:
                return DURATION;
            case 45:
                return RELATIVE_HUMIDITY;
            case 46:
                return LENGTH;
            case 47:
                return DST;
            case 48:
                return TST;
            case 49:
                return DST_SCORE;

            default:
                return UNKNOWN;
        }
    }

    public static List<String> getMeasurementTypes() {
        List<String> measurementType = new ArrayList<>();
        measurementType.add(UNKNOWN);
        measurementType.add(TEMPERATURE);
        measurementType.add(AMOUNT);
        measurementType.add(WEIGHT);
        measurementType.add(DURATION);
        measurementType.add(RELATIVE_HUMIDITY);
        measurementType.add(LENGTH);
        measurementType.add(DST);
        measurementType.add(TST);
        measurementType.add(DST_SCORE);
        return measurementType;
    }
}
