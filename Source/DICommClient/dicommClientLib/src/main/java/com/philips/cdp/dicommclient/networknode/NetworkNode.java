/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.networknode;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Observable;

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
    private String mModelName;
    private String mModelId;
    private String mHomeSsid;
    private long mBootId;
    private String mEncryptionKey;
    private boolean mHttps;
    private String bleAddress;

    private PAIRED_STATUS mPairedState = PAIRED_STATUS.NOT_PAIRED;
    private long mLastPairedTime;

    private final int mDICommProtocolVersion = 1;

    private EncryptionKeyUpdatedListener encryptionKeyUpdatedListener;

    public NetworkNode() {
    }

    public String getIpAddress() {
        return mIpAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.mIpAddress = ipAddress;
    }

    @NonNull
    public String getCppId() {
        return mCppId;
    }

    public void setCppId(@NonNull String cppId) {
        this.mCppId = cppId;
    }

    @Deprecated
    public ConnectionState getConnectionState() {
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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    /**
     * The model name defines the category of the product (e.g. AirPurifier)
     * Different products can have the same model name, but their model id
     * will be different.
     *
     * @return model name
     */
    public String getModelName() {
        return mModelName;
    }

    public void setModelName(String modelName) {
        this.mModelName = modelName;
    }

    /**
     * The model id defines one particular type of product (e.g. AC7342).
     * Different products will have a different model id, but their
     * model name can be the same.
     *
     * @return model id
     */
    public String getModelId() {
        return mModelId;
    }

    public void setModelId(String modelId) {
        this.mModelId = modelId;
    }

    public String getHomeSsid() {
        return mHomeSsid;
    }

    public void setHomeSsid(String homeSsid) {
        if (homeSsid == null || homeSsid.isEmpty()) return;
        this.mHomeSsid = homeSsid;
    }

    public long getBootId() {
        return mBootId;
    }

    public void setBootId(long bootId) {
        synchronized (this) { // notifyObservers called from same Thread
            if (mBootId == bootId) return;
            this.mBootId = bootId;
        }
        setChanged();
        notifyObservers();
    }

    public String getEncryptionKey() {
        return mEncryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        boolean isKeyUpdated = mEncryptionKey != encryptionKey;
        this.mEncryptionKey = encryptionKey;
        if (isKeyUpdated && encryptionKeyUpdatedListener != null) {
            encryptionKeyUpdatedListener.onKeyUpdate();
        }
    }

    public boolean getHttps() {
        return mHttps;
    }

    public void setHttps(boolean mHttps) {
        this.mHttps = mHttps;
    }

    public String getBleAddress() {
        return bleAddress;
    }

    public void setBleAddress(String bleAddress) {
        this.bleAddress = bleAddress;
    }

    public NetworkNode.PAIRED_STATUS getPairedState() {
        return mPairedState;
    }

    public void setPairedState(NetworkNode.PAIRED_STATUS pairedState) {
        this.mPairedState = pairedState;
    }

    public long getLastPairedTime() {
        return mLastPairedTime;
    }

    public void setLastPairedTime(long lastPairedTime) {
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
        mModelId = in.readString();
        mHomeSsid = in.readString();
        mBootId = in.readLong();
        mEncryptionKey = in.readString();
        mPairedState = PAIRED_STATUS.values()[in.readInt()];
        mLastPairedTime = in.readLong();
        bleAddress = in.readString();
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
        dest.writeString(mModelId);
        dest.writeString(mHomeSsid);
        dest.writeLong(mBootId);
        dest.writeString(mEncryptionKey);
        dest.writeInt(mPairedState.ordinal());
        dest.writeLong(mLastPairedTime);
        dest.writeString(bleAddress);
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
        builder.append("name: ").append(mName)
                .append("   ipAddress: ").append(mIpAddress)
                .append("   cppId: ").append(mCppId)
                .append("   bootId: ").append(mBootId)
                .append("   modelName: ").append(mModelName)
                .append("   modelId: ").append(mModelId)
                .append("   paired: ").append(mPairedState)
                .append("   connectedState: ").append(mConnectionState)
                .append("   HomeSsid: ").append(mHomeSsid)
                .append("   bleAddress:").append(bleAddress);
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
