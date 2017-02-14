package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DBDeletingInterface {
    void deleteAll(DBRequestListener dbRequestListener) throws SQLException;
    void markAsInActive(Moment moment,DBRequestListener dbRequestListener) throws SQLException;
    void deleteMoment(Moment moment,DBRequestListener dbRequestListener) throws  SQLException;
    void deleteMoments(List<Moment> moments, DBRequestListener dbRequestListener) throws SQLException;
    void deleteMomentDetail(Moment moment, DBRequestListener dbRequestListener) throws SQLException;
    void deleteMeasurementGroup(Moment moment,DBRequestListener dbRequestListener) throws SQLException;
    void deleteFailed(Exception e,DBRequestListener dbRequestListener);
    void deleteAllMoments(DBRequestListener dbRequestListener) throws SQLException;
    public void markMomentsAsInActive(final List<Moment> moment, DBRequestListener dbRequestListener) throws SQLException;
}
