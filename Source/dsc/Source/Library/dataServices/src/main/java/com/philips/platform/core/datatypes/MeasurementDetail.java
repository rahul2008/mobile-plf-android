/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

import java.io.Serializable;

/**
 * Data-Base Interface for creating MeasurementDetail Object
 */
public interface MeasurementDetail extends BaseAppData, Serializable {

    String getType();

    String getValue();

    void setValue(String value);

    Measurement getMeasurement();
}
