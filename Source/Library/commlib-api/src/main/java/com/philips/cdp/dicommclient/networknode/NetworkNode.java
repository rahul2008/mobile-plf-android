/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.networknode;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;

import com.philips.cdp2.commlib.core.appliance.Appliance;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_BOOT_ID;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_CPP_ID;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_DEVICE_NAME;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_DEVICE_TYPE;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_ENCRYPTION_KEY;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_HOME_SSID;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_HTTPS;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_IP_ADDRESS;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_IS_PAIRED;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_LAST_PAIRED;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_MISMATCHED_PIN;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_MODEL_ID;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_PIN;

/**
 * A network node represents an appliance that was found by discovery.
 * <p>
 * It contains all the data that will be persisted.
 *
 * @publicApi
 */
public class NetworkNode implements Parcelable {

    private static final int DICOMM_PROTOCOL_VERSION = 1;

    public enum PairingState {PAIRED, NOT_PAIRED, UNPAIRED, PAIRING}

    private boolean isHttps = true;
    private long bootId;
    private long lastPairedTime;
    private PairingState pairedState = PairingState.NOT_PAIRED;
    private String cppId;
    private String deviceType;
    private String encryptionKey;
    private String homeSsid;
    private String ipAddress;
    private String modelId;
    private String name;
    private String pin;
    private String mismatchedPin;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public NetworkNode() {
    }

