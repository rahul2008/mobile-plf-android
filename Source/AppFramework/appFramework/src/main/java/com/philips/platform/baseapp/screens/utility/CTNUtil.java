/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.utility;

public class CTNUtil {

    private static final String TAG = CTNUtil.class.getSimpleName();

    public static String getCtnForCountry(String country) {
        RALog.d(TAG, "country : "+country);
        if (country == null) {
            return "HX6064/33";
        }
        switch (country) {
            case "HK":
                return "HX6322/04";
            case "MO":
                return "HX6322/04";
            case "IN":
                return "HX6311/07";
            case "US":
                return "HD8645/47";
            case "CN":
                return "HX6721/33";
            default:
                return "HX6064/33";
        }
    }
}
