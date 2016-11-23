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
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.MeasurementGroupDetailType;
import com.philips.platform.core.datatypes.MeasurementType;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.conversion.MeasurementDetailValueMap;
import com.philips.platform.datasync.conversion.MeasurementGroupDetailTypeMap;
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

    private MeasurementGroupDetailTypeMap measurementGroupDetailValueMap;

    @NonNull
    private BaseAppDataCreator baseAppDataCreater;

    @Inject
    public MomentsConverter(@NonNull final MomentTypeMap momentTypeMap,
                            @NonNull final MeasurementDetailValueMap measurementDetailValueMap,
                            @NonNull final MeasurementGroupDetailTypeMap measurementGroupDetailTypeMap) {
        this.momentTypeMap = momentTypeMap;
        this.measurementDetailValueMap = measurementDetailValueMap;
        this.measurementGroupDetailValueMap = measurementGroupDetailTypeMap;
        DataServicesManager manager = DataServicesManager.getInstance();
        this.baseAppDataCreater = manager.getDataCreater();
    }

    @NonNull
    public List<Moment> convert(@NonNull final List<UCoreMoment> uCoreMoments) {
        List<Moment> momentList = new ArrayList<>();
        for (UCoreMoment uCoreMoment : uCoreMoments) {
            Moment moment = createMoment(uCoreMoment);
            momentList.add(moment);
        }
        return momentList;
    }

//    @NonNull
//    private Moment createMoment(@NonNull final UCoreMoment uCoreMoment) {
//        Moment moment = baseAppDataCreater.createMomentWithoutUUID(uCoreMoment.getCreatorId(), uCoreMoment.getSubjectId(),
//                momentTypeMap.getMomentType(uCoreMoment.getType()));
//        moment.setDateTime(new DateTime(uCoreMoment.getTimestamp()));
//
//        addSynchronisationData(moment, uCoreMoment);
//
//        List<UCoreDetail> details = uCoreMoment.getDetails();
//        if (details != null) {
//            addToUCoreMomentDetails(moment, details);
//        }
//
//        List<UCoreMeasurement> measurements = uCoreMoment.getMeasurements();
//        if (measurements != null) {
//            addMeasurements(moment, measurements);
//        }
//
//        return moment;
//    }

    @NonNull
    private Moment createMoment(@NonNull final UCoreMoment uCoreMoment) {
        Moment moment = baseAppDataCreater.createMomentWithoutUUID(uCoreMoment.getCreatorId(), uCoreMoment.getSubjectId(),
                momentTypeMap.getMomentType(uCoreMoment.getType()));
        moment.setDateTime(new DateTime(uCoreMoment.getTimestamp()));

        addSynchronisationData(moment, uCoreMoment);

        List<UCoreDetail> details = uCoreMoment.getDetails();
        if (details != null) {
            addToUCoreMomentDetails(moment, details);
        }

        List<UCoreMeasurementGroups> measurementGroups = uCoreMoment.getMeasurementGroups();//only one entry will come here
        if (measurementGroups != null) {
            addMeasurementGroupsToMoment(moment, measurementGroups);
        }
        return moment;
    }

