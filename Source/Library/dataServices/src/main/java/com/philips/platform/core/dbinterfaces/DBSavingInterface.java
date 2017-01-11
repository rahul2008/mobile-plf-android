package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DBSavingInterface {
    boolean saveMoment(final Moment moment, DBRequestListener dbRequestListener) throws SQLException;
    boolean saveConsent(final Consent consent,DBRequestListener dbRequestListener) throws SQLException;
    void postError(Exception e, DBRequestListener dbRequestListener);
    boolean saveUserCharacteristics(final Characteristics characteristics,DBRequestListener dbRequestListener) throws SQLException;
}
