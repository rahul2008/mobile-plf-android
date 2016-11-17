package cdp.philips.com.mydemoapp.database.table;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementGroup;

import java.io.Serializable;

import cdp.philips.com.mydemoapp.database.EmptyForeignCollection;
import cdp.philips.com.mydemoapp.database.annotations.DatabaseConstructor;

/**
 * Created by 310218660 on 11/17/2016.
 */

public class OrmMeasurementGroup implements MeasurementGroup, Serializable {
    public static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = false)
    private OrmMoment ormMoment;

    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = false)
    private OrmMeasurementGroup ormMeasurementGroup;

    @ForeignCollectionField(eager = true)
    ForeignCollection<OrmMeasurementGroup> ormMeasurementGroups = new EmptyForeignCollection<>();

    @Override
    public int getId() {
        return 0;
    }

    @DatabaseConstructor
    OrmMeasurementGroup() {
    }
}
