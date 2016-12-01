package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;

import java.sql.SQLException;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DBSavingInterface {
    boolean saveMoment(final Moment moment) throws SQLException;

    //TODO: save Consent and saveBackened consent can they be merged to one API
    boolean saveConsent(final Consent consent) throws SQLException;
    boolean saveBackEndConsent(final Consent consent) throws SQLException;
}
