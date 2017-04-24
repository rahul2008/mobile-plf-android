/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.dataservices.database.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MomentType {


    public static final String UNKNOWN = "UNKNOWN";
    public static final String TREATMENT = "TREATMENT";
    public static final String USER_INFO = "USER_INFO";
    public static final String PHOTO = "PHOTO";
    public static final String TEMPERATURE = "Temperature";


    public static int getIDFromDescription(String description) {
        if(description == null){
            return -1;
        }

        if(description.equalsIgnoreCase(TEMPERATURE)){
            return 25;
        }else {
            return -1;
        }
    }

    public static String getDescriptionFromID(int ID) {
        switch (ID) {

            case -1:
                return UNKNOWN;
            case 20:
                return TREATMENT;
            case 21:
                return USER_INFO;
            case 22:
                return PHOTO;
            case 25:
                return TEMPERATURE;

            default:
                return UNKNOWN;
        }
    }
    public static List<String> getMomentTypes(){
        List<String> momentTypes = new ArrayList<>();
        momentTypes.add(UNKNOWN);
        momentTypes.add(TREATMENT);
        momentTypes.add(USER_INFO);
        momentTypes.add(PHOTO);
        momentTypes.add(TEMPERATURE);
        return momentTypes;
    }
}
