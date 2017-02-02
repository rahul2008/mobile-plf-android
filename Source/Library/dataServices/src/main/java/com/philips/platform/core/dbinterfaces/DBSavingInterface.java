package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
//TODO: Write an DBErrorInterface and all db interfaces will extend this
public interface DBSavingInterface {
    boolean saveMoment(final Moment moment, DBRequestListener dbRequestListener) throws SQLException;
    boolean saveConsent(final Consent consent,DBRequestListener dbRequestListener) throws SQLException;
    void postError(Exception e, DBRequestListener dbRequestListener);
    boolean saveUserCharacteristics(final List<Characteristics> userCharacteristics, DBRequestListener dbRequestListener) throws SQLException;
    boolean saveSettings(final Settings settings, DBRequestListener dbRequestListener) throws SQLException;
}
