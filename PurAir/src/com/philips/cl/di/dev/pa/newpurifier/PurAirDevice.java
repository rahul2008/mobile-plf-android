package com.philips.cl.di.dev.pa.newpurifier;

import java.util.List;
import java.util.Observable;

import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.datamodel.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;
import com.philips.cl.di.dev.pa.util.MetricsTracker;

/**
 * @author Jeroen Mols
 * @date 28 Apr 2014
 */
public class PurAirDevice extends Observable {

	private final String mUsn;
	private String latitude;
	private String longitude;
	
	public long getLastPairedTime() {
		return mNetworkNode.getLastPairedTime();
	}

	public void setLastPairedTime(long mLastPairedTime) {
		this.mNetworkNode.setLastPairedTime(mLastPairedTime);
	}

	public static enum PAIRED_STATUS {PAIRED, NOT_PAIRED, UNPAIRED, PAIRING};
	private NetworkNode mNetworkNode = new NetworkNode();
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
		mNetworkNode = new NetworkNode();
		mNetworkNode.setBootId(bootId);
		mNetworkNode.setCppId(eui64);
		mUsn = usn;
		mNetworkNode.setIpAddress(ipAddress);
		mNetworkNode.setName(name);
		mNetworkNode.setConnectionState(connectionState);
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getEui64() {
		return mNetworkNode.getCppId();
	}

	public String getUsn() {
		return mUsn;
	}
	
	public synchronized String getIpAddress() {
		return mNetworkNode.getIpAddress();
	}

	public synchronized void setIpAddress(String ipAddress) {
		this.mNetworkNode.setIpAddress(ipAddress);
	}

	public synchronized String getName() {
		return mNetworkNode.getName();
	}

	public synchronized void setName(String name) {
		this.mNetworkNode.setName(name);
	}

	public synchronized long getBootId() {
		return mNetworkNode.getBootId();
	}

	public synchronized void setBootId(long bootId) {
		this.mNetworkNode.setBootId(bootId);
	}
	
	public synchronized String getLastKnownNetworkSsid() {
		return mNetworkNode.getHomeSsid();
	}

	public synchronized void setLastKnownNetworkSsid(String lastKnownNetworkSsid) {
		if (lastKnownNetworkSsid == null || lastKnownNetworkSsid.isEmpty()) return;
		this.mNetworkNode.setHomeSsid(lastKnownNetworkSsid);
	}

	public synchronized ConnectionState getConnectionState() {
		return mNetworkNode.getConnectionState();
	}

	public void setConnectionState(ConnectionState connectionState) {
		synchronized(this) { // notifyObservers called from same Thread
			if (connectionState.equals(mNetworkNode.getConnectionState())) return;
			MetricsTracker.trackActionConnectionType(connectionState);
			this.mNetworkNode.setConnectionState(connectionState);
		}
		setChanged();
		notifyObservers();
	}

	public synchronized PAIRED_STATUS getPairedStatus() {
		return mNetworkNode.getPairedState();
	}

	public synchronized void setPairing(PAIRED_STATUS status) {
		this.mNetworkNode.setPairedState(status);
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
		return mNetworkNode.isOnlineViaCpp();
	}
	
	public synchronized void setOnlineViaCpp(boolean onlineViaCpp) {
		this.mNetworkNode.setOnlineViaCpp(onlineViaCpp);
	}

	public synchronized String getEncryptionKey() {
		return mNetworkNode.getEncryptionKey();
	}

	public synchronized void setEncryptionKey(String encryptionKey) {
		this.mNetworkNode.setEncryptionKey(encryptionKey);
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
		return (EWSConstant.PURIFIER_ADHOCIP.equals(mNetworkNode.getIpAddress()));
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("name: ").append(getName()).append("   ip: ").append(getIpAddress())
				.append("   eui64: ").append(getEui64()).append("   bootId: ").append(getBootId())
				.append("   usn: ").append(getUsn()).append("   paired: ").append(getPairedStatus())
				.append("   airportInfo: ").append(getAirPortInfo()).append("   firmwareInfo: ").append(getFirmwarePortInfo())
				.append("   connectedState: ").append(getConnectionState()).append("   lastKnownssid: ")
				.append("   lat: ").append(getLatitude()).append("   long: ").append(getLongitude())
				.append(getLastKnownNetworkSsid());
		return builder.toString();
	}
}
