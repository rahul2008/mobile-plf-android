package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.BlobRequestListener;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.datasync.blob.BlobMetaData;

import java.sql.SQLException;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public interface DBUpdatingInterface {
    void updateMoment(final Moment ormMoment, DBRequestListener<Moment> dbRequestListener) throws SQLException;

    boolean updateMoments(final List<Moment> ormMoments, DBRequestListener<Moment> dbRequestListener) throws SQLException;

    boolean updateConsent(final List<? extends ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) throws SQLException;

    void updateFailed(Exception e, DBRequestListener dbRequestListener);

    boolean updateCharacteristics(final List<Characteristics> userCharacteristics, DBRequestListener<Characteristics> dbRequestListener) throws SQLException;

    void updateSettings(Settings settings, DBRequestListener<Settings> dbRequestListener) throws SQLException;

    boolean updateSyncBit(int tableID,boolean isSynced) throws SQLException;

    boolean updateInsights(final List<? extends Insight> insights ,DBRequestListener<Insight> dbRequestListener) throws SQLException;

    boolean updateBlobMetaData(BlobMetaData blobMetaData, BlobRequestListener blobRequestListener) throws SQLException;
}
