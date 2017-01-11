package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBRequestListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DBDeletingInterface {
    void deleteAllMoments(DBRequestListener dbRequestListener);
    void deleteMoment(Moment moment, DBRequestListener dbRequestListener);
    void ormDeletingDeleteMoment(Moment moment,DBRequestListener dbRequestListener);
}
