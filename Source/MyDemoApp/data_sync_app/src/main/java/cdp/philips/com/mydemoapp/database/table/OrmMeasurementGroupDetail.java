package cdp.philips.com.mydemoapp.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;

import java.io.Serializable;

import cdp.philips.com.mydemoapp.database.annotations.DatabaseConstructor;

/**
 * Created by 310218660 on 11/17/2016.
 */

public class OrmMeasurementGroupDetail implements MeasurementGroupDetail, Serializable{

    public static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseConstructor
    OrmMeasurementGroupDetail() {
    }

    @Override
    public int getId() {
        return 0;
    }
}
