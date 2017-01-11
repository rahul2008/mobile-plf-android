/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.testing.verticals.table;

import com.philips.platform.core.datatypes.CharacteristicsDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrmCharacteristicsDetail implements CharacteristicsDetail, Serializable {

    public static final long serialVersionUID = 11L;

    private int parent;

    private int id;

    private String type;

    private String value;


    private OrmCharacteristics ormCharacteristics;

    private OrmCharacteristicsDetail ormCharacteristicsDetail;
    private List<OrmCharacteristicsDetail> ormCharacteristicsDetails = new ArrayList<>();

    public OrmCharacteristicsDetail(final String type, final String value, int parent, OrmCharacteristics ormCharacteristics, OrmCharacteristicsDetail ormCharacteristicsDetail) {
        this.type = type;
        this.ormCharacteristics = ormCharacteristics;
        this.value = value;
        this.parent = ormCharacteristicsDetail.getId();
        this.ormCharacteristicsDetail = ormCharacteristicsDetail;
    }

    public OrmCharacteristicsDetail(final String type, final String value, int parent, OrmCharacteristics ormCharacteristics) {
        this.type = type;
        this.ormCharacteristics = ormCharacteristics;
        this.value = value;
        this.parent = parent;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setParent(int parentID) {

    }

    @Override
    public int getParent() {
        return parent;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Collection<? extends CharacteristicsDetail> getCharacteristicsDetail() {
        return ormCharacteristicsDetails;
    }

    @Override
    public void setCharacteristicsDetail(CharacteristicsDetail characteristicsDetail) {
        ormCharacteristicsDetails.add((OrmCharacteristicsDetail) characteristicsDetail);
    }

    @Override
    public String toString() {
        return "[OrmConsentDetail, id=" + id + ", type=" + type + ", value=" + value + ", OrmCharacteristics=" + ormCharacteristics + " ,ormCharacteristicsDetail=" + ormCharacteristicsDetail + "]";
    }
}
