//package com.philips.platform.core.datatypes;
//
///**
// * Created by 310218660 on 11/17/2016.
// */
//
//public enum MeasurementGroupDetailType {
//    UNKNOWN(-1, "UNKNOWN"),
//    TEMP_OF_DAY(77, "TEMP_OF_DAY"),;
//
//    private final int id;
//    private final String description;
//
//    MeasurementGroupDetailType(final int id, final String description) {
//        this.id = id;
//        this.description = description;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public static MeasurementGroupDetailType fromId(final int id) {
//        final MeasurementGroupDetailType[] values = MeasurementGroupDetailType.values();
//
//        for (MeasurementGroupDetailType item : values) {
//            if (id == item.getId()) {
//                return item;
//            }
//        }
//
//        return UNKNOWN;
//    }
//}
