/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.moments;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementDetailType;
import com.philips.platform.core.datatypes.MeasurementType;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.datasync.conversion.MeasurementDetailValueMap;
import com.philips.platform.datasync.conversion.MomentTypeMap;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/*
 * *
 *  * (C) Koninklijke Philips N.V., 2015.
 *  * All rights reserved.
 *
 */
public class MomentsConverter {

    @NonNull
    private MomentTypeMap momentTypeMap;

    private MeasurementDetailValueMap measurementDetailValueMap;

    @NonNull
    private BaseAppDataCreator baseAppDataCreater;

    @Inject
    public MomentsConverter(@NonNull final MomentTypeMap momentTypeMap,
                            @NonNull final MeasurementDetailValueMap measurementDetailValueMap,
                            @NonNull final BaseAppDataCreator uGrowDataCreator) {
        this.momentTypeMap = momentTypeMap;
        this.measurementDetailValueMap = measurementDetailValueMap;
        this.baseAppDataCreater = uGrowDataCreator;
    }

    @NonNull
    public List<Moment> convert(@NonNull final List<com.philips.platform.datasync.moments.UCoreMoment> uCoreMoments) {
        List<Moment> momentList = new ArrayList<>();
        for (com.philips.platform.datasync.moments.UCoreMoment uCoreMoment : uCoreMoments) {
            Moment moment = createMoment(uCoreMoment);
            momentList.add(moment);
        }
        return momentList;
    }

    @NonNull
    private Moment createMoment(@NonNull final com.philips.platform.datasync.moments.UCoreMoment uCoreMoment) {
        Moment moment = baseAppDataCreater.createMomentWithoutUUID(uCoreMoment.getCreatorId(), uCoreMoment.getSubjectId(),
                momentTypeMap.getMomentType(uCoreMoment.getType()));
        moment.setDateTime(new DateTime(uCoreMoment.getTimestamp()));

        addSynchronisationData(moment, uCoreMoment);

        List<com.philips.platform.datasync.moments.UCoreDetail> details = uCoreMoment.getDetails();
        if (details != null) {
            addToUCoreMomentDetails(moment, details);
        }

        List<com.philips.platform.datasync.moments.UCoreMeasurement> measurements = uCoreMoment.getMeasurements();
        if (measurements != null) {
            addMeasurements(moment, measurements);
        }

        return moment;
    }

    private void addSynchronisationData(@NonNull final Moment moment, @NonNull final com.philips.platform.datasync.moments.UCoreMoment uCoreMoment) {
        SynchronisationData synchronisationData = baseAppDataCreater.createSynchronisationData(uCoreMoment.getGuid(), uCoreMoment.getInactive(), new DateTime(uCoreMoment.getLastModified()), uCoreMoment.getVersion());
        moment.setSynchronisationData(synchronisationData);
    }

    private void addToUCoreMomentDetails(@NonNull final Moment moment, @NonNull final List<com.philips.platform.datasync.moments.UCoreDetail> uCoreDetailList) {
        for (com.philips.platform.datasync.moments.UCoreDetail uCoreDetail : uCoreDetailList) {
            MomentDetailType type = momentTypeMap.getMomentDetailType(uCoreDetail.getType());
            if (!MomentDetailType.UNKNOWN.equals(type)) {
                MomentDetail momentDetail = baseAppDataCreater.createMomentDetail(type, moment);
                momentDetail.setValue(uCoreDetail.getValue());
                moment.addMomentDetail(momentDetail);
            }
        }
    }

    private void addMeasurements(@NonNull final Moment moment, @NonNull final List<com.philips.platform.datasync.moments.UCoreMeasurement> uCoreMeasurementList) {
        for (com.philips.platform.datasync.moments.UCoreMeasurement uCoreMeasurement : uCoreMeasurementList) {
            MeasurementType type = momentTypeMap.getMeasurementType(uCoreMeasurement.getType());
            if (!MeasurementType.UNKNOWN.equals(type)) {
                Measurement measurement = baseAppDataCreater.createMeasurement(type, moment);
                measurement.setDateTime(new DateTime(uCoreMeasurement.getTimestamp()));
                measurement.setValue(uCoreMeasurement.getValue());

                List<com.philips.platform.datasync.moments.UCoreDetail> uCoreDetailList = uCoreMeasurement.getDetails();
                if (uCoreDetailList != null) {
                    addMeasurementDetails(measurement, uCoreDetailList);
                }

                moment.addMeasurement(measurement);
            }
        }
    }

