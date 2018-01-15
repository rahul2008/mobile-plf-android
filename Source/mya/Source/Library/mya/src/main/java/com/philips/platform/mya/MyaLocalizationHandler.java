/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya;


import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

public class MyaLocalizationHandler {

    public Map<String, String> getLocalisedList(Context context, ArrayList<?> propertyForKey, Map<String, String> map) {
        for (int i = 0; i < propertyForKey.size(); i++) {
            String profileKey = (String) propertyForKey.get(i);
            String stringResourceByName = getStringResourceByName(context, profileKey);
            map.put(profileKey, stringResourceByName);
        }
        return map;
    }

    public String getStringResourceByName(Context context, String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        try {
            return context.getString(resId);
        } catch (Exception exception) {
            return null;
        }
    }

}
