package com.philips.platform.verticals;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.SynchronisationData;

import org.joda.time.DateTime;

/**
 * Created by 310218660 on 12/12/2016.
 */

public class VerticalCreater implements BaseAppDataCreator{
    @NonNull
    @Override
    public Moment createMoment(@NonNull String creatorId, @NonNull String subjectId, @NonNull String type) {
        return null;
    }

    @NonNull
    @Override
    public Moment createMomentWithoutUUID(@NonNull String creatorId, @NonNull String subjectId, @NonNull String type) {
        return null;
    }

    @NonNull
    @Override
    public MomentDetail createMomentDetail(@NonNull String type, @NonNull Moment moment) {
        return null;
    }

    @Override
    public Measurement createMeasurement(@NonNull String type, @NonNull MeasurementGroup measurementGroup) {
        return null;
    }

    @NonNull
    @Override
    public MeasurementDetail createMeasurementDetail(@NonNull String type, @NonNull Measurement measurement) {
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull MeasurementGroup measurementGroup) {
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroup createMeasurementGroup(@NonNull Moment moment) {
        return null;
    }

    @NonNull
    @Override
    public MeasurementGroupDetail createMeasurementGroupDetail(@NonNull String type, @NonNull MeasurementGroup measurementGroup) {
        return null;
    }

    @NonNull
    @Override
    public SynchronisationData createSynchronisationData(@NonNull String guid, boolean inactive, @NonNull DateTime lastModifiedTime, int version) {
        return null;
    }

    @NonNull
    @Override
    public Consent createConsent(@NonNull String creatorId) {
        return null;
    }

    @NonNull
    @Override
    public ConsentDetail createConsentDetail(@NonNull String type, @NonNull String status, @NonNull String version, String deviceIdentificationNumber, boolean isSynchronized, @NonNull Consent consent) {
        return new ConsentDetailImpl(type,status,version,deviceIdentificationNumber,isSynchronized,consent);
    }
}
