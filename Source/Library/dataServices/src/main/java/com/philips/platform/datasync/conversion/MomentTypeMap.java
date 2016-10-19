/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.conversion;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.MeasurementDetailType;
import com.philips.platform.core.datatypes.MeasurementType;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.MomentType;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MomentTypeMap {

    public static final String UNKNOWN_TYPE = "UNKNOWN_TYPE";

    @NonNull
    private final Map<String, MomentType> toMomentTypeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    @NonNull
    private final Map<String, MeasurementType> toMeasurementTypeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    @NonNull
    private final Map<String, MeasurementDetailType> toMeasurementDetailTypeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    @NonNull
    private final Map<String, MomentDetailType> toMomentDetailTypeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    @NonNull
    private final Map<MomentType, String> fromMomentTypeMap;

    @NonNull
    private final Map<MeasurementType, String> fromMeasurementTypeMap;

    @NonNull
    private final Map<MomentDetailType, String> fromMomentDetailTypeMap;
    @NonNull
    private final Map<MeasurementDetailType, String> fromMeasurementDetailTypeMap;


    @Inject
    public MomentTypeMap() {
        toMomentTypeMap.put("Treatment", MomentType.TREATMENT);
        toMomentTypeMap.put("Temperature", MomentType.TEMPERATURE);
        toMomentTypeMap.put("Photo", MomentType.PHOTO);
        fromMomentTypeMap = createInvertedMap(toMomentTypeMap);

        toMeasurementTypeMap.put("Amount", MeasurementType.AMOUNT);
        toMeasurementTypeMap.put("Duration", MeasurementType.DURATION);
        toMeasurementTypeMap.put("Temperature", MeasurementType.TEMPERATURE);
        toMeasurementTypeMap.put("Weight", MeasurementType.WEIGHT);
        toMeasurementTypeMap.put("RelativeHumidity", MeasurementType.RELATIVE_HUMIDITY);
        toMeasurementTypeMap.put("Length", MeasurementType.LENGTH);
        fromMeasurementTypeMap = createInvertedMap(toMeasurementTypeMap);



        toMomentDetailTypeMap.put("Note", MomentDetailType.NOTE);
        toMomentDetailTypeMap.put("Photo", MomentDetailType.PHOTO);
        toMomentDetailTypeMap.put("Temperature", MomentDetailType.PHOTO);
        toMomentDetailTypeMap.put("Sticker", MomentDetailType.STICKER);
        toMomentDetailTypeMap.put("Video", MomentDetailType.VIDEO);
        toMomentDetailTypeMap.put("Phase", MomentDetailType.PHASE);
        toMomentDetailTypeMap.put("SiteCatalystId", MomentDetailType.TAGGING_ID);
        fromMomentDetailTypeMap = createInvertedMap(toMomentDetailTypeMap);

        toMeasurementDetailTypeMap.put("Location", MeasurementDetailType.LOCATION);
        fromMeasurementDetailTypeMap = createInvertedMap(toMeasurementDetailTypeMap);
    }

    @NonNull
    private <T> Map<T, String> createInvertedMap(@NonNull final Map<String, T> map) {
        Map<T, String> invertedMap = new HashMap<>();
        for (final String value : map.keySet()) {
            invertedMap.put(map.get(value), value);
        }
        return invertedMap;
    }

    @NonNull
    public MomentType getMomentType(@NonNull final String dsMomentString) {
        MomentType type = toMomentTypeMap.get(dsMomentString);
        return type == null ? MomentType.UNKNOWN : type;
    }

    @NonNull
    public MeasurementType getMeasurementType(@NonNull final String uGrowMeasurementTypeString) {
        MeasurementType type = toMeasurementTypeMap.get(uGrowMeasurementTypeString);
        return type == null ? MeasurementType.UNKNOWN : type;
    }

    @NonNull
    public MeasurementDetailType getMeasurementDetailType(@NonNull final String uGrowMeasurementDetailTypeString) {
        MeasurementDetailType type = toMeasurementDetailTypeMap.get(uGrowMeasurementDetailTypeString);
        return type == null ? MeasurementDetailType.UNKNOWN : type;
    }

    @NonNull
    public MomentDetailType getMomentDetailType(@NonNull final String uGrowMomentDetailString) {
        MomentDetailType type = toMomentDetailTypeMap.get(uGrowMomentDetailString);
        return type == null ? MomentDetailType.UNKNOWN : type;
    }

    @NonNull
    public String getMomentTypeString(@NonNull final MomentType type) {
        return getFromMap(fromMomentTypeMap, type);
    }

    @NonNull
    public String getMeasurementTypeString(@NonNull final MeasurementType type) {
        return getFromMap(fromMeasurementTypeMap, type);
    }



    @NonNull
    public String getMomentDetailTypeString(@NonNull final MomentDetailType type) {
        return getFromMap(fromMomentDetailTypeMap, type);
    }

    @NonNull
    private <T> String getFromMap(@NonNull final Map<T, String> fromMap, @NonNull final T type) {
        String value = fromMap.get(type);
        return value == null ? UNKNOWN_TYPE : value;
    }

    @NonNull
    public String getMeasurementDetailTypeString(@NonNull final MeasurementDetailType type) {
        return getFromMap(fromMeasurementDetailTypeMap, type);
    }

}
