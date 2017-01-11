package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DBDeletingInterface {
    void deleteAllMoments(DBRequestListener dbRequestListener);
    void markAsInActive(Moment moment,DBRequestListener dbRequestListener);
    void deleteMoment(Moment moment,DBRequestListener dbRequestListener);
    void deleteMomentDetail(Moment moment,DBRequestListener dbRequestListener) throws SQLException;
    void deleteMeasurementGroup(Moment moment,DBRequestListener dbRequestListener) throws SQLException;
}
