/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.networknode;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.appliance.Appliance;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;
import java.util.Observable;

import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_BOOT_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_CPP_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_DEVICE_NAME;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_DEVICE_TYPE;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_ENCRYPTION_KEY;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_HOME_SSID;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_HTTPS;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_IP_ADDRESS;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_IS_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_LAST_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_MODEL_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_PIN;

/**
 * A network node represents an appliance that was found by discovery.
 * <p>
 * It contains all the data that will be persisted.
 *
 * @publicApi
 */
public class NetworkNode extends Observable implements Parcelable {

    private static final int DICOMM_PROTOCOL_VERSION = 1;

    public enum PairingState {PAIRED, NOT_PAIRED, UNPAIRED, PAIRING}

    public interface EncryptionKeyUpdatedListener {
        void onKeyUpdate();
    }

    private boolean isHttps = true;
    @Deprecated
    private ConnectionState connectionState;
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

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private EncryptionKeyUpdatedListener encryptionKeyUpdatedListener;

    public NetworkNode() {
    }

    public void addPropertyChangeListener(final @NonNull PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final @NonNull PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    public synchronized String getIpAddress() {
        return ipAddress;
    }

    public synchronized void setIpAddress(String ipAddress) {
        final String oldIpAddress = this.ipAddress;
        this.ipAddress = ipAddress;
        this.pcs.firePropertyChange(KEY_IP_ADDRESS, oldIpAddress, ipAddress);
    }

    @NonNull
    public synchronized String getCppId() {
        return cppId;
    }

    public synchronized void setCppId(@NonNull String cppId) {
        final String oldCppId = this.cppId;
        this.cppId = cppId;
        this.pcs.firePropertyChange(KEY_CPP_ID, oldCppId, cppId);
    }

    @Deprecated
    public synchronized ConnectionState getConnectionState() {
        return connectionState;
    }

    @Deprecated
    public void setConnectionState(ConnectionState connectionState) {
        synchronized (this) { // notifyObservers called from same Thread
            if (connectionState.equals(this.connectionState)) return;
            this.connectionState = connectionState;
        }
        setChanged();
        notifyObservers();
    }

    public synchronized String getName() {
        return name;
    }

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

    public synchronized void setModelId(String modelId) {
        final String oldModelId = this.modelId;
        this.modelId = modelId;
        this.pcs.firePropertyChange(KEY_MODEL_ID, oldModelId, modelId);
    }

    public synchronized String getHomeSsid() {
        return homeSsid;
    }

    public synchronized void setHomeSsid(String homeSsid) {
        if (homeSsid == null || homeSsid.isEmpty()) return;

        final String oldHomeSsid = this.homeSsid;
        this.homeSsid = homeSsid;
        this.pcs.firePropertyChange(KEY_HOME_SSID, oldHomeSsid, homeSsid);
    }

    public synchronized long getBootId() {
        return bootId;
    }

    public synchronized void setBootId(long bootId) {
        synchronized (this) { // notifyObservers called from same Thread
            if (this.bootId == bootId) return;

            final long oldBootId = this.bootId;
            this.bootId = bootId;
            this.pcs.firePropertyChange(KEY_BOOT_ID, oldBootId, bootId);
        }
        setChanged();
        notifyObservers();
    }

    public synchronized String getEncryptionKey() {
        return encryptionKey;
    }

    public synchronized void setEncryptionKey(String encryptionKey) {
        boolean isKeyUpdated = !Objects.equals(this.encryptionKey, encryptionKey);

        final String oldEncryptionKey = this.encryptionKey;
        this.encryptionKey = encryptionKey;
        this.pcs.firePropertyChange(KEY_ENCRYPTION_KEY, oldEncryptionKey, encryptionKey);

        if (isKeyUpdated && encryptionKeyUpdatedListener != null) {
            encryptionKeyUpdatedListener.onKeyUpdate();
        }
    }

    public synchronized boolean isHttps() {
        return isHttps;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        final String oldPin = this.pin;
        this.pin = pin;
        this.pcs.firePropertyChange(KEY_PIN, oldPin, pin);
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

    public synchronized PairingState getPairedState() {
        return pairedState;
    }

    public synchronized void setPairedState(PairingState pairedState) {
        final PairingState oldPairingState = this.pairedState;
        this.pairedState = pairedState;
        this.pcs.firePropertyChange(KEY_IS_PAIRED, oldPairingState, pairedState);
    }

    public synchronized long getLastPairedTime() {
        return lastPairedTime;
    }

    public synchronized void setLastPairedTime(long lastPairedTime) {
        final long oldPairedTime = this.lastPairedTime;
        this.lastPairedTime = lastPairedTime;
        this.pcs.firePropertyChange(KEY_LAST_PAIRED, oldPairedTime, lastPairedTime);
    }

    public int getDICommProtocolVersion() {
        return DICOMM_PROTOCOL_VERSION;
    }

    protected NetworkNode(Parcel in) {
        ipAddress = in.readString();
        cppId = in.readString();
        connectionState = ConnectionState.values()[in.readInt()];
        name = in.readString();
        deviceType = in.readString();
        modelId = in.readString();
        homeSsid = in.readString();
        bootId = in.readLong();
        encryptionKey = in.readString();
        pairedState = PairingState.values()[in.readInt()];
        lastPairedTime = in.readLong();
        pin = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ipAddress);
        dest.writeString(cppId);
        dest.writeInt(connectionState.ordinal());
        dest.writeString(name);
        dest.writeString(deviceType);
        dest.writeString(modelId);
        dest.writeString(homeSsid);
        dest.writeLong(bootId);
        dest.writeString(encryptionKey);
        dest.writeInt(pairedState.ordinal());
        dest.writeLong(lastPairedTime);
        dest.writeString(pin);
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
                .append("   connectedState: ").append(getConnectionState())
                .append("   HomeSsid: ").append(getHomeSsid())
                .append("   pin: ").append(pin);
        return builder.toString();
    }

    public void setEncryptionKeyUpdatedListener(EncryptionKeyUpdatedListener encryptionKeyUpdatedListener) {
        this.encryptionKeyUpdatedListener = encryptionKeyUpdatedListener;
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
