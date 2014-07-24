package com.philips.cl.di.dev.pa.newpurifier;

import java.util.List;
import java.util.Observable;

import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;

/**
 * @author Jeroen Mols
 * @date 28 Apr 2014
 */
public class PurAirDevice extends Observable {

	private final String mEui64;
	private final String mUsn;
	
	private String mIpAddress;
	private String mName;
	private long mBootId;
	private String mLastKnownNetworkSsid;
	
	private ConnectionState mConnectionState;
	public long getLastPairedTime() {
		return mLastPairedTime;
	}

	public void setLastPairedTime(long mLastPairedTime) {
		this.mLastPairedTime = mLastPairedTime;
	}

	public static enum PAIRED_STATUS {PAIRED, NOT_PAIRED, UNPAIRED, PAIRING};
	private PAIRED_STATUS pairedState = PAIRED_STATUS.NOT_PAIRED ;
	private long 			mLastPairedTime;
	private boolean			isOnlineViaCpp 	= false;
	private String 			mEncryptionKey;

	private AirPortInfo 		   mAirPortInfo;
	private FirmwarePortInfo	   mFirmwarePortInfo;
	private List<SchedulePortInfo> mSchedulerPortInfoList;
	
	public List<SchedulePortInfo> getmSchedulerPortInfoList() {
		return mSchedulerPortInfoList;
	}

	public void setmSchedulerPortInfoList(
			List<SchedulePortInfo> mSchedulerPortInfoList) {
		this.mSchedulerPortInfoList = mSchedulerPortInfoList;
	}

	

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

	public void setConnectionState(ConnectionState connectionState) {
		synchronized(this) { // notifyObservers called from same Thread
			if (connectionState.equals(mConnectionState)) return;
			this.mConnectionState = connectionState;
		}
		setChanged();
		notifyObservers();
	}

	public synchronized PAIRED_STATUS getPairedStatus() {
		return pairedState;
	}

	public synchronized void setPairing(PAIRED_STATUS status) {
		this.pairedState = status;
	}
	
	public static PAIRED_STATUS getPairedStatusKey(int status){
		PAIRED_STATUS state = null;
		switch(status){
		case 0:
			state= PAIRED_STATUS.PAIRED;
			break;
		case 1:
			state= PAIRED_STATUS.NOT_PAIRED;
			break;
		case 2:
			state= PAIRED_STATUS.UNPAIRED;
			break;
		case 3:
			state= PAIRED_STATUS.PAIRING;
			break;
		default:
			state= PAIRED_STATUS.NOT_PAIRED;
			break;
		}
		return state;
	}
			
	public synchronized boolean isOnlineViaCpp() {
		return isOnlineViaCpp;
	}
	
	public synchronized void setOnlineViaCpp(boolean onlineViaCpp) {
		this.isOnlineViaCpp = onlineViaCpp;
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
	
	public boolean isDemoPurifier() {
		return (EWSConstant.PURIFIER_ADHOCIP.equals(mIpAddress));
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("name: ").append(getName()).append("   ip: ").append(getIpAddress())
				.append("   eui64: ").append(getEui64()).append("   bootId: ").append(getBootId())
				.append("   usn: ").append(getUsn()).append("   paired: ").append(getPairedStatus())
				.append("   airportInfo: ").append(getAirPortInfo()).append("   firmwareInfo: ").append(getFirmwarePortInfo())
				.append("   connectedState: ").append(getConnectionState()).append("   lastKnownssid: ")
				.append(getLastKnownNetworkSsid());
		return builder.toString();
	}
}
