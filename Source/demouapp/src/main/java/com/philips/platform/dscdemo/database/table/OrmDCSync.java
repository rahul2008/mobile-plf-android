package com.philips.platform.dscdemo.database.table;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.DCSync;
import com.philips.platform.dscdemo.database.annotations.DatabaseConstructor;

import org.joda.time.DateTime;

import java.io.Serializable;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@DatabaseTable
public class OrmDCSync implements DCSync, Serializable {

    public static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = true)
    private int tableID;

    @DatabaseField(canBeNull = true)
    private String tableType;

    @DatabaseField(canBeNull = false)
    private DateTime dateTime = new DateTime();

    @DatabaseField(canBeNull = false)
    private boolean isSynced;


    @DatabaseConstructor
    OrmDCSync() {
    }

    public OrmDCSync(@NonNull final int tableID,@NonNull final String tableType,boolean isSynced) {
        this.tableType = tableType;
        this.isSynced=isSynced;
        this.tableID=tableID;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getTableType() {
        return tableType;
    }

    @Override
    public boolean isSynced() {
        return isSynced;
    }

    @Override
    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "[OrmConsentDetail, id=" + id + ", tableType=" + tableType + ", dateTime=" + dateTime + "]";
    }
}
