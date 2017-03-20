package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 310218660 on 1/2/2017.
 */

public class VerticalDBDeletingInterfaceImpl implements DBDeletingInterface {
    @Override
    public void deleteAll(DBRequestListener dbRequestListener) {

    }

    @Override
    public void markAsInActive(Moment moment, DBRequestListener dbRequestListener) {

    }

    @Override
    public void deleteMoment(Moment moment, DBRequestListener dbRequestListener) {

    }

    @Override
    public boolean deleteMoments(List<Moment> moments, DBRequestListener dbRequestListener) throws SQLException {

        return false;
    }

    @Override
    public void deleteMomentDetail(Moment moment, DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public void deleteMeasurementGroup(Moment moment, DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public void deleteFailed(Exception e, DBRequestListener dbRequestListener) {

    }

    @Override
    public void deleteAllMoments(DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public void markMomentsAsInActive(List<Moment> moment, DBRequestListener dbRequestListener) throws SQLException {

    }
}
