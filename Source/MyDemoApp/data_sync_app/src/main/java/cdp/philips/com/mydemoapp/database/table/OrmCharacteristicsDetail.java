/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package cdp.philips.com.mydemoapp.database.table;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.Characteristics;

import java.io.Serializable;
import java.util.Collection;

import cdp.philips.com.mydemoapp.database.EmptyForeignCollection;
import cdp.philips.com.mydemoapp.database.annotations.DatabaseConstructor;


@DatabaseTable
public class OrmCharacteristicsDetail implements Characteristics, Serializable {

    public static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = true)
    private String type;

    @DatabaseField(canBeNull = true)
    private String value;

    @DatabaseField(canBeNull = false)
    private int parent;

    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = false)
    private OrmCharacteristics ormCharacteristics;

    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = true)
    private OrmCharacteristicsDetail ormCharacteristicsDetail;

    @ForeignCollectionField(eager = true)
    ForeignCollection<OrmCharacteristicsDetail> ormCharacteristicsDetails = new EmptyForeignCollection<>();

    @DatabaseConstructor
    OrmCharacteristicsDetail() {
    }

    public OrmCharacteristicsDetail(final String type, final String value, int parent, OrmCharacteristics ormCharacteristics, OrmCharacteristicsDetail ormCharacteristicsDetail) {
        this.type = type;
        this.value = value;
        this.parent = parent;
        this.ormCharacteristics = ormCharacteristics;
        this.ormCharacteristicsDetail = ormCharacteristicsDetail;
    }

    public OrmCharacteristicsDetail(final String type, final String value, int parent, OrmCharacteristics ormCharacteristics) {
        this.type = type;
        this.value = value;
        this.parent = parent;
        this.ormCharacteristics = ormCharacteristics;
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
    public void setParent(int parentId) {
        this.parent = parentId;
    }

    @Override
    public int getParent() {
        return parent;
    }

    @Override
    public Collection<? extends Characteristics> getCharacteristicsDetail() {
        return ormCharacteristicsDetails;
    }

    @Override
    public void setCharacteristicsDetail(Characteristics characteristics) {
        ormCharacteristicsDetails.add((OrmCharacteristicsDetail) characteristics);
    }

    @Override
    public String toString() {
        return "[OrmCharacteristicsDetail, id=" + id + ", type=" + type + ", value=" + value + ", OrmCharacteristics=" + ormCharacteristics + " ,ormCharacteristicsDetail=" + ormCharacteristicsDetail + "]";
    }
}
