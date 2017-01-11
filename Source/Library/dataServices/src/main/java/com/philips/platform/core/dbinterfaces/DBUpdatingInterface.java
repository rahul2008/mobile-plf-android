package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DBUpdatingInterface {
    void updateMoment(final Moment ormMoment,DBRequestListener dbRequestListener);
    int processMomentsReceivedFromBackend(final List<? extends Moment> moments,DBRequestListener dbRequestListener);
    void processCreatedMoment(final List<? extends Moment> moments,DBRequestListener dbRequestListener);
    void updateFailed(Exception e,DBRequestListener dbRequestListener);
    boolean updateConsent(final Consent consent,DBRequestListener dbRequestListener) throws SQLException;
}
