package com.philips.cl.di.dev.pa.newpurifier;

import android.location.Location;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.common.ScheduleListPort;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dicomm.port.AirPort;

/**
 * @author Jeroen Mols
 * @date 28 Apr 2014
 */
public class AirPurifier extends DICommAppliance {

	public static final String PRODUCT_ID = "883437300710";

	private String latitude;
	private String longitude;
	
	private int notificationsPermissionEnabled ;
	public enum NotificationPermission {
		UNKNOWN(0),
		PERMISSION_ENABLED(1),
		PERMISSION_DISABLED(2);
		public final int notificationPermission;
		
		NotificationPermission(int notificationPermission) {
			this.notificationPermission = notificationPermission;
		}		
		public int getNotificationPermission() {
			return notificationPermission;
		}
		
	}
	
	
    public int getNotificationsPermissionEnabled() {
		return notificationsPermissionEnabled;
	}

	public void setNotificationsPermissionEnabled(int notificationsPermissionEnabled) {
		this.notificationsPermissionEnabled = notificationsPermissionEnabled;
	}

	protected final ScheduleListPort mScheduleListPort;
	private final AirPort mAirPort;

	public AirPurifier(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
	    super(networkNode, communicationStrategy);

        mAirPort = new AirPort(mNetworkNode,communicationStrategy);
		mScheduleListPort = new ScheduleListPort(mNetworkNode, communicationStrategy);

        addPort(mAirPort);
        addPort(mScheduleListPort);

        /* Very first time AirPurifier is created, its connectionState will be local.
         * This call will ensure its location is set.*/
        loadLocationData();
	}
  
	@Override
	public String getDeviceType() {
		// AirPurifier uses productId instead of DeviceType for legacy reasons
		return PRODUCT_ID;
	}

	public AirPort getAirPort() {
		return mAirPort;
	}

    public ScheduleListPort getScheduleListPort() {
        return mScheduleListPort;
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

	public boolean isDemoPurifier() {
		return (EWSConstant.PURIFIER_ADHOCIP.equals(mNetworkNode.getIpAddress()));
	}

	private void loadLocationData() {
		Location location = OutdoorController.getInstance().getCurrentLocation();
		if (location != null && getNetworkNode().getConnectionState().equals(ConnectionState.CONNECTED_LOCALLY)) {
			setLatitude(String.valueOf(location.getLatitude()));
			setLongitude(String.valueOf(location.getLongitude()));
			((DiscoveryManager<AirPurifier>)DiscoveryManager.getInstance()).updateApplianceInDatabase(this);
		}
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("name: ").append(getName()).append("   ip: ").append(getNetworkNode().getIpAddress())
				.append("   eui64: ").append(getNetworkNode().getCppId()).append("   bootId: ").append(getNetworkNode().getBootId())
				.append("   paired: ").append(getNetworkNode().getPairedState())
				.append("   connectedState: ").append(getNetworkNode().getConnectionState()).append("   lastKnownssid: ").append(getNetworkNode().getHomeSsid())
				.append("   lat: ").append(getLatitude()).append("   long: ").append(getLongitude());
		return builder.toString();
	}

}
