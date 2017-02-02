package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.Characteristics;

import java.util.Collection;

/**
 * Created by indrajitkumar on 2/2/17.
 */
public class OrmCharacteristics implements Characteristics {
    public OrmCharacteristics(String type, String value, Characteristics characteristics) {
    }

    @Override
    public void setType(String type) {

    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public void setValue(String value) {

    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public Collection<? extends Characteristics> getCharacteristicsDetail() {
        return null;
    }

    @Override
    public void setCharacteristicsDetail(Characteristics characteristics) {

    }

    @Override
    public int getParent() {
        return 0;
    }

    @Override
    public int getId() {
        return 0;
    }
}
