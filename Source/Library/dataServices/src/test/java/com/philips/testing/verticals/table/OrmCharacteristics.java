
/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.testing.verticals.table;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.CharacteristicsDetail;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrmCharacteristics implements Characteristics, Serializable {

    public static final long serialVersionUID = 11L;

    private int id;

    private String creatorId;

   // private DateTime dateTime = new DateTime();

    private boolean beSynchronized;
    private List<OrmCharacteristicsDetail> ormCharacteristicsDetailList = new ArrayList<>();


    OrmCharacteristics() {
    }

    public OrmCharacteristics(@NonNull final String creatorId) {
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
    public Collection<? extends CharacteristicsDetail> getCharacteristicsDetails() {
        return ormCharacteristicsDetailList;
    }

    @Override
    public void addCharacteristicsDetail(CharacteristicsDetail characteristicsDetail) {
        ormCharacteristicsDetailList.add((OrmCharacteristicsDetail) characteristicsDetail);
    }

    @Override
    public boolean isSynchronized() {
        return false;
    }

    @Override
    public void setSynchronized(boolean isSynchronized) {

    }

    @Override
    public DateTime getDateTime() {
        return null;
    }

    @Override
    public String toString() {
        return "[OrmConsent, id=" + id + ", creatorId=" + creatorId + ", dateTime=" + null + "]";
    }
}
