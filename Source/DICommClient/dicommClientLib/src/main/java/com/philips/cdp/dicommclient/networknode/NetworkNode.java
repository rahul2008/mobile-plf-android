/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.networknode;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.appliance.Appliance;

import java.util.Observable;

/**
 * A network node represents an appliance that was found by discovery.
 * <p>
 * It contains all the data that will be persisted.
 *
 * @publicApi
 */
public class NetworkNode extends Observable implements Parcelable {
    public enum PAIRED_STATUS {PAIRED, NOT_PAIRED, UNPAIRED, PAIRING}

    public interface EncryptionKeyUpdatedListener {
        void onKeyUpdate();
    }

    private String mIpAddress;
    @NonNull
    private String mCppId;
    @Deprecated
    private ConnectionState mConnectionState;

    private String mName;
    private String mDeviceType;
    private String mModelId;
    private String mHomeSsid;
    private long mBootId;
    private String mEncryptionKey;
    private boolean mHttps = true;

    private PAIRED_STATUS mPairedState = PAIRED_STATUS.NOT_PAIRED;
    private long mLastPairedTime;

    private final int mDICommProtocolVersion = 1;

    private EncryptionKeyUpdatedListener encryptionKeyUpdatedListener;

    public NetworkNode() {
    }

    public synchronized String getIpAddress() {
        return mIpAddress;
    }

    public synchronized void setIpAddress(String ipAddress) {
        this.mIpAddress = ipAddress;
    }

    @NonNull
    public synchronized String getCppId() {
        return mCppId;
    }

    public synchronized void setCppId(@NonNull String cppId) {
        this.mCppId = cppId;
    }

    @Deprecated
    public synchronized ConnectionState getConnectionState() {
        return mConnectionState;
    }

    @Deprecated
    public void setConnectionState(ConnectionState connectionState) {
        synchronized (this) { // notifyObservers called from same Thread
            if (connectionState.equals(mConnectionState)) return;
            this.mConnectionState = connectionState;
        }
        setChanged();
        notifyObservers();
    }

    public synchronized String getName() {
        return mName;
    }

    public synchronized void setName(String name) {
        this.mName = name;
    }

    /**
     * The device type defines the category of the product (e.g. AirPurifier)
     * Different products can have the same device type, but their model id
     * will be different.
     *
     * @return device type
     */
    public synchronized String getDeviceType() {
        return mDeviceType;
    }

    public synchronized void setDeviceType(String deviceType) {
        this.mDeviceType = deviceType;
    }

    /**
     * The model id defines one particular type of product (e.g. AC7342).
     * Different products will have a different model id, but their
     * device type can be the same.
     *
     * @return model id
     */
    public synchronized String getModelId() {
        return mModelId;
    }

    public synchronized void setModelId(String modelId) {
        this.mModelId = modelId;
    }

    public synchronized String getHomeSsid() {
        return mHomeSsid;
    }

    public synchronized void setHomeSsid(String homeSsid) {
        if (homeSsid == null || homeSsid.isEmpty()) return;
        this.mHomeSsid = homeSsid;
    }

    public synchronized long getBootId() {
        return mBootId;
    }

    public synchronized void setBootId(long bootId) {
        synchronized (this) { // notifyObservers called from same Thread
            if (mBootId == bootId) return;
            this.mBootId = bootId;
        }
        setChanged();
        notifyObservers();
    }

    public synchronized String getEncryptionKey() {
        return mEncryptionKey;
    }

    public synchronized void setEncryptionKey(String encryptionKey) {
        boolean isKeyUpdated = mEncryptionKey != encryptionKey;
        this.mEncryptionKey = encryptionKey;
        if (isKeyUpdated && encryptionKeyUpdatedListener != null) {
            encryptionKeyUpdatedListener.onKeyUpdate();
        }
    }

    public synchronized boolean getHttps() {
        return mHttps;
    }

    /**
     * Indicate that the {@link Appliance} belonging to this {@link NetworkNode} only supports legacy HTTP connections over lan.
     * <p>
     * If the appliance belonging to this {@link NetworkNode} supports HTTPS you should NOT call this, ever. As legacy HTTP is a deprecated technology within diComm, this is only here to support older devices which have not (or cannot) be updated to use HTTPS.
     */
    public synchronized void useLegacyHttp() {
        this.mHttps = false;
    }

    public synchronized NetworkNode.PAIRED_STATUS getPairedState() {
        return mPairedState;
    }

    public synchronized void setPairedState(NetworkNode.PAIRED_STATUS pairedState) {
        this.mPairedState = pairedState;
    }

    public synchronized long getLastPairedTime() {
        return mLastPairedTime;
    }

    public synchronized void setLastPairedTime(long lastPairedTime) {
        this.mLastPairedTime = lastPairedTime;
    }

    public int getDICommProtocolVersion() {
        return mDICommProtocolVersion;
    }

    protected NetworkNode(Parcel in) {
        mIpAddress = in.readString();
        mCppId = in.readString();
        mConnectionState = ConnectionState.values()[in.readInt()];
        mName = in.readString();
        mDeviceType = in.readString();
        mModelId = in.readString();
        mHomeSsid = in.readString();
        mBootId = in.readLong();
        mEncryptionKey = in.readString();
        mPairedState = PAIRED_STATUS.values()[in.readInt()];
        mLastPairedTime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIpAddress);
        dest.writeString(mCppId);
        dest.writeInt(mConnectionState.ordinal());
        dest.writeString(mName);
        dest.writeString(mDeviceType);
        dest.writeString(mModelId);
        dest.writeString(mHomeSsid);
        dest.writeLong(mBootId);
        dest.writeString(mEncryptionKey);
        dest.writeInt(mPairedState.ordinal());
        dest.writeLong(mLastPairedTime);
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

    public static NetworkNode.PAIRED_STATUS getPairedStatusKey(int status) {
        if (status >= 0 && status < NetworkNode.PAIRED_STATUS.values().length) {
            return NetworkNode.PAIRED_STATUS.values()[status];
        }
        return NetworkNode.PAIRED_STATUS.NOT_PAIRED;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("name: ").append(getName()).append("   ipAddress: ").append(getIpAddress())
                .append("   cppId: ").append(getCppId()).append("   bootId: ").append(getBootId())
                .append("   deviceType: ").append(getDeviceType()).append("   modelId: ").append(getModelId())
                .append("   paired: ").append(getPairedState())
                .append("   connectedState: ").append(getConnectionState()).append("   HomeSsid: ")
                .append(getHomeSsid());
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
        return mCppId.equals(((NetworkNode) other).mCppId);
    }

    @Override
    public int hashCode() {
        return mCppId.hashCode();
    }
}
