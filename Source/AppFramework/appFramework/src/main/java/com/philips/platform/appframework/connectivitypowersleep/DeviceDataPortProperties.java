/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivitypowersleep;

import com.google.gson.annotations.SerializedName;
import com.philips.cdp2.commlib.core.port.PortProperties;

@SuppressWarnings("WeakerAccess")
public class DeviceDataPortProperties implements PortProperties {

    private final String KEY_NAME = "name";
    private final String KEY_TYPE = "type";
    private final String KEY_MODELID = "modelid";
    private final String KEY_ALLOW_UPLOADS = "allowuploads";
    private final String KEY_SERIAL = "serial";
    private final String KEY_CTN = "ctn";
    private final String KEY_UID = "udi";

    @SerializedName(KEY_NAME)
    private String deviceName;

    @SerializedName(KEY_TYPE)
    private String type;

    @SerializedName(KEY_MODELID)
    private String modelid;

    @SerializedName(KEY_ALLOW_UPLOADS)
    private String allowuploads;

    @SerializedName(KEY_SERIAL)
    private String serialnumber;

    @SerializedName(KEY_CTN)
    private String ctn;

    @SerializedName(KEY_UID)
    private String uid;

    public DeviceDataPortProperties() {
        // Default constructor for Gson
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceType() {
        return type;
    }

    public String getAllowUploads() {
        return allowuploads;
    }

    public String getSerialNumber() {
        return serialnumber;
    }

    public String getCTN() {
        return ctn;
    }

    public String getUID() {
        return uid;
    }
}
