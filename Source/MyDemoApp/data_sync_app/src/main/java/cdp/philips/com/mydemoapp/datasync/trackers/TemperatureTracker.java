package cdp.philips.com.mydemoapp.datasync.trackers;

import android.content.Context;

import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementDetailType;
import com.philips.platform.core.datatypes.MeasurementType;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.events.LoadMomentsRequest;

import org.joda.time.DateTime;

import cdp.philips.com.mydemoapp.DataSyncApplicationClass;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TemperatureTracker extends BaseTracker{

   public TemperatureTracker(Context context, MomentType type){
       ((DataSyncApplicationClass) context.getApplicationContext()).getAppComponent().injectTemperature(this);
       mMomentType = type;
   }

    @Override
        public void createMomentDetail(String value){
        MomentDetail momentDetail = tracker.createMomentDetail(MomentDetailType.PHASE, mMoment);
        momentDetail.setValue(value);
    }

    @Override
     public void createMeasurement(String value){
        mMeasurement = tracker.createMeasurement(MeasurementType.TEMPERATURE, mMoment);
        mMeasurement.setValue(Integer.parseInt(value));
        mMeasurement.setDateTime(DateTime.now());
    }

    @Override
    public void createMeasurementDetail(String value){
        MeasurementDetail measurementDetail = tracker.createMeasurementDetail(MeasurementDetailType.LOCATION,mMeasurement);
        measurementDetail.setValue(value);
    }

    @Override
    public void fetchData(){
        eventing.post(new LoadMomentsRequest(MomentType.TEMPERATURE));
    }
}
