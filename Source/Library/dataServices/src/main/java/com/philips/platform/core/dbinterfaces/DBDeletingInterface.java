package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Moment;

import java.sql.SQLException;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DBDeletingInterface {
    void deleteAllMoments();
    void deleteMoment(Moment moment);
    void ormDeletingDeleteMoment(Moment moment);
    void deleteMomentDetail(Moment moment) throws SQLException;
    void deleteMeasurementGroup(Moment moment) throws SQLException;
}
