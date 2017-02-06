package com.philips.platform.core.datatypes;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface ConsentDetail extends BaseAppData, Serializable {

    String DEFAULT_DOCUMENT_VERSION = "draft";
    String DEFAULT_DEVICE_IDENTIFICATION_NUMBER = "manual";
    String SMART_BABY_MONITOR = "smartBabyMonitor";

    void setStatus(String status);
    void setVersion(String version);
    void setDeviceIdentificationNumber(String deviceIdentificationNumber);
    //ConsentDetailType getTableType();
    String getType();

    String getStatus();
    String getVersion();

    String getDeviceIdentificationNumber();

}
