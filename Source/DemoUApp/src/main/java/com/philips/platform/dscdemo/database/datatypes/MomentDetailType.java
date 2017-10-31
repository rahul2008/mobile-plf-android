/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.dscdemo.database.datatypes;

import java.util.ArrayList;
import java.util.List;

public class MomentDetailType {

    public static final String UNKNOWN = "UNKNOWN";
    public static final String NOTE = "NOTE";
    public static final String PHOTO = "PHOTO";
    public static final String STICKER = "STICKER";
    public static final String VIDEO = "VIDEO";
    public static final String TAGGING_ID = "TAGGING ID";
    public static final String PHASE = "PHASE";
    public static final String DEVICE_SERIAL = "deviceSerial";
    public static final String CTN="CTN";
    public static final String FW_VERSION="fwVersion";
    public static final String MOMENT_VERSION="momentVersion";


    public static int getIDFromDescription(String description) {
        if (description == null) {
            return -1;
        }
        switch (description.toUpperCase()) {
            case UNKNOWN:
                return -1;
            case NOTE:
                return 50;
            case PHOTO:
                return 51;
            case STICKER:
                return 52;
            case VIDEO:
                return 53;
            case TAGGING_ID:
                return 54;
            case PHASE:
                return 55;
            case DEVICE_SERIAL:
                return 56;
            case CTN:
                return 57;
            case FW_VERSION:
                return 58;
            case MOMENT_VERSION:
                return 59;
            default:
                return 0;
        }
    }

    public static String getDescriptionFromID(int ID) {
        switch (ID) {
            case -1:
                return UNKNOWN;
            case 50:
                return NOTE;
            case 51:
                return PHOTO;
            case 52:
                return STICKER;
            case 53:
                return VIDEO;
            case 54:
                return TAGGING_ID;
            case 55:
                return PHASE;
            case 56:
                return DEVICE_SERIAL;
            case 57:
                return CTN;
            case 58:
                return FW_VERSION;
            case 59:
                return MOMENT_VERSION;
            default:
                return UNKNOWN;
        }
    }

    public static List<String> getMomentDetailTypes() {
        List<String> momentDetailType = new ArrayList<>();
        momentDetailType.add(UNKNOWN);
        momentDetailType.add(NOTE);
        momentDetailType.add(PHOTO);
        momentDetailType.add(STICKER);
        momentDetailType.add(VIDEO);
        momentDetailType.add(TAGGING_ID);
        momentDetailType.add(PHASE);
        momentDetailType.add(DEVICE_SERIAL);
        momentDetailType.add(CTN);
        momentDetailType.add(FW_VERSION);
        momentDetailType.add(MOMENT_VERSION);
        return momentDetailType;
    }
}
