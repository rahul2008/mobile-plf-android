package com.philips.cl.di.dev.pa.newpurifier;

import android.os.Parcel;
import android.os.Parcelable;


public class NetworkNode implements Parcelable {
	public static enum PAIRED_STATUS {PAIRED, NOT_PAIRED, UNPAIRED, PAIRING}

	private String mIpAddress;
	private String mCppId;
	private ConnectionState mConnectionState;
	
	private String mName;
	private String mModelName;
	private String mHomeSsid;
	private long mBootId;
	private String mEncryptionKey;
	
	private boolean mIsOnlineViaCpp = false;
	private PAIRED_STATUS mPairedState = PAIRED_STATUS.NOT_PAIRED;
	private long mLastPairedTime;

	public NetworkNode() {
	}
	
	public String getIpAddress() {
		return mIpAddress;
	}
	
	public void setIpAddress(String ipAddress) {
		this.mIpAddress = ipAddress;
	}

	public String getCppId() {
		return mCppId;
	}

	public void setCppId(String cppId) {
		this.mCppId = cppId;
	}

	public ConnectionState getConnectionState() {
		return mConnectionState;
	}
	
	public void setConnectionState(ConnectionState connectionState) {
		this.mConnectionState = connectionState;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}
	
	//TODO: to implement
	public String getModelName() {
		throw new UnsupportedOperationException();
		//return mModelName;
	}

	public void setModelName(String modelName) {
		this.mModelName = modelName;
	}

	public String getHomeSsid() {
		return mHomeSsid;
	}

	public void setHomeSsid(String homeSsid) {
		this.mHomeSsid = homeSsid;
	}

	public long getBootId() {
		return mBootId;
	}

	public void setBootId(long bootId) {
		this.mBootId = bootId;
	}
	
	public String getEncryptionKey() {
		return mEncryptionKey;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.mEncryptionKey = encryptionKey;
	}
	
	public boolean isOnlineViaCpp() {
		return mIsOnlineViaCpp;
	}

	public void setOnlineViaCpp(boolean isOnlineViaCpp) {
		this.mIsOnlineViaCpp = isOnlineViaCpp;
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

	

    protected NetworkNode(Parcel in) {
        mIpAddress = in.readString();
        mCppId = in.readString();
        mConnectionState = ConnectionState.values()[in.readInt()];
        mName = in.readString();
        mModelName = in.readString();
        mHomeSsid = in.readString();
        mBootId = in.readLong();
        mEncryptionKey = in.readString();
        mIsOnlineViaCpp = in.readByte() != 0x00;
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
        dest.writeString(mHomeSsid);
        dest.writeLong(mBootId);
        dest.writeString(mEncryptionKey);
        dest.writeByte((byte) (mIsOnlineViaCpp ? 0x01 : 0x00));
        dest.writeInt(mPairedState.ordinal());
        dest.writeLong(mLastPairedTime);
    }

    @SuppressWarnings("unused")
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
}