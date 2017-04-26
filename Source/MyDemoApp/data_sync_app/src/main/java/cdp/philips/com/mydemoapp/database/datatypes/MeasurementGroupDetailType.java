package cdp.philips.com.mydemoapp.database.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310218660 on 11/17/2016.
 */

public class MeasurementGroupDetailType {
    public static final String UNKNOWN = "UNKNOWN";
    public static final String TEMP_OF_DAY = "TEMP_OF_DAY";


    public static int getIDFromDescription(String description) {
        if(description == null){
            return -1;
        }

        if (description.equalsIgnoreCase(TEMP_OF_DAY)) {
            return 77;
        } else {
            return -1;
        }
    }

    public static String getDescriptionFromID(int ID) {
        switch (ID) {
            case -1:
                return UNKNOWN;
            case 46:
                return TEMP_OF_DAY;
            default:
                return UNKNOWN;
        }
    }

    public static List<String> getMeasurementGroupDetailType() {
        List<String> measurementGroupDetailType = new ArrayList<>();
        measurementGroupDetailType.add(UNKNOWN);
        measurementGroupDetailType.add(TEMP_OF_DAY);
        return measurementGroupDetailType;
    }
}
