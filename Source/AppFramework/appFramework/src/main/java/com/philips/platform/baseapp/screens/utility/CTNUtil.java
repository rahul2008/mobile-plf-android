/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.utility;

import android.util.Log;

/**
 * Created by admin on 07/08/17.
 */

public class CTNUtil {

    public static String getCtnForCountry(String country) {
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
