/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

import java.io.Serializable;

/**
 * Data-Base Interface for creating DCSync table
 */
public interface DCSync extends BaseAppData, DateData, Serializable {

    String DEFAULT_DOCUMENT_VERSION = "draft";
    String DEFAULT_DEVICE_IDENTIFICATION_NUMBER = "manual";
    String SMART_BABY_MONITOR = "smartBabyMonitor";

    String getTableType();

    boolean isSynced();

}
