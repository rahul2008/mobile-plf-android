package com.philips.platform.baseapp.screens.utility;

import android.content.Context;

/**
 * Created by admin on 07/08/17.
 */

public class CTNUtil {

    public static String getCtnForCountry(String country) {
        switch (country) {
            case "HK":
                return "HX6322/04";
            case "MO":
                return "HX6322/04";
            case "IN":
                return "HX6311/07";
            case "US":
                return "HX6321/02";
            case "CN":
                return "HX6721/33";
            default:
                return "HX6064/33";
        }
    }
}
