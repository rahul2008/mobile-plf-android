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
	
	private final NetworkNode mNetworkNode = new NetworkNode();
	
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
		mNetworkNode.setBootId(bootId);
		mNetworkNode.setCppId(eui64);
		mUsn = usn;
		mNetworkNode.setIpAddress(ipAddress);
		mNetworkNode.setName(name);
		mNetworkNode.setConnectionState(connectionState);
	}

	public NetworkNode getNetworkNode() {
		return mNetworkNode;
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

	public String getUsn() {
		return mUsn;
	}
	
	public synchronized String getName() {
		return getNetworkNode().getName();
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
		builder.append("name: ").append(getName()).append("   ip: ").append(getNetworkNode().getIpAddress())
				.append("   eui64: ").append(getNetworkNode().getCppId()).append("   bootId: ").append(getNetworkNode().getBootId())
				.append("   usn: ").append(getUsn()).append("   paired: ").append(getNetworkNode().getPairedState())
				.append("   airportInfo: ").append(getAirPortInfo()).append("   firmwareInfo: ").append(getFirmwarePortInfo())
				.append("   connectedState: ").append(getNetworkNode().getConnectionState()).append("   lastKnownssid: ")
				.append("   lat: ").append(getLatitude()).append("   long: ").append(getLongitude())
				.append(getNetworkNode().getHomeSsid());
		return builder.toString();
	}
}
