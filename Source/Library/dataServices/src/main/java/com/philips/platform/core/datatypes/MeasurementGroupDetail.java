package com.philips.platform.core.datatypes;

import java.io.Serializable;

/**
 * Created by 310218660 on 11/17/2016.
 */

public interface MeasurementGroupDetail extends BaseAppData, Serializable {

    public MeasurementGroupDetailType getType();

    public String getValue();

    public void setValue(final String value);

    public MeasurementGroup getOrmMeasurementGroup();
}
