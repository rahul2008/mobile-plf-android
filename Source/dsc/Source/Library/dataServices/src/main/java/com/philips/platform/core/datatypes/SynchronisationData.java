/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

import org.joda.time.DateTime;

/**
 * Data-Base Interface for creating SynchronisationData Object
 */
public interface SynchronisationData {

    String getGuid();

    boolean isInactive();

    DateTime getLastModified();

    int getVersion();

    void setVersion(int version);

    void setInactive(boolean inActive);

    void setGuid(String guid);
}
