/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.appframework.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.SynchronisationData;

import org.joda.time.DateTime;

import java.io.Serializable;

import com.philips.platform.appframework.database.annotations.DatabaseConstructor;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@DatabaseTable
public class OrmSynchronisationData implements SynchronisationData, Serializable {

    public static final long serialVersionUID = 11L;

    @DatabaseField(id = true, canBeNull = false)
    private String guid;

    @DatabaseField
    private boolean inactive;

    @DatabaseField(canBeNull = false)
    private DateTime lastModified;

    @DatabaseField
    private int version;

    @DatabaseConstructor
    OrmSynchronisationData() {
    }

    public OrmSynchronisationData(final String guid, final boolean inactive, final DateTime lastModified, final int version) {
        this.guid = guid;
        this.inactive = inactive;
        this.lastModified = lastModified;
        this.version = version;
    }

    @Override
    public String getGuid() {
        return guid;
    }

    @Override
    public boolean isInactive() {
        return inactive;
    }

    @Override
    public DateTime getLastModified() {
        return lastModified;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void setVersion(final int version) {
        this.version = version;
    }

    @Override
    public void setInactive(final boolean inactive) {
        this.inactive = inactive;
    }

    @Override
    public String toString() {
        return "[OrmSynchronisationData, guid=" + guid + ", inactive=" + inactive + ", lastModified=" + lastModified + ", version=" + version + "]";
    }
}
