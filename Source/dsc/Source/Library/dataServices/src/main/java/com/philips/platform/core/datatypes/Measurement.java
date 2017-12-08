/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Collection;

/**
 * Data-Base Interface for creating Measurement Object
 */
public interface Measurement extends BaseAppData, DateData, Serializable {

    int getId();

    String getType();

    String getValue();

    void setValue(String value);

    DateTime getDateTime();

    void setDateTime(DateTime dateTime);

    Collection<? extends com.philips.platform.core.datatypes.MeasurementDetail> getMeasurementDetails();

    void addMeasurementDetail(com.philips.platform.core.datatypes.MeasurementDetail measurementDetail);

    MeasurementGroup getMeasurementGroup();

    String getUnit();

    void setUnit(String unit);

}
