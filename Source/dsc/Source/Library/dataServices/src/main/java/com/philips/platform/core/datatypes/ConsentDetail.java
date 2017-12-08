/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.core.datatypes;

import java.io.Serializable;

/**
 * Data-Base interface for creating ConsentDetail Object
 */
public interface ConsentDetail extends BaseAppData, Serializable {

    String DEFAULT_DOCUMENT_VERSION = "draft";
    String DEFAULT_DEVICE_IDENTIFICATION_NUMBER = "manual";
    String SMART_BABY_MONITOR = "smartBabyMonitor";

    void setStatus(String status);

    void setVersion(String version);

    void setDeviceIdentificationNumber(String deviceIdentificationNumber);

    String getType();

    String getStatus();

    String getVersion();

    String getDeviceIdentificationNumber();

}
