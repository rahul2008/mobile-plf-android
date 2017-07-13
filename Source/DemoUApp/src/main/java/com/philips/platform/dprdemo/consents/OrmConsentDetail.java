/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.dprdemo.consents;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.core.datatypes.ConsentDetail;

import java.io.Serializable;

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


    @DatabaseField(canBeNull = false)
    private String deviceIdentificationNumber;

    @DatabaseConstructor
    OrmConsentDetail() {
    }

    public OrmConsentDetail(final String type, final String status, final String version, final String deviceIdentificationNumber) {
        this.type = type;
        this.status = status;
        this.version = version;
        this.deviceIdentificationNumber = deviceIdentificationNumber;
    }

    @Override
    public int getId() {
        return id;
    }

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
    public void setVersion(final String version) {
        this.version = version;
    }

    @Override
    public void setDeviceIdentificationNumber(final String deviceIdentificationNumber) {
        this.deviceIdentificationNumber = deviceIdentificationNumber;
    }

    @Override
    public String toString() {
        return "[OrmConsentDetail, id =" + id + ", OrmConsentDetailType =" + type + ", version =" + version + "]";
    }
}