//    @NonNull
//    private void addMeasurementGroupsToMoment(Moment moment, List<UCoreMeasurementGroups> measurementGroups) {
//        for (UCoreMeasurementGroups uCoreMeasurementGroups : measurementGroups) {
//            MeasurementGroup measurementGroup = baseAppDataCreater.createMeasurementGroup(moment);
//            if(uCoreMeasurementGroups.getMeasurementGroupDetails()!=null){
//                addToMeasurementGroupDeatil(uCoreMeasurementGroups, measurementGroup);
//            }
//
//            if(uCoreMeasurementGroups.getMeasurements()!=null){
//                addMeasurements(measurementGroup,uCoreMeasurementGroups.getMeasurements());
//            }
//
//            Collection<? extends UCoreMeasurementGroups> uCoreMeasurementGroups1 = uCoreMeasurementGroups.getMeasurementGroups();
//            MeasurementGroup measurementGroup1 = measurementGroup;
//            if(uCoreMeasurementGroups1!=null){
//                for(UCoreMeasurementGroups uCoreMeasurementGroup : uCoreMeasurementGroups1){
//                    measurementGroup1 = baseAppDataCreater.createMeasurementGroup(measurementGroup);
//                    if(uCoreMeasurementGroup.getMeasurementGroupDetails()!=null){
//                        addToMeasurementGroupDeatil(uCoreMeasurementGroup, measurementGroup1);
//                    }
//                    if(uCoreMeasurementGroup.getMeasurements()!=null){
//                        addMeasurements(measurementGroup1,uCoreMeasurementGroup.getMeasurements());
//                    }
//                }
//            }
//
//            moment.addMeasurementGroup(measurementGroup1);
//        }
//
//    }

    @NonNull
    private void addMeasurementGroupsToMoment(Moment moment, List<UCoreMeasurementGroups> measurementGroups) {
        MeasurementGroup parentOrmToAttachMoment = null;
        for (UCoreMeasurementGroups uCoreMeasurementGroups : measurementGroups) {
            MeasurementGroup parentOrm = baseAppDataCreater.createMeasurementGroup(moment);
            parentOrmToAttachMoment = parentOrm;

            UCoreMeasurementGroups parentUCore = uCoreMeasurementGroups;

            addMeasurementsAndDeatilsToMeasurementGroup(parentUCore, parentOrm);

            List<UCoreMeasurementGroups> childUCoreList = parentUCore.getMeasurementGroups();
            int i = 0;
            while (childUCoreList!=null){
                UCoreMeasurementGroups childUCore = childUCoreList.get(i);
                MeasurementGroup childOrm = baseAppDataCreater.createMeasurementGroup(parentOrm);
                addMeasurementsAndDeatilsToMeasurementGroup(childUCore, childOrm);
                parentOrmToAttachMoment.addMeasurementGroup(childOrm);
                parentOrm = childOrm;
                childUCoreList = childUCore.getMeasurementGroups();
                i++;
            }

            /*Collection<? extends UCoreMeasurementGroups> uCoreMeasurementGroups1 = uCoreMeasurementGroups.getMeasurementGroups();
            MeasurementGroup measurementGroup1 = measurementGroup;
            if(uCoreMeasurementGroups1!=null){
                for(UCoreMeasurementGroups uCoreMeasurementGroup : uCoreMeasurementGroups1){
                    measurementGroup1 = baseAppDataCreater.createMeasurementGroup(measurementGroup);
                    addMeasurementsAndDeatilsToMeasurementGroup(uCoreMeasurementGroup, measurementGroup1);
                }
            }

            moment.addMeasurementGroup(measurementGroup1);*/
        }

        moment.addMeasurementGroup(parentOrmToAttachMoment);

    }

    private void addMeasurementsAndDeatilsToMeasurementGroup(UCoreMeasurementGroups uCoreMeasurementGroups, MeasurementGroup parent) {
        if(uCoreMeasurementGroups.getMeasurementGroupDetails()!=null){
            addToMeasurementGroupDeatil(uCoreMeasurementGroups, parent);
        }

        if(uCoreMeasurementGroups.getMeasurements()!=null){
            addMeasurements(parent,uCoreMeasurementGroups.getMeasurements());
        }
    }

    private void addToMeasurementGroupDeatil(UCoreMeasurementGroups uCoreMeasurementGroups, MeasurementGroup measurementGroup) {
        for (UCoreMeasurementGroupDetail uCoreDetail : uCoreMeasurementGroups.getMeasurementGroupDetails()) {
            MeasurementGroupDetailType type = momentTypeMap.getMeasurementGroupDeatilType(uCoreDetail.getType());
            if (!MomentDetailType.UNKNOWN.equals(type)) {
                MeasurementGroupDetail measurementGroupDetail = baseAppDataCreater.createMeasurementGroupDetail(type, measurementGroup);
                measurementGroupDetail.setValue(uCoreDetail.getValue());
                measurementGroup.addMeasurementGroupDetail(measurementGroupDetail);
            }
        }
    }


    /*
        private MeasurementGroup createMeasurementGroup(UCoreMeasurementGroups uCoreMeasurementGroups) {
            MeasurementGroup measurementGroup = baseAppDataCreater.createMeasurementGroup(uCoreMoment.getCreatorId(), uCoreMoment.getSubjectId(),
                    momentTypeMap.getMomentType(uCoreMoment.getType()));
            moment.setDateTime(new DateTime(uCoreMoment.getTimestamp()));

            addSynchronisationData(moment, uCoreMoment);

            List<UCoreDetail> details = uCoreMoment.getDetails();
            if (details != null) {
                addToUCoreMomentDetails(moment, details);
            }

            List<UCoreMeasurementGroups> measurementGroups = uCoreMoment.getMeasurementGroups();
            if (measurementGroups != null) {
                addMeasurementGroupsToMoment(moment, measurementGroups);
            }

            return moment;
        }

    */
    private void addSynchronisationData(@NonNull final Moment moment, @NonNull final UCoreMoment uCoreMoment) {
        SynchronisationData synchronisationData = baseAppDataCreater.createSynchronisationData(uCoreMoment.getGuid(), uCoreMoment.getInactive(), new DateTime(uCoreMoment.getLastModified()), uCoreMoment.getVersion());
        moment.setSynchronisationData(synchronisationData);
    }

    private void addToUCoreMomentDetails(@NonNull final Moment moment, @NonNull final List<UCoreDetail> uCoreDetailList) {
        for (UCoreDetail uCoreDetail : uCoreDetailList) {
            MomentDetailType type = momentTypeMap.getMomentDetailType(uCoreDetail.getType());
            if (!MomentDetailType.UNKNOWN.equals(type)) {
                MomentDetail momentDetail = baseAppDataCreater.createMomentDetail(type, moment);
                momentDetail.setValue(uCoreDetail.getValue());
                moment.addMomentDetail(momentDetail);
            }
        }
    }

    private void addMeasurements(@NonNull final MeasurementGroup measurementGroup, @NonNull final List<UCoreMeasurement> uCoreMeasurementList) {
        for (UCoreMeasurement uCoreMeasurement : uCoreMeasurementList) {
            MeasurementType type = momentTypeMap.getMeasurementType(uCoreMeasurement.getType());
            if (!MeasurementType.UNKNOWN.equals(type)) {
                Measurement measurement = baseAppDataCreater.createMeasurement(type, measurementGroup);
                measurement.setDateTime(new DateTime(uCoreMeasurement.getTimestamp()));
                measurement.setValue(uCoreMeasurement.getValue());

                List<UCoreDetail> uCoreDetailList = uCoreMeasurement.getDetails();
                if (uCoreDetailList != null) {
                    addMeasurementDetails(measurement, uCoreDetailList);
                }

                measurementGroup.addMeasurement(measurement);
            }
        }
    }

