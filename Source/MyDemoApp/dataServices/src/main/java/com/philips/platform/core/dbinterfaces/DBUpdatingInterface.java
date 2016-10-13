package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Moment;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DBUpdatingInterface {
    int processMoment(int updatedCount, final Moment moment);
    void updateOrSaveMomentInDatabase(final Moment ormMoment);
    Moment getOrmMoment(final Moment moment);
    int processMomentsReceivedFromBackend(final List<? extends Moment> moments);
}
