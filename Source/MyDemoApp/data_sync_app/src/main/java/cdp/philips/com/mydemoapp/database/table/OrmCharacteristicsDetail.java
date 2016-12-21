package cdp.philips.com.mydemoapp.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.CharacteristicsDetail;
import com.philips.platform.core.datatypes.ConsentDetail;

import java.io.Serializable;

import cdp.philips.com.mydemoapp.database.annotations.DatabaseConstructor;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@DatabaseTable
public class OrmCharacteristicsDetail implements CharacteristicsDetail, Serializable {

    public static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String type;

    @DatabaseField(canBeNull = false)
    private int parent;

    @DatabaseField(canBeNull = true)
    private String value;


    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = false)
    private OrmCharacteristics ormCharacteristics;

    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = true)
    private OrmCharacteristicsDetail ormCharacteristicsDetail;


    @DatabaseConstructor
    OrmCharacteristicsDetail() {
    }

    public OrmCharacteristicsDetail(final String type, final String value, int parent, OrmCharacteristics ormCharacteristics,OrmCharacteristicsDetail ormCharacteristicsDetail) {
        this.type = type;
        this.ormCharacteristics = ormCharacteristics;
        this.value = value;
        this.parent = ormCharacteristicsDetail.getId();
        this.ormCharacteristicsDetail=ormCharacteristicsDetail;
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
    public String toString() {
        return "[OrmConsentDetail, id=" + id + ", type=" + type + ", value=" + value + ", OrmCharacteristics=" + ormCharacteristics + " ,ormCharacteristicsDetail=" + ormCharacteristicsDetail +"]";
    }
}
