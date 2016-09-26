package cdp.philips.com.mydemoapp.datasync.trackers;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.trackers.Tracker;

import org.joda.time.DateTime;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
abstract class BaseTracker {
    @Inject
    Tracker tracker;

    @Inject
    Eventing eventing;

    Moment mMoment;
    Measurement mMeasurement;
    MomentType mMomentType;

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

    protected abstract void createMeasurementDetail(final String location);

    protected abstract void createMeasurement(final String temperature);

    protected abstract void createMomentDetail(final String phase);

    protected abstract void fetchData();

    public Moment getMoment(){
        return mMoment;
    }

    public void saveRequest(){
        tracker.save(mMoment);
    }

    public void updateRequest(Moment moment){
        tracker.update(moment);
    }

    public void deleteRequest(){
        tracker.deleteMoment(mMoment);
    }

}
