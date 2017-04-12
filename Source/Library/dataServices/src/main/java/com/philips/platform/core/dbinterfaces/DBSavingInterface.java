package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.BlobRequestListener;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.datasync.blob.BlobMetaData;

import java.sql.SQLException;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
//TODO: Write an DBErrorInterface and all db interfaces will extend this
@SuppressWarnings({"rawtypes", "unchecked"})
public interface DBSavingInterface {
    boolean saveMoment(final Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException;

    boolean saveMoments(List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException;

    boolean saveConsentDetails(final List<ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) throws SQLException;

    void postError(Exception e, DBRequestListener dbRequestListener);

    boolean saveUserCharacteristics(final List<Characteristics> userCharacteristics, DBRequestListener<Characteristics> dbRequestListener) throws SQLException;
    boolean saveSettings(final Settings settings, DBRequestListener<Settings> dbRequestListener) throws SQLException;
    boolean saveInsights(final List<Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException;
}