    private void addMeasurementDetails(@NonNull final Measurement measurement, @NonNull final List<com.philips.platform.datasync.moments.UCoreDetail> uCoreDetailList) {
        for (com.philips.platform.datasync.moments.UCoreDetail uCoreDetail : uCoreDetailList) {
            MeasurementDetailType type = momentTypeMap.getMeasurementDetailType(uCoreDetail.getType());
            if (!MeasurementDetailType.UNKNOWN.equals(type)) {
                final MeasurementDetail measurementDetail = baseAppDataCreater.createMeasurementDetail(type, measurement);
                String value = uCoreDetail.getValue();
                if (!value.equals(MeasurementDetailValueMap.UNKNOWN_NAME)) {
                    measurementDetail.setValue(value);
                    measurement.addMeasurementDetail(measurementDetail);
                }
            }
        }
    }

    /// This is the part which converts App moment to UCore moment
    public com.philips.platform.datasync.moments.UCoreMoment convertToUCoreMoment(Moment moment) {
        com.philips.platform.datasync.moments.UCoreMoment uCoreMoment = new com.philips.platform.datasync.moments.UCoreMoment();

        uCoreMoment.setTimestamp(moment.getDateTime().toString());
        String momentTypeString = momentTypeMap.getMomentTypeString(moment.getType());
        uCoreMoment.setType(momentTypeString);

        List<com.philips.platform.datasync.moments.UCoreMeasurement> uCoreMeasurementList = new ArrayList<>();
        List<com.philips.platform.datasync.moments.UCoreDetail> uCoreMomentList = new ArrayList<>();

        addToUCoreMomentDetails(moment.getMomentDetails(), uCoreMomentList);
        addToUCoreMeasurements(moment.getMeasurements(), uCoreMeasurementList);

        uCoreMoment.setDetails(uCoreMomentList);
        uCoreMoment.setMeasurements(uCoreMeasurementList);

        setVersion(uCoreMoment, moment.getSynchronisationData());
        return uCoreMoment;
    }

    private void setVersion(final com.philips.platform.datasync.moments.UCoreMoment uCoreMoment, final SynchronisationData synchronisationData) {
        if (synchronisationData != null) {
            uCoreMoment.setVersion(synchronisationData.getVersion());
        }
    }

    private void addToUCoreMomentDetails(Collection<? extends MomentDetail> momentDetails, List<com.philips.platform.datasync.moments.UCoreDetail> momentDetailList) {

        for (MomentDetail momentDetail : momentDetails) {
            com.philips.platform.datasync.moments.UCoreDetail detail = new com.philips.platform.datasync.moments.UCoreDetail();
            detail.setType(momentTypeMap.getMomentDetailTypeString(momentDetail.getType()));
            detail.setValue(momentDetail.getValue());

            momentDetailList.add(detail);
        }
    }

    private void addToUCoreMeasurements(Collection<? extends Measurement> measurements, List<com.philips.platform.datasync.moments.UCoreMeasurement> uCoreMeasurementList) {
        for (Measurement measurement : measurements) {
            com.philips.platform.datasync.moments.UCoreMeasurement uCoreMeasurement = new com.philips.platform.datasync.moments.UCoreMeasurement();
            uCoreMeasurement.setTimestamp(measurement.getDateTime().toString());
            uCoreMeasurement.setType(momentTypeMap.getMeasurementTypeString(measurement.getType()));
            uCoreMeasurement.setValue(measurement.getValue());

            addToUCoreMeasurementDetails(measurement.getMeasurementDetails(), uCoreMeasurement);
            uCoreMeasurementList.add(uCoreMeasurement);
        }
    }

    private void addToUCoreMeasurementDetails(Collection<? extends MeasurementDetail> measurementDetails, com.philips.platform.datasync.moments.UCoreMeasurement uCoreMeasurement) {
        List<com.philips.platform.datasync.moments.UCoreDetail> uCoreDetailList = new ArrayList<>();

        for (MeasurementDetail measurementDetail : measurementDetails) {
            com.philips.platform.datasync.moments.UCoreDetail uCoreDetail = new com.philips.platform.datasync.moments.UCoreDetail();
            uCoreDetail.setType(momentTypeMap.getMeasurementDetailTypeString(measurementDetail.getType()));

           // uCoreDetail.setValue(measurementDetailValueMap.getString(measurementDetail.getType(), measurementDetail.getValue()));
            uCoreDetail.setValue(measurementDetail.getValue());

            uCoreDetailList.add(uCoreDetail);
        }
        uCoreMeasurement.setDetails(uCoreDetailList);
    }

}