/*    private void addMeasurements(@NonNull final Moment moment, @NonNull final List<UCoreMeasurement> uCoreMeasurementList) {
        for (UCoreMeasurement uCoreMeasurement : uCoreMeasurementList) {
            MeasurementType type = momentTypeMap.getMeasurementType(uCoreMeasurement.getType());
            if (!MeasurementType.UNKNOWN.equals(type)) {
                Measurement measurement = baseAppDataCreater.createMeasurement(type, moment);
                measurement.setDateTime(new DateTime(uCoreMeasurement.getTimestamp()));
                measurement.setValue(uCoreMeasurement.getValue());

                List<UCoreDetail> uCoreDetailList = uCoreMeasurement.getDetails();
                if (uCoreDetailList != null) {
                    addMeasurementDetails(measurement, uCoreDetailList);
                }

                moment.addMeasurement(measurement);
            }
        }
    }*/

    private void addMeasurementDetails(@NonNull final Measurement measurement, @NonNull final List<UCoreDetail> uCoreDetailList) {
        for (UCoreDetail uCoreDetail : uCoreDetailList) {
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

  /*  private void addMeasurementGroupDetails(@NonNull final MeasurementGroup measurementGroup, @NonNull final List<UCoreMeasurementGroupDetail> uCoreDetailList) {
        for (UCoreMeasurementGroupDetail uCoreDetail : uCoreDetailList) {
            MeasurementDetailType type = momentTypeMap.getMeasurementDetailType(uCoreDetail.getType());
            if (!MeasurementDetailType.UNKNOWN.equals(type)) {
                final MeasurementGroupDetail measurementGroupDetail = baseAppDataCreater.createMeasurementGroupDetail(type, measurementGroup);
                String value = uCoreDetail.getValue();
                if (!value.equals(MeasurementDetailValueMap.UNKNOWN_NAME)) {
                    measurementGroupDetail.setValue(value);
                    measurementGroup.addMeasurementGroupDetail(measurementGroupDetail);
                }
            }
        }
    }
*/
    /// This is the part which converts App moment to UCore moment
//    public com.philips.platform.datasync.moments.UCoreMoment convertToUCoreMoment(Moment moment) {
//        UCoreMoment uCoreMoment = new UCoreMoment();
//
//        uCoreMoment.setTimestamp(moment.getDateTime().toString());
//        String momentTypeString = momentTypeMap.getMomentTypeString(moment.getType());
//        uCoreMoment.setType(momentTypeString);
//
//        List<UCoreMeasurement> uCoreMeasurementList = new ArrayList<>();
//        List<UCoreDetail> uCoreMomentList = new ArrayList<>();
//
//        addToUCoreMomentDetails(moment.getMomentDetails(), uCoreMomentList);
//        addToUCoreMeasurements(moment.getMeasurements(), uCoreMeasurementList);
//
//        uCoreMoment.setDetails(uCoreMomentList);
//        uCoreMoment.setMeasurements(uCoreMeasurementList);
//
//        setVersion(uCoreMoment, moment.getSynchronisationData());
//        return uCoreMoment;
//    }

    public UCoreMoment convertToUCoreMoment(Moment moment) {
        UCoreMoment uCoreMoment = new UCoreMoment();

        uCoreMoment.setTimestamp(moment.getDateTime().toString());
        String momentTypeString = momentTypeMap.getMomentTypeString(moment.getType());
        uCoreMoment.setType(momentTypeString);

        List<UCoreMeasurementGroups> uCoreMeasurementGroupList = new ArrayList<>();
        List<UCoreMeasurementGroupDetail> uCoreMeasurementGroupDetails = new ArrayList<>();
        List<UCoreDetail> uCoreMomentList = new ArrayList<>();

        addToUCoreMomentDetails(moment.getMomentDetails(), uCoreMomentList);
        //addToUCoreMeasurements(moment.getMeasurements(), uCoreMeasurementList);
        uCoreMoment.setDetails(uCoreMomentList);

        uCoreMeasurementGroupList = addToUCoreMeasurementGroups(moment.getMeasurementGroups(),uCoreMeasurementGroupList);
        uCoreMoment.setMeasurementGroups(uCoreMeasurementGroupList);

        setVersion(uCoreMoment, moment.getSynchronisationData());
        return uCoreMoment;
    }

     /*private List<UCoreMeasurementGroups> addToUCoreMeaurementGroups(
             Collection<? extends MeasurementGroup> measurementGroups, List<UCoreMeasurementGroups> uCoreMeasurementGroupList) {
         for (MeasurementGroup measurementGroup : measurementGroups) {
             UCoreMeasurementGroups uCoreMeasurementGroups = new UCoreMeasurementGroups();

             List<UCoreMeasurement> uCoreMeasurementList = new ArrayList<>();
             List<UCoreMeasurementGroupDetail> uCoreMeasurementGroupDetails = new ArrayList<>();

             addToUCoreMeasurements(measurementGroup.getMeasurements(), uCoreMeasurementList);
             addToUCoreMeasurementGroupDetails(measurementGroup.getMeasurementGroupDetails(), uCoreMeasurementGroupDetails);
             measurementGroup.setDetails(uCoreMomentList);
             uCoreMoment.setMeasurements(uCoreMeasurementList);

             addToUCoreMeasurements();
             uCoreMeasurementGroupList.add(uCoreMeasurement);
         }
         return uCoreMeasurementGroupList;
     }*/

    List<UCoreMeasurementGroups> addToUCoreMeasurementGroups(Collection<? extends MeasurementGroup> measurementGroups, List<UCoreMeasurementGroups> uCoreMeasurementGroupList){
        for(MeasurementGroup measurementGroup: measurementGroups){
            UCoreMeasurementGroups uCoreMeasurementGroup = new UCoreMeasurementGroups();
            UCoreMeasurementGroups uCoreMeasurementGroups = convertMeasurementGroupToUCoreMeasurementGroup(measurementGroup, uCoreMeasurementGroup);
            uCoreMeasurementGroupList.add(uCoreMeasurementGroups);
        }
        return uCoreMeasurementGroupList;
    }

    UCoreMeasurementGroups convertMeasurementGroupToUCoreMeasurementGroup(MeasurementGroup measurementGroup, UCoreMeasurementGroups uCoreMeasurementGroups){
        //Added Details
        List<UCoreMeasurementGroupDetail> measurementGroupDetails = new ArrayList<>();
        List<UCoreMeasurementGroupDetail> uCoreMeasurementGroupDetails = addToUCoreMeasurementGroupDetails(measurementGroup.getMeasurementGroupDetails(), measurementGroupDetails);
        uCoreMeasurementGroups.setDetails(uCoreMeasurementGroupDetails);

        //Added Measurements
        List<UCoreMeasurement> measurements = new ArrayList<>();
        List<UCoreMeasurement> uCoreMeasurements = addToUCoreMeasurements(measurementGroup.getMeasurements(), measurements);
        uCoreMeasurementGroups.setMeasurements(uCoreMeasurements);

        return uCoreMeasurementGroups;
    }


    private void setVersion(final UCoreMoment uCoreMoment, final SynchronisationData synchronisationData) {
        if (synchronisationData != null) {
            uCoreMoment.setVersion(synchronisationData.getVersion());
        }
    }

    private void addToUCoreMomentDetails(Collection<? extends MomentDetail> momentDetails, List<UCoreDetail> momentDetailList) {

        for (MomentDetail momentDetail : momentDetails) {
            UCoreDetail detail = new UCoreDetail();
            detail.setType(momentTypeMap.getMomentDetailTypeString(momentDetail.getType()));
            detail.setValue(momentDetail.getValue());

            momentDetailList.add(detail);
        }
    }

    private List<UCoreMeasurementGroupDetail> addToUCoreMeasurementGroupDetails(Collection<? extends MeasurementGroupDetail> measurementGroupDetails,
                                                                                List<UCoreMeasurementGroupDetail> uCoreMeasurementGroupDetails) {

        for (MeasurementGroupDetail measurementGroupDetail : measurementGroupDetails) {
            UCoreMeasurementGroupDetail detail = new UCoreMeasurementGroupDetail();
            detail.setType(momentTypeMap.getMeasurementGroupDetailTypeString(measurementGroupDetail.getType()));
            detail.setValue(measurementGroupDetail.getValue());

            uCoreMeasurementGroupDetails.add(detail);
        }
        return uCoreMeasurementGroupDetails;
    }

    private List<UCoreMeasurement> addToUCoreMeasurements(Collection<? extends Measurement> measurements, List<UCoreMeasurement> uCoreMeasurementList) {
        for (Measurement measurement : measurements) {
            UCoreMeasurement uCoreMeasurement = new UCoreMeasurement();
            uCoreMeasurement.setTimestamp(measurement.getDateTime().toString());
            uCoreMeasurement.setType(momentTypeMap.getMeasurementTypeString(measurement.getType()));
            uCoreMeasurement.setValue(measurement.getValue());

            addToUCoreMeasurementDetails(measurement.getMeasurementDetails(), uCoreMeasurement);
            uCoreMeasurementList.add(uCoreMeasurement);
        }
        return uCoreMeasurementList;
    }

    private void addToUCoreMeasurementDetails(Collection<? extends MeasurementDetail> measurementDetails, com.philips.platform.datasync.moments.UCoreMeasurement uCoreMeasurement) {
        List<UCoreDetail> uCoreDetailList = new ArrayList<>();

        for (MeasurementDetail measurementDetail : measurementDetails) {
            UCoreDetail uCoreDetail = new UCoreDetail();
            uCoreDetail.setType(momentTypeMap.getMeasurementDetailTypeString(measurementDetail.getType()));

            uCoreDetail.setValue(measurementDetailValueMap.getString(measurementDetail.getType(), measurementDetail.getValue()));
            uCoreDetail.setValue(measurementDetail.getValue());

            uCoreDetailList.add(uCoreDetail);
        }
        uCoreMeasurement.setDetails(uCoreDetailList);
    }

}