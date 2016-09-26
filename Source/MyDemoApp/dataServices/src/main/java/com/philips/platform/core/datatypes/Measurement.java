/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Collection;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface Measurement extends BaseAppData, DateData, Serializable {

    int getId();

    com.philips.platform.core.datatypes.MeasurementType getType();

    double getValue();

    void setValue(double value);

    DateTime getDateTime();

    void setDateTime(DateTime dateTime);

    Collection<? extends com.philips.platform.core.datatypes.MeasurementDetail> getMeasurementDetails();

    void addMeasurementDetail(com.philips.platform.core.datatypes.MeasurementDetail measurementDetail);

    com.philips.platform.core.datatypes.Moment getMoment();

}
