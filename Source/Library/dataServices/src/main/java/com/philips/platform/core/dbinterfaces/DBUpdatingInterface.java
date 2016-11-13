package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.ConsentDetail;
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
    ConsentDetail getOrmConsentDetail(final ConsentDetail consentDetail);
    int processMomentsReceivedFromBackend(final List<? extends Moment> moments);
    void updateFailed(Exception e);
    void postRetrofitError(Throwable error);

    //TODO: save Consent ad saveBackened consent can they be merged to one API
    void updateConsentDetails(List<ConsentDetail> consentDetails);
}
