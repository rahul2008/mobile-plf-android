/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.utility;

public class CTNUtil {

    private static final String TAG = CTNUtil.class.getSimpleName();

    public static String[] getCtnForCountry(String country) {
        RALog.d(TAG, "country : "+country);
        if (country == null) {
            return new String[]{"HX6064/33"};
        }
        switch (country) {
            case "HK":
                return new String[]{"HX6322/04"};
            case "MO":
                return new String[]{"HX6322/04"};
            case "IN":
                return new String[]{"HX6311/07"};
            case "US":
                return new String[]{
                        "HD8645/47",
                        "HD9980/20",
                        "HX8918/10",
                        "HD9240/94",
                        "SCF782/10",
                        "HX8332/11",
                        "SCF251/03",
                        "DIS359/03",
                        "DIS362/03",
                        "CA6702/00",
                        "CA6700/47",
                        "SCD393/03",
                        "BRE394/60",
                        "DIS363/03",
                        "DIS364/03"};
            case "CN":
                return new String[]{"HX6721/33"};
            default:
                return new String[]{"HX6064/33"};
        }
    }
}
