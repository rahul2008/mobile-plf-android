/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.datatypes;

import org.joda.time.DateTime;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface SynchronisationData {

    String getGuid();

    boolean isInactive();

    DateTime getLastModified();

    int getVersion();

    void setVersion(int version);

    void setInactive(boolean inActive);
}
