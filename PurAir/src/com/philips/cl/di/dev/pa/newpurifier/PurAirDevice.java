package com.philips.cl.di.dev.pa.newpurifier;

import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;

/**
 * @author Jeroen Mols
 * @date 28 Apr 2014
 */
public class PurAirDevice {

	private final String mEui64;
	private final String mUsn;
	
	private String mIpAddress;
	private String mName;
	private long mBootId;
	private String mLastKnownNetworkSsid;
	
	private ConnectionState mConnectionState;
	private boolean 		isPaired = false;
	private String 			mEncryptionKey;

	private AirPortInfo 		mAirPortInfo;
	private FirmwarePortInfo	mFirmwarePortInfo;

	public PurAirDevice(String eui64, String usn, String ipAddress, String name, 
			long bootId, ConnectionState connectionState) {
		mBootId = bootId;
		mEui64 = eui64;
		mUsn = usn;
		mIpAddress = ipAddress;
		mName = name;
		mConnectionState = connectionState;
	}
	
	public String getEui64() {
		return mEui64;
	}

	public String getUsn() {
		return mUsn;
	}
	
	public synchronized String getIpAddress() {
		return mIpAddress;
	}

	public synchronized void setIpAddress(String ipAddress) {
		this.mIpAddress = ipAddress;
	}

	public synchronized String getName() {
		return mName;
	}

	public synchronized void setName(String name) {
		this.mName = name;
	}

	public synchronized long getBootId() {
		return mBootId;
	}

	public synchronized void setBootId(long bootId) {
		this.mBootId = bootId;
	}
	
	public synchronized String getLastKnownNetworkSsid() {
		return mLastKnownNetworkSsid;
	}

	public synchronized void setLastKnownNetworkSsid(String lastKnownNetworkSsid) {
		if (lastKnownNetworkSsid == null || lastKnownNetworkSsid.isEmpty()) return;
		this.mLastKnownNetworkSsid = lastKnownNetworkSsid;
	}

	public synchronized ConnectionState getConnectionState() {
		return mConnectionState;
	}

	public synchronized void setConnectionState(ConnectionState connectionState) {
		this.mConnectionState = connectionState;
	}

	public synchronized boolean isPaired() {
		return isPaired;
	}

	public synchronized void setPairing(boolean paired) {
		this.isPaired = paired;
	}

	public synchronized String getEncryptionKey() {
		return mEncryptionKey;
	}

	public synchronized void setEncryptionKey(String encryptionKey) {
		this.mEncryptionKey = encryptionKey;
	}
	
	public AirPortInfo getAirPortInfo() {
		return mAirPortInfo;
	}

	public void setAirPortInfo(AirPortInfo airPortInfo) {
		this.mAirPortInfo = airPortInfo;
	}

	public FirmwarePortInfo getFirmwarePortInfo() {
		return mFirmwarePortInfo;
	}

	public void setFirmwarePortInfo(FirmwarePortInfo firmwarePortInfo) {
		this.mFirmwarePortInfo = firmwarePortInfo;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("name: ").append(getName()).append("   ip: ").append(getIpAddress())
				.append("   eui64: ").append(getEui64()).append("   bootId: ").append(getBootId())
				.append("   usn: ").append(getUsn()).append("   paired: ").append(isPaired())
				.append("   connectedState: ").append(getConnectionState()).append("   lastKnownssid: ")
				.append(getLastKnownNetworkSsid());
		return builder.toString();
	}
}
