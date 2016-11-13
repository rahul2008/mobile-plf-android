package com.philips.platform.core.datatypes;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface ConsentDetail extends BaseAppData, Serializable {

    void setStatus(String status);
    void setVersion(String version);

    //TODO: Spoorti: remove if not required
    void setDeviceIdentificationNumber(String deviceIdendificationNumber);

    ConsentDetailType getType();

    String getStatus();
    String getVersion();

    String getDeviceIdentificationNumber();

    void setBackEndSynchronized(boolean b);
    boolean getBackEndSynchronized();
}
