/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.dscdemo.database.table;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.dscdemo.database.EmptyForeignCollection;
import com.philips.platform.dscdemo.database.annotations.DatabaseConstructor;

import java.io.Serializable;
import java.util.Collection;

@DatabaseTable
public class OrmCharacteristics implements Characteristics, Serializable {

    public static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = true)
    private String type;

    @DatabaseField(canBeNull = true)
    private String value;

    @DatabaseField(canBeNull = true)
    private int parent;


    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = true)
    private OrmCharacteristics ormCharacteristics;

    @ForeignCollectionField(eager = true)
    ForeignCollection<OrmCharacteristics> ormCharacteristicses = new EmptyForeignCollection<>();

    @DatabaseConstructor
    OrmCharacteristics() {
    }

    public OrmCharacteristics(final String type, final String value, OrmCharacteristics ormCharacteristicsDetail) {
        this.type = type;
        this.value = value;
        this.ormCharacteristics = ormCharacteristicsDetail;
        parent=1;
    }

    public OrmCharacteristics(final String type, final String value) {
        this.type = type;
        this.value = value;
        parent=0;
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

    @Override
    public String toString() {
        return "[OrmCharacteristics, id=" + id + ", type=" + type + ", value=" + value + ", OrmCharacteristics=" + ormCharacteristics + " ,ormCharacteristics=" + ormCharacteristics + "]";
    }
}
