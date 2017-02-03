package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DBUpdatingInterface {
    void updateMoment(final Moment ormMoment, DBRequestListener dbRequestListener) throws SQLException;

    boolean updateConsent(final List<? extends Consent> consentDetails, DBRequestListener dbRequestListener) throws SQLException;

    void updateFailed(Exception e, DBRequestListener dbRequestListener);

    boolean updateCharacteristics(final List<Characteristics> userCharacteristics, DBRequestListener dbRequestListener) throws SQLException;

    void updateSettings(Settings settings, DBRequestListener dbRequestListener) throws SQLException;

    boolean updateSyncBit(int tableID,boolean isSynced) throws SQLException;

}
