/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;


import com.philips.platform.appframework.connectivitypowersleep.datamodels.Summary;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.database.datatypes.MeasurementDetailType;
import com.philips.platform.dscdemo.database.datatypes.MeasurementGroupDetailType;
import com.philips.platform.dscdemo.database.datatypes.MeasurementType;
import com.philips.platform.dscdemo.database.datatypes.MomentDetailType;
import com.philips.platform.dscdemo.database.datatypes.MomentType;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class ConnectivityHelper {
    private static final String TAG = ConnectivityHelper.class.getSimpleName();
    private final int PROGRESS_SCORE_MAX = 360;
    private final int PROGRESS_PERCENTAGE_MAX = 100;
    private final int IDEAL_DEEP_SLEEP_TIME = 120;
    private final int PROGRESS_DRAW_TIME = 1500;

    protected Summary getSummaryInfoFromMoment(Moment moment){
        Summary summary=null;
        try {
            ArrayList<? extends MeasurementGroup> measurementGroupParent = new ArrayList<>(moment.getMeasurementGroups());
            ArrayList<? extends MeasurementGroup> measurementGroupChild = new ArrayList<>(measurementGroupParent.get(0).getMeasurementGroups());
            ArrayList<? extends Measurement> measurements = new ArrayList<>(measurementGroupChild.get(0).getMeasurements());
            String deepSleepTime = null,totalSleepTime = null;

            for(int count=0;count<measurements.size();count++){
                switch (count){
                    case 0:
                        deepSleepTime=measurements.get(0).getValue();
                        break;
                    case 1:
                        totalSleepTime=measurements.get(1).getValue();
                        break;
                }
            }
            Float dstFloat=Float.parseFloat(deepSleepTime);
            long dst=dstFloat.longValue();

            Float tstFloat=Float.parseFloat(totalSleepTime);
            long tst=tstFloat.longValue();

            summary=new Summary(new Date(moment.getDateTime().getMillis()),dst,tst);
            return summary;
        } catch (ArrayIndexOutOfBoundsException e) {
            RALog.e(TAG,"Error while converting moment data to summary"+e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            RALog.e(TAG,"Error while converting moment data to summary"+e.getMessage());
        } catch (Exception e) {
            RALog.e(TAG,"Error while converting moment data to summary"+e.getMessage());
        }
        return summary;
    }

    public int calculateDeepSleepScore(long deepSleepTime) {
        long deepSleepTimeInMinutes= TimeUnit.MILLISECONDS.toMinutes(deepSleepTime);
        return (int) (deepSleepTimeInMinutes * PROGRESS_PERCENTAGE_MAX) / IDEAL_DEEP_SLEEP_TIME;
    }


    protected Moment createMoment(String dstScore, String deepSleepTime, String sleepTime, DateTime dateTime, String measurementDetail) {
        DataServicesManager dataServicesManager=DataServicesManager.getInstance();
        Moment moment = dataServicesManager.createMoment(MomentType.SLEEP_SESSION);
        moment.setDateTime(dateTime);

        MeasurementGroup measurementGroupInside;
        MeasurementGroup measurementGroup;
        Measurement deepSleepTimeMeasurement;
        Measurement sleepTimeMeasurement;
        Measurement dstScoreMeasurement;
        dataServicesManager.createMomentDetail(MomentDetailType.DEVICE_SERIAL, "", moment);
        dataServicesManager.createMomentDetail(MomentDetailType.CTN, "", moment);
        dataServicesManager.createMomentDetail(MomentDetailType.FW_VERSION, "", moment);
        dataServicesManager.createMomentDetail(MomentDetailType.MOMENT_VERSION, "1", moment);
        measurementGroup = dataServicesManager.createMeasurementGroup(moment);
        dataServicesManager.createMeasurementGroupDetail(MeasurementGroupDetailType.REFERENCE_GROUP_ID, measurementDetail, measurementGroup);
        measurementGroupInside = dataServicesManager.createMeasurementGroup(measurementGroup);
        deepSleepTimeMeasurement = dataServicesManager.createMeasurement(MeasurementType.DST, deepSleepTime, MeasurementType.getUnitFromDescription(MeasurementType.DST), measurementGroupInside);
        sleepTimeMeasurement = dataServicesManager.createMeasurement(MeasurementType.TST, sleepTime, MeasurementType.getUnitFromDescription(MeasurementType.TST), measurementGroupInside);
        dstScoreMeasurement = dataServicesManager.createMeasurement(MeasurementType.DST_SCORE, dstScore, "%", measurementGroupInside);
        dataServicesManager.createMeasurementDetail(MeasurementDetailType.LOCATION, measurementDetail, deepSleepTimeMeasurement);
        measurementGroupInside.addMeasurement(deepSleepTimeMeasurement);
        measurementGroupInside.addMeasurement(sleepTimeMeasurement);
        measurementGroupInside.addMeasurement(dstScoreMeasurement);
        measurementGroup.addMeasurementGroup(measurementGroupInside);
        moment.addMeasurementGroup(measurementGroup);
        return moment;
    }
}