    /**
     * Adds a listener for property changes
     * @param listener PropertyChangeListener
     */
    public void addPropertyChangeListener(final @NonNull PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * Removes a listener for property changes
     * @param listener PropertyChangeListener
     */
    public void removePropertyChangeListener(final @NonNull PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * Returns the IP Address for the NetworkNode.
     * Is empty when the NetworkNode represents a BLE appliance.
     * @return String The IP Address
     */
    public synchronized String getIpAddress() {
        return ipAddress;
    }

    /**
     * Allows to set the IP Address for the NetworkNode
     * @param ipAddress String
     */
    public synchronized void setIpAddress(String ipAddress) {
        final String oldIpAddress = this.ipAddress;
        this.ipAddress = ipAddress;
        this.pcs.firePropertyChange(KEY_IP_ADDRESS, oldIpAddress, ipAddress);
    }

    /**
     * Returns the unique identifier for the NetworkNode.
     * Will contain a MAC address when the NetworkNode represents a BLE Appliance
     * @return String
     */
    @NonNull
    public synchronized String getCppId() {
        return cppId;
    }

    /**
     * Allows to set the unique identifier for the NetworkNode
     * @param cppId String
     */
    public synchronized void setCppId(@NonNull String cppId) {
        final String oldCppId = this.cppId;
        this.cppId = cppId;
        this.pcs.firePropertyChange(KEY_CPP_ID, oldCppId, cppId);
    }

    /**
     * Returns the appliance's name
     * @return String
     */
    public synchronized String getName() {
        return name;
    }

    /**
     *  Allows to set the appliance's name
     * @param name String
     */
    public synchronized void setName(String name) {
        final String oldName = this.name;
        this.name = name;
        this.pcs.firePropertyChange(KEY_DEVICE_NAME, oldName, name);
    }

    /**
     * The device type defines the category of the product (e.g. AirPurifier)
     * Different products can have the same device type, but their model id
     * will be different.
     *
     * @return device type
     */
    public synchronized String getDeviceType() {
        return deviceType;
    }

    /**
     * Set the device type.
     *
     * @param deviceType The device type
     * @see #getDeviceType()
     */
    public synchronized void setDeviceType(String deviceType) {
        final String oldDeviceType = this.deviceType;
        this.deviceType = deviceType;
        this.pcs.firePropertyChange(KEY_DEVICE_TYPE, oldDeviceType, deviceType);
    }

    /**
     * The model id defines one particular type of product (e.g. AC7342).
     * Different products will have a different model id, but their
     * device type can be the same.
     *
     * @return model id
     */
    public synchronized String getModelId() {
        return modelId;
    }

    /**
     * Set the model id.
     *
     * @param modelId The model id
     * @see #getModelId()
     */
    public synchronized void setModelId(String modelId) {
        final String oldModelId = this.modelId;
        this.modelId = modelId;
        this.pcs.firePropertyChange(KEY_MODEL_ID, oldModelId, modelId);
    }

    /**
     * Returns the SSID where the appliance is at home.
     * @return String
     */
    public synchronized String getHomeSsid() {
        return homeSsid;
    }

    /**
     * Allows to set the appliance's home SSID.
     * @param homeSsid String
     */
    public synchronized void setHomeSsid(String homeSsid) {
        if (homeSsid == null || homeSsid.isEmpty()) return;

        final String oldHomeSsid = this.homeSsid;
        this.homeSsid = homeSsid;
        this.pcs.firePropertyChange(KEY_HOME_SSID, oldHomeSsid, homeSsid);
    }

    /**
     * Returns the appliance's bootId.
     * The bootId indicates the number of times the appliance had a power cycle.
     * @return long
     */
    public synchronized long getBootId() {
        return bootId;
    }

    /**
     * Allows to set the appliance's bootId
     * @param bootId long
     */
    public synchronized void setBootId(long bootId) {
        final long oldBootId = this.bootId;
        this.bootId = bootId;
        this.pcs.firePropertyChange(KEY_BOOT_ID, oldBootId, bootId);
    }

    /**
     * Returns the encryption key from the appliance.
     * @return String
     */
    public synchronized String getEncryptionKey() {
        return encryptionKey;
    }

    /**
     * Allows to set the encryption key for the appliance.
     * @param encryptionKey String
     */
    public synchronized void setEncryptionKey(String encryptionKey) {
        final String oldEncryptionKey = this.encryptionKey;
        this.encryptionKey = encryptionKey;
        this.pcs.firePropertyChange(KEY_ENCRYPTION_KEY, oldEncryptionKey, encryptionKey);
    }

    /**
     * Indicates whether the appliance uses the HTTPS protocol to communicate.
     * @return boolean
     */
    public synchronized boolean isHttps() {
        return isHttps;
    }

    /**
     * Returns the pin for HTTPS certificate pinning.
     * @return String
     */
    public String getPin() {
        return pin;
    }

    /**
     * Allows to set the pin for HTTPS certificate pinning.
     * @param pin String
     */
    public void setPin(@Nullable String pin) {
        final String oldPin = this.pin;
        this.pin = pin;
        this.pcs.firePropertyChange(KEY_PIN, oldPin, pin);
    }

    /**
     * Returns a pin if it was mismatched earlier in HTTPS certificate pinning.
     * @return String
     */
    public String getMismatchedPin() {
        return mismatchedPin;
    }

    /**
     * Allows to set a mismatched pin after failed HTTPS certificate pinning.
     * @param mismatchedPin String
     */
    public void setMismatchedPin(@Nullable String mismatchedPin) {
        final String oldMismatchedPin = this.mismatchedPin;
        this.mismatchedPin = mismatchedPin;
        this.pcs.firePropertyChange(KEY_MISMATCHED_PIN, oldMismatchedPin, mismatchedPin);
    }

    /**
     * Indicate that the {@link Appliance} belonging to this {@link NetworkNode} only supports legacy HTTP connections over lan.
     * <p>
     * If the appliance belonging to this {@link NetworkNode} supports HTTPS you should NOT call this, ever. As legacy HTTP is a deprecated technology within diComm, this is only here to support older devices which have not (or cannot) be updated to use HTTPS.
     */
    public synchronized void useLegacyHttp() {
        if (isHttps) {
            this.isHttps = false;
            this.pcs.firePropertyChange(KEY_HTTPS, true, false);
        }
    }

    /**
     * Returns the {@link PairingState} between the phone and the appliance in DeviceCloud.
     * @return PairingState
     */
    public synchronized PairingState getPairedState() {
        return pairedState;
    }

    /**
     * Allows to update the paired state locally.
     * @param pairedState PairedState
     */
    public synchronized void setPairedState(PairingState pairedState) {
        final PairingState oldPairingState = this.pairedState;
        this.pairedState = pairedState;
        this.pcs.firePropertyChange(KEY_IS_PAIRED, oldPairingState, pairedState);
    }

    /**
     * Returns timestamp of the moment the appliance has paired in DeviceCloud the last time.
     * @return long
     */
    public synchronized long getLastPairedTime() {
        return lastPairedTime;
    }

    /**
     * Allows to set the timestamp of the moment the appliance has last paired in DeviceCloud.
     * @param lastPairedTime long
     */
    public synchronized void setLastPairedTime(long lastPairedTime) {
        final long oldPairedTime = this.lastPairedTime;
        this.lastPairedTime = lastPairedTime;
        this.pcs.firePropertyChange(KEY_LAST_PAIRED, oldPairedTime, lastPairedTime);
    }

    /**
     * Updates the current NetworkNode with values of a different NetworkNode.
     * @param networkNode NetworkNode The updated NetworkNode to read from
     */
    public void updateWithValuesFrom(final @NonNull NetworkNode networkNode) {
        if (!Objects.equals(networkNode.getCppId(), this.cppId)) {
            return;
        }

        if (!Objects.equals(networkNode.getHomeSsid(), this.homeSsid)) {
            setHomeSsid(networkNode.getHomeSsid());
        }

        if (!Objects.equals(networkNode.getIpAddress(), this.ipAddress)) {
            setIpAddress(networkNode.getIpAddress());
        }

        if (!Objects.equals(networkNode.getName(), this.name)) {
            setName(networkNode.getName());
        }

        if (!Objects.equals(networkNode.getModelId(), this.modelId)) {
            setModelId(networkNode.getModelId());
        }

        if (!Objects.equals(networkNode.getDeviceType(), this.deviceType)) {
            setDeviceType(networkNode.getDeviceType());
        }

        if (networkNode.getBootId() != this.bootId) {
            setEncryptionKey(null);
            setBootId(networkNode.getBootId());
        }

        if (networkNode.getEncryptionKey() != null) {
            setEncryptionKey(null);
        }
    }

    /**
     * Returns the DIComm version currently used.
     * @return int The DIComm version
     */
    public int getDICommProtocolVersion() {
        return DICOMM_PROTOCOL_VERSION;
    }

    protected NetworkNode(Parcel in) {
        ipAddress = in.readString();
        cppId = in.readString();
        name = in.readString();
        deviceType = in.readString();
        modelId = in.readString();
        homeSsid = in.readString();
        bootId = in.readLong();
        encryptionKey = in.readString();
        pairedState = PairingState.values()[in.readInt()];
        lastPairedTime = in.readLong();
        pin = in.readString();
        mismatchedPin = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ipAddress);
        dest.writeString(cppId);
        dest.writeString(name);
        dest.writeString(deviceType);
        dest.writeString(modelId);
        dest.writeString(homeSsid);
        dest.writeLong(bootId);
        dest.writeString(encryptionKey);
        dest.writeInt(pairedState.ordinal());
        dest.writeLong(lastPairedTime);
        dest.writeString(pin);
        dest.writeString(mismatchedPin);
    }

    public static final Parcelable.Creator<NetworkNode> CREATOR = new Parcelable.Creator<NetworkNode>() {
        @Override
        public NetworkNode createFromParcel(Parcel in) {
            return new NetworkNode(in);
        }

        @Override
        public NetworkNode[] newArray(int size) {
            return new NetworkNode[size];
        }
    };

    /**
     * Returns a pairing status based on a given status.
     * Will return NOT_PAIRED when no known state for given status.
     * @param status int The status to find the PairingState for
     * @return PairingState The state corresponding to the given status
     */
    public static PairingState getPairedStatusKey(int status) {
        if (status >= 0 && status < PairingState.values().length) {
            return PairingState.values()[status];
        }
        return PairingState.NOT_PAIRED;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("name: ").append(getName())
                .append("   ipAddress: ").append(getIpAddress())
                .append("   cppId: ").append(getCppId())
                .append("   bootId: ").append(getBootId())
                .append("   deviceType: ").append(getDeviceType())
                .append("   modelId: ").append(getModelId())
                .append("   paired: ").append(getPairedState())
                .append("   homeSsid: ").append(getHomeSsid())
                .append("   pin: ").append(getPin())
                .append("   mismatchedPin: ").append(getMismatchedPin());
        return builder.toString();
    }

    /**
     * Checks if this {@link NetworkNode} has the minimal set of its properties set to a valid value.
     *
     * @return true, if this {@link NetworkNode} is valid.
     */
    public boolean isValid() {
        boolean isValid = !TextUtils.isEmpty(getCppId())
                && !TextUtils.isEmpty(getName())
                && !TextUtils.isEmpty(getDeviceType());

        if (!TextUtils.isEmpty(getIpAddress())) {
            isValid &= Patterns.IP_ADDRESS.matcher(getIpAddress()).matches();
        }
        return isValid;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return cppId.equals(((NetworkNode) other).cppId);
    }

    @Override
    public int hashCode() {
        return cppId.hashCode();
    }
}
