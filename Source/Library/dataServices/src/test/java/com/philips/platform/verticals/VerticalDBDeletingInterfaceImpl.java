package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;

import java.sql.SQLException;

/**
 * Created by 310218660 on 1/2/2017.
 */

public class VerticalDBDeletingInterfaceImpl implements DBDeletingInterface{
    @Override
    public void deleteAllMoments() {

    }

    @Override
    public void markAsInActive(Moment moment) {

    }

    @Override
    public void deleteMoment(Moment moment) {

    }

    @Override
    public void deleteMomentDetail(Moment moment) throws SQLException {

    }

    @Override
    public void deleteMeasurementGroup(Moment moment) throws SQLException {

    }
}
