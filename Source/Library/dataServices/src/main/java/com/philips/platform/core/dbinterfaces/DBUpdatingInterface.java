package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;

import java.sql.SQLException;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DBUpdatingInterface {
    void updateMoment(final Moment ormMoment);
    void updateFailed(Exception e);
    boolean updateConsent(final Consent consent) throws SQLException;
}
