package com.philips.platform.datasync.consent;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UCoreConsentDetail {

    private String name;

    private String documentVersion;

    private String status;

    public String getDeviceIdentificationNumber() {
        return deviceIdentificationNumber;
    }

    private String deviceIdentificationNumber;

    public UCoreConsentDetail(String name, String status, String version, final String deviceIdentificationNumber) {
        this.name = name;
        this.status = status;
        this.documentVersion = version;
        this.deviceIdentificationNumber = deviceIdentificationNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getDocumentVersion() {
        return documentVersion;
    }

    public String getName() {
        return name;
    }
}
