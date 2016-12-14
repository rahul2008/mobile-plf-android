package com.philips.testing.verticals.table;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmConsent implements Consent, Serializable {

    public static final long serialVersionUID = 11L;

    private int id;

    private String creatorId;

    private DateTime dateTime = new DateTime();


    private List<OrmConsentDetail> ormConsentDetails = new ArrayList<>();

    public OrmConsent(@NonNull final String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public Collection<? extends OrmConsentDetail> getConsentDetails() {
        return ormConsentDetails;
    }


    @Override
    public void addConsentDetails(final ConsentDetail consentDetail) {
        ormConsentDetails.add((OrmConsentDetail) consentDetail);
    }

    @Override
    public String toString() {
        return "[OrmConsent, id=" + id + ", creatorId=" + creatorId + ", dateTime=" + dateTime + "]";
    }
}
