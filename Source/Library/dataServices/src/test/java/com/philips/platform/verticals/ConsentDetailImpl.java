package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.ConsentDetail;

/**
 * Created by 310218660 on 12/12/2016.
 */

public class ConsentDetailImpl implements ConsentDetail {

    int id;
    String type;
    String status;
    String version;
    String deviceIdentificationNumber;
    boolean beSynchronized;
    ConsentDetail ormConsentDetail;

    public ConsentDetailImpl(String type, String status, String version, String deviceIdentificationNumber, boolean isSynchronized, ConsentDetail consentDetail) {
        this.type = type;
        this.status = status;
        this.version = version;
        this.deviceIdentificationNumber = deviceIdentificationNumber;
        this.beSynchronized = isSynchronized;
        this.ormConsentDetail = consentDetail;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public void setDeviceIdentificationNumber(String deviceIdentificationNumber) {
        this.deviceIdentificationNumber = deviceIdentificationNumber;
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
    public String getVersion() {
        return version;
    }

    @Override
    public String getDeviceIdentificationNumber() {
        return deviceIdentificationNumber;
    }



    @Override
    public int getId() {
        return id;
    }
}
