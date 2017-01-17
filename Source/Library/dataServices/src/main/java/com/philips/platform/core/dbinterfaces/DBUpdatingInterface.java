package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.UserCharacteristics;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DBUpdatingInterface {
    void updateMoment(final Moment ormMoment, DBRequestListener dbRequestListener) throws SQLException;

    boolean updateConsent(final Consent consent, DBRequestListener dbRequestListener) throws SQLException;

    void updateFailed(Exception e, DBRequestListener dbRequestListener);

    boolean updateCharacteristics(final UserCharacteristics userCharacteristics, DBRequestListener dbRequestListener) throws SQLException;

    void processCharacteristicsReceivedFromDataCore(final UserCharacteristics userCharacteristics, DBRequestListener dbRequestListener) throws SQLException;
}
