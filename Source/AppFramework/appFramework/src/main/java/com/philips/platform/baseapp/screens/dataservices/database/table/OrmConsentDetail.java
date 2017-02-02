package com.philips.platform.baseapp.screens.dataservices.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.baseapp.screens.dataservices.database.annotations.DatabaseConstructor;
import com.philips.platform.core.datatypes.ConsentDetail;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@DatabaseTable
public class OrmConsentDetail implements ConsentDetail, Serializable {

    public static final long serialVersionUID = 11L;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String type;

    @DatabaseField(canBeNull = false)
    private String version;

    @DatabaseField(canBeNull = false)
    private String status;

    @DatabaseField(canBeNull = true)
    private boolean beSynchronized;

    @DatabaseField(canBeNull = false)
    private String deviceIdentificationNumber;

    @DatabaseField(foreign = true, foreignAutoRefresh = false, canBeNull = false)
    private OrmConsent ormConsent;

    @DatabaseConstructor
    OrmConsentDetail() {
    }

    public OrmConsentDetail(final String type, final String status, final String version, final String deviceIdentificationNumber, final OrmConsent ormConsent,boolean beSynchronized) {
        this.type = type;
        this.status = status;
        this.version = version;
        this.deviceIdentificationNumber = deviceIdentificationNumber;
        this.beSynchronized=beSynchronized;
        this.ormConsent = ormConsent;
    }

    @Override
    public int getId() {
        return id;
    }

   /* @Override
    public ConsentDetailType getTableType() {
        return type.getTableType();
    }*/

    @Override
    public String getType() {
        return type;
    }


    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getDeviceIdentificationNumber() {
        return deviceIdentificationNumber;
    }

    @Override
    public void setBackEndSynchronized(boolean backEndSynchronized) {
        this.beSynchronized = backEndSynchronized;
    }

    @Override
    public boolean getBackEndSynchronized() {
        return  this.beSynchronized ;
    }

    @Override
    public void setVersion(final String version) {
        this.version = version;
    }

    @Override
    public void setDeviceIdentificationNumber(final String deviceIdentificationNumber) {
        this.deviceIdentificationNumber = deviceIdentificationNumber;
    }

    @Override
    public String toString() {
        return "[OrmConsentDetail, id=" + id + ", OrmConsentDetailType=" + type + ", version=" + version + ", ormConsent=" + ormConsent + "]";
    }
}
