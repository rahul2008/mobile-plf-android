package com.philips.platform.core.datatypes;

import java.io.Serializable;
import java.util.Collection;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DCSync extends BaseAppData, DateData, Serializable {

    String DEFAULT_DOCUMENT_VERSION = "draft";
    String DEFAULT_DEVICE_IDENTIFICATION_NUMBER = "manual";
    String SMART_BABY_MONITOR = "smartBabyMonitor";

    String getTableType();
    boolean isSynced();

}
