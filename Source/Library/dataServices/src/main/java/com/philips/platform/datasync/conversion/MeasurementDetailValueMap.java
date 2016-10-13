/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.conversion;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.MeasurementDetailType;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MeasurementDetailValueMap {

    public static final String UNKNOWN_NAME = "UNKNOWN";
    public static final String UNKNOWN_UCORE_STRING = "unknown";

    @NonNull
    private final Map<MeasurementDetailType, Map<String, String>> toMeasurementDetailValueMap = new HashMap<>();

    @NonNull
    private final Map<MeasurementDetailType, Map<String, String>> fromMeasurementDetailValueMap = new HashMap<>();

    @Inject
    public MeasurementDetailValueMap() {
        final Map<String, String> toMeasurementDetailType = new HashMap<>();
        toMeasurementDetailType.put("LOCATION", MeasurementDetailType.LOCATION.toString());
        fromMeasurementDetailValueMap.put(MeasurementDetailType.LOCATION, createInvertedMap(toMeasurementDetailType));
    }

    @NonNull
    private <T> Map<T, String> createInvertedMap(@NonNull final Map<String, T> map) {
        Map<T, String> invertedMap = new HashMap<>();
        for (final String value : map.keySet()) {
            invertedMap.put(map.get(value), value);
        }
        return invertedMap;
    }

   /* @NonNull
    public String getName(@NonNull final MeasurementDetailType type, @NonNull final String uCoreString) {
        String name = UNKNOWN_NAME;
        Map<String, String> detailValueMap = toMeasurementDetailValueMap.get(type);
        if (detailValueMap != null) {
            String value = detailValueMap.get(uCoreString);
            if (value != null) {
                name = value;
            }
        }
        return name;
    }*/

    /*@NonNull
    public String getString(@NonNull final MeasurementDetailType type, @NonNull final String key) {
        String uCoreString = UNKNOWN_UCORE_STRING;

        final Map<String, String> toMeasurementDetailType = new HashMap<>();
        toMeasurementDetailType.put(key,MeasurementDetailType.LOCATION.toString());
        fromMeasurementDetailValueMap.put(MeasurementDetailType.LOCATION, createInvertedMap(toMeasurementDetailType));

        Map<String, String> detailValueMap = fromMeasurementDetailValueMap.get(type);
        if (detailValueMap != null) {
            String value = detailValueMap.get(MeasurementDetailType.LOCATION.toString());
            if (value != null) {
                uCoreString = value;
            }
        }
        return uCoreString;
    }*/
}
