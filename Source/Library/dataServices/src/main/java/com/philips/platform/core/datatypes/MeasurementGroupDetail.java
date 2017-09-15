/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

import java.io.Serializable;

/**
 * DataBase Interface for creating MeasurementGroupDetail Object
 */
public interface MeasurementGroupDetail extends BaseAppData, Serializable {

    public String getType();

    public String getValue();

    public void setValue(final String value);

    public MeasurementGroup getOrmMeasurementGroup();
}
