package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.listeners.DBRequestListener;

/**
 * Created by 310218660 on 1/2/2017.
 */

public class VerticalDBDeletingInterfaceImpl implements DBDeletingInterface{
    @Override
    public void deleteAllMoments(DBRequestListener dbRequestListener) {

    }

    @Override
    public void deleteMoment(Moment moment, DBRequestListener dbRequestListener) {

    }

    @Override
    public void ormDeletingDeleteMoment(Moment moment,DBRequestListener dbRequestListener) {

    }
}
