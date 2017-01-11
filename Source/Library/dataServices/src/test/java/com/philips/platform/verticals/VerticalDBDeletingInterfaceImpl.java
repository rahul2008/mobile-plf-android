package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;

/**
 * Created by 310218660 on 1/2/2017.
 */

public class VerticalDBDeletingInterfaceImpl implements DBDeletingInterface{
    @Override
    public void deleteAllMoments(DBRequestListener dbRequestListener) {

    }

    @Override
    public void markAsInActive(Moment moment, DBRequestListener dbRequestListener) {

    }

    @Override
    public void deleteMoment(Moment moment, DBRequestListener dbRequestListener) {

    }

    @Override
    public void deleteMomentDetail(Moment moment, DBRequestListener dbRequestListener) throws SQLException {

    }

    @Override
    public void deleteMeasurementGroup(Moment moment, DBRequestListener dbRequestListener) throws SQLException {

    }
}
