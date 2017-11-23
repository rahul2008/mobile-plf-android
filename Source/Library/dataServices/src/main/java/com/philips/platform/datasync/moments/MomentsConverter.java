/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.datasync.moments;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.trackers.DataServicesManager;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MomentsConverter {

    @Inject
    BaseAppDataCreator baseAppDataCreater;

    @Inject
    public MomentsConverter() {
        DataServicesManager.getInstance().getAppComponent().injectMomentsConverter(this);
    }

    @NonNull
    public List<Moment> convert(@NonNull final List<UCoreMoment> uCoreMoments) {

        List<Moment> momentList = new ArrayList<>();
        try {
            for (UCoreMoment uCoreMoment : uCoreMoments) {
                Moment moment = createMoment(uCoreMoment);
                if (moment == null) return null;
                momentList.add(moment);
            }
        } catch (Exception e) {
            //Debug Log
        }
        return momentList;
    }

    @NonNull
    private Moment createMoment(@NonNull final UCoreMoment uCoreMoment) {
        String expirationDate = uCoreMoment.getExpirationDate();
        Moment moment = baseAppDataCreater.createMoment(uCoreMoment.getCreatorId(), uCoreMoment.getSubjectId(),
                uCoreMoment.getType(), expirationDate == null ? null : DateTime.parse(expirationDate));

        if (moment == null) {
            return null;
        }

        moment.setDateTime(new DateTime(uCoreMoment.getTimestamp()));

        addSynchronisationData(moment, uCoreMoment);

        List<UCoreDetail> details = uCoreMoment.getDetails();
        if (details != null) {
            addToUCoreMomentDetails(moment, details);
        }

        List<UCoreMeasurementGroups> measurementGroups = uCoreMoment.getMeasurementGroups();
        if (measurementGroups != null && measurementGroups.size() != 0) {
            //DSLog.i(DSLog.LOG, "BEFORE addMeasurementGroupsToMomentRecursively" + moment.toString() + "and UcoreMoment = " + uCoreMoment);
            addMeasurementGroupsToMomentRecursively(true, moment, null, measurementGroups);
        }
        return moment;
    }

    private void addMeasurementGroupsToMomentRecursively(final boolean isRoot, final Moment moment, final MeasurementGroup parents, final List<UCoreMeasurementGroups> measurementGroups) {
        for (UCoreMeasurementGroups uCoreMeasurementGroups : measurementGroups) {

            MeasurementGroup parentOrm;
            if (!isRoot) {
                parentOrm = baseAppDataCreater.createMeasurementGroup(parents);
            } else {
                parentOrm = baseAppDataCreater.createMeasurementGroup(moment);
            }
            addMeasurementsAndDeatilsToMeasurementGroup(uCoreMeasurementGroups, parentOrm);

            if (!isRoot) {
                parents.addMeasurementGroup(parentOrm);
            }
            if (uCoreMeasurementGroups.getMeasurementGroups() != null && uCoreMeasurementGroups.getMeasurementGroups().size() > 0) {
                addMeasurementGroupsToMomentRecursively(false, moment, parentOrm, uCoreMeasurementGroups.getMeasurementGroups());
            }

            if (isRoot) {
                moment.addMeasurementGroup(parentOrm);
            }
        }
    }

    private void addMeasurementsAndDeatilsToMeasurementGroup(UCoreMeasurementGroups uCoreMeasurementGroups, MeasurementGroup parent) {
        if (uCoreMeasurementGroups.getMeasurementGroupDetails() != null) {
            addToMeasurementGroupDeatil(uCoreMeasurementGroups, parent);
        }

        if (uCoreMeasurementGroups.getMeasurements() != null) {
            addMeasurements(parent, uCoreMeasurementGroups.getMeasurements());
        }
    }

    private void addToMeasurementGroupDeatil(UCoreMeasurementGroups uCoreMeasurementGroups, MeasurementGroup measurementGroup) {
        for (UCoreMeasurementGroupDetail uCoreDetail : uCoreMeasurementGroups.getMeasurementGroupDetails()) {
            // MeasurementGroupDetailType type = momentTypeMap.getMeasurementGroupDeatilType(uCoreDetail.getType());
//            if (!MomentDetailType.UNKNOWN.equals(uCoreDetail.getType())) {
            MeasurementGroupDetail measurementGroupDetail = baseAppDataCreater.createMeasurementGroupDetail(uCoreDetail.getType(), measurementGroup);
            measurementGroupDetail.setValue(uCoreDetail.getValue());
            measurementGroup.addMeasurementGroupDetail(measurementGroupDetail);
//            }
        }
    }

    private void addSynchronisationData(@NonNull final Moment moment, @NonNull final UCoreMoment uCoreMoment) {
        SynchronisationData synchronisationData = baseAppDataCreater.createSynchronisationData(uCoreMoment.getGuid(), uCoreMoment.getInactive(), new DateTime(uCoreMoment.getLastModified()), uCoreMoment.getVersion());
        moment.setSynchronisationData(synchronisationData);
    }

    private void addToUCoreMomentDetails(@NonNull final Moment moment, @NonNull final List<UCoreDetail> uCoreDetailList) {
        for (UCoreDetail uCoreDetail : uCoreDetailList) {
            //MomentDetailType type = momentTypeMap.getMomentDetailType(uCoreDetail.getType());
            //if (!MomentDetailType.UNKNOWN.equals(type)) {
            MomentDetail momentDetail = baseAppDataCreater.createMomentDetail(uCoreDetail.getType(), moment);
            momentDetail.setValue(uCoreDetail.getValue());
            moment.addMomentDetail(momentDetail);
//            }
        }
    }

    private void addMeasurements(@NonNull final MeasurementGroup measurementGroup, @NonNull final List<UCoreMeasurement> uCoreMeasurementList) {
        for (UCoreMeasurement uCoreMeasurement : uCoreMeasurementList) {
            //MeasurementType type = momentTypeMap.getMeasurementType(uCoreMeasurement.getType());
            //if (!MeasurementType.UNKNOWN.equals(type)) {
            Measurement measurement = baseAppDataCreater.createMeasurement(uCoreMeasurement.getType(), measurementGroup);
            measurement.setDateTime(new DateTime(uCoreMeasurement.getTimestamp()));
            measurement.setValue(uCoreMeasurement.getValue());
            measurement.setUnit(uCoreMeasurement.getUnit());

            List<UCoreDetail> uCoreDetailList = uCoreMeasurement.getDetails();
            if (uCoreDetailList != null) {
                addMeasurementDetails(measurement, uCoreDetailList);
            }

            measurementGroup.addMeasurement(measurement);
            // }
        }
    }

    private void addMeasurementDetails(@NonNull final Measurement measurement, @NonNull final List<UCoreDetail> uCoreDetailList) {
        for (UCoreDetail uCoreDetail : uCoreDetailList) {
//            MeasurementDetailType type = momentTypeMap.getMeasurementDetailType(uCoreDetail.getType());
//            if (!MeasurementDetailType.UNKNOWN.equals(type)) {
            final MeasurementDetail measurementDetail = baseAppDataCreater.createMeasurementDetail(uCoreDetail.getType(), measurement);
            String value = uCoreDetail.getValue();
            // if (!value.equals(MeasurementDetailValueMap.UNKNOWN_NAME)) {
            measurementDetail.setValue(value);
            measurement.addMeasurementDetail(measurementDetail);
            // }
//            }
        }
    }

    public UCoreMoment convertToUCoreMoment(Moment moment) {
        UCoreMoment uCoreMoment = new UCoreMoment();

        try {
            uCoreMoment.setTimestamp(moment.getDateTime().toString());
            String momentTypeString = moment.getType();

            uCoreMoment.setType(momentTypeString);
            List<UCoreDetail> uCoreMomentList = new ArrayList<>();

            addToUCoreMomentDetails(moment.getMomentDetails(), uCoreMomentList);
            uCoreMoment.setDetails(uCoreMomentList);

            final Collection<? extends MeasurementGroup> measurementGroups = moment.getMeasurementGroups();
            if (measurementGroups != null && measurementGroups.size() != 0) {
                uCoreMoment.setMeasurementGroups(convertMeasurementGroupToUCoreMeasurementGroup(moment.getMeasurementGroups()));
            }
            setVersion(uCoreMoment, moment.getSynchronisationData());
        } catch (Exception e) {
            //Debug Log
        }
        return uCoreMoment;
    }

    private void setVersion(final UCoreMoment uCoreMoment, final SynchronisationData synchronisationData) {
        if (synchronisationData != null) {
            uCoreMoment.setVersion(synchronisationData.getVersion());
        }
    }

    private void addToUCoreMomentDetails(Collection<? extends MomentDetail> momentDetails, List<UCoreDetail> momentDetailList) {

        for (MomentDetail momentDetail : momentDetails) {
            UCoreDetail detail = new UCoreDetail();
            detail.setType(momentDetail.getType());
            detail.setValue(momentDetail.getValue());

            momentDetailList.add(detail);
        }
    }

    private List<UCoreMeasurementGroups> convertMeasurementGroupToUCoreMeasurementGroup(Collection<? extends MeasurementGroup> measurementGroupsList) {
        List<UCoreMeasurementGroups> uCoreMeasurementGroupsList = new ArrayList<>();

        for (MeasurementGroup measurementGroup : measurementGroupsList) {
            UCoreMeasurementGroups uCoreMeasurementGroups = new UCoreMeasurementGroups();
            uCoreMeasurementGroups.setDetails(convertMGDetailToUCoreMGDetail(measurementGroup.getMeasurementGroupDetails()));
            uCoreMeasurementGroups.setMeasurements(convertMeasurementToUCoreMeasurement(measurementGroup.getMeasurements()));
            uCoreMeasurementGroups.setMeasurementGroups(convertMeasurementGroupToUCoreMeasurementGroupRecursively(measurementGroup.getMeasurementGroups()));
            uCoreMeasurementGroupsList.add(uCoreMeasurementGroups);
        }

        return uCoreMeasurementGroupsList;
    }

    private List<UCoreMeasurementGroupDetail> convertMGDetailToUCoreMGDetail(Collection<? extends MeasurementGroupDetail> measurementGroupDetails) {

        List<UCoreMeasurementGroupDetail> uCoreMeasurementGroupDetailList = new ArrayList<>();
        for (MeasurementGroupDetail detail : measurementGroupDetails) {
            UCoreMeasurementGroupDetail uCoreMeasurementGroupDetail = new UCoreMeasurementGroupDetail();
            uCoreMeasurementGroupDetail.setType(detail.getType());
            uCoreMeasurementGroupDetail.setValue(detail.getValue());

            uCoreMeasurementGroupDetailList.add(uCoreMeasurementGroupDetail);
        }
        return uCoreMeasurementGroupDetailList;
    }

    private List<UCoreMeasurement> convertMeasurementToUCoreMeasurement(Collection<? extends Measurement> measurements) {
        List<UCoreMeasurement> uCoreMeasurementList = new ArrayList<>();

        for (Measurement measurement : measurements) {
            UCoreMeasurement uCoreMeasurement = new UCoreMeasurement();
            uCoreMeasurement.setTimestamp(measurement.getDateTime().toString());
            uCoreMeasurement.setValue(measurement.getValue());
            uCoreMeasurement.setType(measurement.getType());
            uCoreMeasurement.setUnit(measurement.getUnit());
            uCoreMeasurement.setDetails(convertMeasurementDetailToUCoreDetail(measurement.getMeasurementDetails()));

            uCoreMeasurementList.add(uCoreMeasurement);
        }

        return uCoreMeasurementList;
    }

    private List<UCoreDetail> convertMeasurementDetailToUCoreDetail(Collection<? extends MeasurementDetail> measurementDetails) {
        List<UCoreDetail> uCoreDetailList = new ArrayList<>();

        for (MeasurementDetail measurementDetail : measurementDetails) {
            UCoreDetail uCoreDetail = new UCoreDetail();
            uCoreDetail.setType(measurementDetail.getType());
            uCoreDetail.setValue(measurementDetail.getValue());

            uCoreDetailList.add(uCoreDetail);
        }

        return uCoreDetailList;
    }

    private List<UCoreMeasurementGroups> convertMeasurementGroupToUCoreMeasurementGroupRecursively(Collection<? extends MeasurementGroup> measurementGroups) {
        List<UCoreMeasurementGroups> uCoreMeasurementGroupsList = new ArrayList<>();

        if (measurementGroups.size() > 0) {
            List<MeasurementGroup> measurementGroupList = convertToList(measurementGroups);
            for (int i = 0; i < measurementGroupList.size(); i++) {
                UCoreMeasurementGroups uCoreMeasurementGroups = new UCoreMeasurementGroups();
                uCoreMeasurementGroups.setDetails(convertMGDetailToUCoreMGDetail(measurementGroupList.get(i).getMeasurementGroupDetails()));
                uCoreMeasurementGroups.setMeasurements(convertMeasurementToUCoreMeasurement(measurementGroupList.get(i).getMeasurements()));
                uCoreMeasurementGroups.setMeasurementGroups(convertMeasurementGroupToUCoreMeasurementGroupRecursively(measurementGroupList.get(i).getMeasurementGroups()));
                uCoreMeasurementGroupsList.add(uCoreMeasurementGroups);
            }
        }
        return uCoreMeasurementGroupsList;
    }

    private List<MeasurementGroup> convertToList(Collection<? extends MeasurementGroup> measurementGroups) {
        List<MeasurementGroup> measurementGroupList = new ArrayList<>();
        for (MeasurementGroup measurementGroup : measurementGroups) {
            measurementGroupList.add(measurementGroup);
        }
        return measurementGroupList;
    }

}