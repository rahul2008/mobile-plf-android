package com.philips.platform.verticals;

import com.philips.platform.core.datatypes.Consent;

/**
 * Created by 310218660 on 12/12/2016.
 */

public class ConsentImpl implements Consent {

    int id;
    String type;
    String status;
    String version;
    String deviceIdentificationNumber;
    boolean beSynchronized;
    Consent ormConsent;

    public ConsentImpl(String type, String status, String version, String deviceIdentificationNumber, boolean isSynchronized, Consent consent) {
        this.type = type;
        this.status = status;
        this.version = version;
        this.deviceIdentificationNumber = deviceIdentificationNumber;
        this.beSynchronized = isSynchronized;
        this.ormConsent = consent;
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
    public void setBackEndSynchronized(boolean b) {
        beSynchronized = b;
    }

    @Override
    public boolean getBackEndSynchronized() {
        return beSynchronized;
    }

    @Override
    public int getId() {
        return id;
    }
}
