package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.Characteristics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrmCharacteristics implements Characteristics {

    public static final long serialVersionUID = 11L;

    private int id;

    private String type;

    private String value;

    private int parent;


    private OrmCharacteristics ormCharacteristics;

    List<OrmCharacteristics> ormCharacteristicses = new ArrayList<>();

    OrmCharacteristics() {
    }

    public OrmCharacteristics(final String type, final String value, OrmCharacteristics ormCharacteristicsDetail) {
        this.type = type;
        this.value = value;
        this.ormCharacteristics = ormCharacteristicsDetail;
        parent = 1;
    }

    public OrmCharacteristics(final String type, final String value) {
        this.type = type;
        this.value = value;
        parent = 0;
    }

    @Override
    public int getId() {
        return id;
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
    public Collection<? extends Characteristics> getCharacteristicsDetail() {
        return ormCharacteristicses;
    }

    @Override
    public void setCharacteristicsDetail(Characteristics characteristics) {
        ormCharacteristicses.add((OrmCharacteristics) characteristics);
    }

    @Override
    public int getParent() {
        return parent;
    }

}
