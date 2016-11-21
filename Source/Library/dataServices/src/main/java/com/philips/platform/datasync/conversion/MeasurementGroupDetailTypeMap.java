package com.philips.platform.datasync.conversion;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.MeasurementDetailType;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetailType;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by 310218660 on 11/19/2016.
 */

public class MeasurementGroupDetailTypeMap {
    public static final String UNKNOWN_NAME = "UNKNOWN";
    public static final String UNKNOWN_UCORE_STRING = "unknown";

    @NonNull
    private final Map<MeasurementGroupDetailType, Map<String, String>> toMeasurementGroupDetailValueMap = new HashMap<>();

    @NonNull
    private final Map<MeasurementGroupDetailType, Map<String, String>> fromMeasurementGroupDetailValueMap = new HashMap<>();

    @Inject
    public MeasurementGroupDetailTypeMap() {
        final Map<String, String> toMeasurementGroupDetailType = new HashMap<>();
        toMeasurementGroupDetailType.put("TEMP_OF_DAY", MeasurementGroupDetailType.TEMP_OF_DAY.toString());
        fromMeasurementGroupDetailValueMap.put(MeasurementGroupDetailType.TEMP_OF_DAY, createInvertedMap(toMeasurementGroupDetailType));
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
