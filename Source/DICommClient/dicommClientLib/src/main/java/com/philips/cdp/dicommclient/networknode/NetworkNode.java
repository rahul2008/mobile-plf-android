/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.networknode;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Observable;

public class NetworkNode extends Observable implements Parcelable {
    public enum PAIRED_STATUS {PAIRED, NOT_PAIRED, UNPAIRED, PAIRING}

    public interface EncryptionKeyUpdatedListener {
        void onKeyUpdate();
    }

    private String mIpAddress;
    private String mCppId;
    private ConnectionState mConnectionState;

    private String mName;
    private String mModelName;
    private String mModelType;
    private String mHomeSsid;
    private long mBootId;
    private String mEncryptionKey;

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

    public synchronized String getCppId() {
        return mCppId;
    }

    public synchronized void setCppId(String cppId) {
        this.mCppId = cppId;
    }

    public synchronized ConnectionState getConnectionState() {
        return mConnectionState;
    }

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
     * The modelname defines the category of the product (e.g. AirPurifier)
     * Different products can have the same modelname, but their modeltype
     * will be different.
     *
     * @return
     */
    public synchronized String getModelName() {
        return mModelName;
    }

    public synchronized void setModelName(String modelName) {
        this.mModelName = modelName;
    }

    /**
     * The modeltype defines one particular type of product (e.g. AC7342).
     * Different products will have a different modeltype, but their
     * modelname can be the same.
     *
     * @return
     */
    public synchronized String getModelType() {
        return mModelType;
    }

    public synchronized void setModelType(String modelType) {
        this.mModelType = modelType;
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
        mModelName = in.readString();
        mModelType = in.readString();
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
        dest.writeString(mModelName);
        dest.writeString(mModelType);
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
                .append("   modelName: ").append(getModelName()).append("   modelType: ").append(getModelType())
                .append("   paired: ").append(getPairedState())
                .append("   connectedState: ").append(getConnectionState()).append("   HomeSsid: ")
                .append(getHomeSsid());
        return builder.toString();
    }

    public void setEncryptionKeyUpdatedListener(EncryptionKeyUpdatedListener encryptionKeyUpdatedListener) {
        this.encryptionKeyUpdatedListener = encryptionKeyUpdatedListener;
    }
}