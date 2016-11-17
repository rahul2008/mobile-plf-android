package cdp.philips.com.mydemoapp.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.philips.platform.core.datatypes.MeasurementDetailType;
import com.philips.platform.core.datatypes.MeasurementGroupDetailType;

import java.io.Serializable;

import cdp.philips.com.mydemoapp.database.annotations.DatabaseConstructor;

/**
 * Created by 310218660 on 11/17/2016.
 */

public class OrmMeasurementGroupDetailType implements Serializable{
    final long serialVersionId = 11L;
    static final long serialVersionUID = 11L;

    @DatabaseField(id = true, canBeNull = false)
    private int id;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseConstructor
    OrmMeasurementGroupDetailType() {
    }

    public OrmMeasurementGroupDetailType(final MeasurementGroupDetailType momentType) {
        this.id = momentType.getId();
        this.description = momentType.getDescription();
    }

    public MeasurementGroupDetailType getType() {
        return MeasurementGroupDetailType.fromId(id);
    }

    @Override
    public String toString() {
        return "[OrmMeasurementDetailType, id=" + id + ", description=" + description + "]";
    }
}
