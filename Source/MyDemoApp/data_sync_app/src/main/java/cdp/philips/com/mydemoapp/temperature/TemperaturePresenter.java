package cdp.philips.com.mydemoapp.temperature;

import android.content.Context;

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

import cdp.philips.com.mydemoapp.DataSyncApplication;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TemperaturePresenter {
    @Inject
    Tracker tracker;

    Moment mMoment;
    Measurement mMeasurement;
    MomentType mMomentType;

    TemperaturePresenter(Context context, MomentType momentType){
        ((DataSyncApplication) context.getApplicationContext()).getAppComponent().injectTemperature(this);
        mMomentType = momentType;
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
        tracker.save(mMoment);
    }

    public void startSync() {
        tracker.synchronize();
    }
}
