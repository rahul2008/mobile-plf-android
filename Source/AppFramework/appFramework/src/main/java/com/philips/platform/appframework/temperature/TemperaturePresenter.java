package com.philips.platform.appframework.temperature;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementDetailType;
import com.philips.platform.core.datatypes.MeasurementType;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.trackers.Tracker;

import org.joda.time.DateTime;

import javax.inject.Inject;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIView;
import com.philips.platform.modularui.stateimpl.URStateListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TemperaturePresenter  extends UIBasePresenter {
    @Inject
    Tracker tracker;

    Moment mMoment;
    Measurement mMeasurement;
    MomentType mMomentType;
    Context mContext;

    TemperaturePresenter(UIView uiView, Context context, MomentType momentType){
        super(uiView);
        ((AppFrameworkApplication) context.getApplicationContext()).getAppComponent().injectTemperature(this);
        mMomentType = momentType;
        mContext = context;
    }

    public void createMoment(String momemtDetail, String measurement, String measurementDetail){
        mMoment= tracker.createMoment(mMomentType);
        createMomentDetail(momemtDetail);
        createMeasurement(measurement);
        createMeasurementDetail(measurementDetail);
    }

    public void updateMoment(String momemtDetail, String measurement, String measurementDetail){
        mMoment= tracker.createMoment(mMomentType);
        mMoment.setDateTime(DateTime.now());
        createMomentDetail(momemtDetail);
        createMeasurement(measurement);
        createMeasurementDetail(measurementDetail);
    }

    public void createMeasurementDetail(String value){
        MeasurementDetail measurementDetail = tracker.createMeasurementDetail(MeasurementDetailType.LOCATION,mMeasurement);
        measurementDetail.setValue(value);
    }

    public void createMeasurement(String value){
        mMeasurement = tracker.createMeasurement(MeasurementType.TEMPERATURE, mMoment);
        mMeasurement.setValue(Integer.parseInt(value));
        mMeasurement.setDateTime(DateTime.now());
    }

    public void createMomentDetail(String value){
        MomentDetail momentDetail = tracker.createMomentDetail(MomentDetailType.PHASE, mMoment);
        momentDetail.setValue(value);
    }

    public void fetchData(){
        tracker.fetch(MomentType.TEMPERATURE);
    }

    public Moment getMoment(){
        return mMoment;
    }

    public void saveRequest(){
        if(mMoment.getCreatorId()==null || mMoment.getSubjectId()==null){
            Toast.makeText(mContext,"Please Login again", Toast.LENGTH_SHORT).show();
        }else {
            tracker.save(mMoment);
        }
    }

    public void startSync() {
        Log.i("***SPO***", "In Presenter");
        tracker.syncData();
    }


    @Override
    public void onClick(int componentID) {

    }

    @Override
    public void onLoad() {

    }

    public void createAndSaveMoment(final String phaseInput, final String temperatureInput, final String locationInput) {
        createMoment(phaseInput, temperatureInput, locationInput);
        saveRequest();
    }

}
