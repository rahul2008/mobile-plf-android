package com.philips.cl.di.dev.pa.newpurifier;

import java.util.List;

import android.content.Context;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;
import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;
import com.philips.cl.di.dev.pa.scheduler.SchedulerHandler;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.communication.ResponseHandler;
import com.philips.cl.di.dicomm.port.AirPort;
import com.philips.cl.di.dicomm.port.DICommPort;
import com.philips.cl.di.dicomm.port.DIPortListener;
import com.philips.cl.di.dicomm.port.ScheduleListPort;

/**
 * @author Jeroen Mols
 * @date 28 Apr 2014
 */
public class AirPurifier extends DICommAppliance implements ResponseHandler {

	private final String mUsn;
	private String latitude;
	private String longitude;

    protected final ScheduleListPort mScheduleListPort;
	private final SchedulerHandler mSchedulerHandler;
	private SubscriptionHandler mSubscriptionHandler;

	private final AirPort mAirPort;

	private PurifierListener mPurifierListener;

	public AirPurifier(NetworkNode networkNode, CommunicationStrategy communicationStrategy, String usn) {
	    super(networkNode, communicationStrategy);
		mUsn = usn;

		mSubscriptionHandler = new SubscriptionHandler(getNetworkNode(), this);
		mSchedulerHandler = new SchedulerHandler(this);

        mAirPort = new AirPort(mNetworkNode,communicationStrategy);
		mScheduleListPort = new ScheduleListPort(mNetworkNode, communicationStrategy, mSchedulerHandler);

        addPort(mAirPort);
        addPort(mScheduleListPort);
	}

	public AirPurifier(NetworkNode networkNode, CommunicationStrategy communicationStrategy, String usn, SubscriptionHandler subscriptionHandler) {
		this(networkNode, communicationStrategy, usn);
		mSubscriptionHandler = subscriptionHandler;
	}

	public void setPurifierListener(PurifierListener mPurifierListener) {
		this.mPurifierListener = mPurifierListener;
	}

	public AirPort getAirPort() {
		return mAirPort;
	}

    public ScheduleListPort getScheduleListPort() {
        return mScheduleListPort;
    }

	public void enableLocalSubscription() {
		mSubscriptionHandler.enableLocalSubscription();
	}

	public void disableLocalSubscription() {
		mSubscriptionHandler.disableLocalSubscription();
	}

	public void enableRemoteSubscription(Context context) {
		mSubscriptionHandler.enableRemoteSubscription(context);
	}

	public void disableRemoteSubscription(Context context) {
		mSubscriptionHandler.disableRemoteSubscription(context);
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

	public boolean isDemoPurifier() {
		return (EWSConstant.PURIFIER_ADHOCIP.equals(mNetworkNode.getIpAddress()));
	}

	public void addListenerForAllPorts(DIPortListener portListener) {
		for (DICommPort<?> port : getAllPorts()) {
			port.registerPortListener(portListener);
		}
	}

	public void removeListenerForAllPorts(DIPortListener portListener) {
		for (DICommPort<?> port : getAllPorts()) {
			port.unregisterPortListener(portListener);
		}
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("name: ").append(getName()).append("   ip: ").append(getNetworkNode().getIpAddress())
				.append("   eui64: ").append(getNetworkNode().getCppId()).append("   bootId: ").append(getNetworkNode().getBootId())
				.append("   usn: ").append(getUsn()).append("   paired: ").append(getNetworkNode().getPairedState())
				.append("   airportInfo: ").append(getAirPort().getPortProperties()).append("   firmwareInfo: ").append(getFirmwarePort().getPortProperties())
				.append("   connectedState: ").append(getNetworkNode().getConnectionState()).append("   lastKnownssid: ")
				.append("   lat: ").append(getLatitude()).append("   long: ").append(getLongitude())
				.append(getNetworkNode().getHomeSsid());
		return builder.toString();
	}

	@Override
	public void onError(Error error, String errorData) {
		if(mPurifierListener==null) return;

		switch (error) {
		case SCHEDULER:
			mPurifierListener.notifyScheduleListenerForErrorOccured(SchedulerHandler.DEFAULT_ERROR);
			break;
		default:
			break;
		}
	}

	private void notifySubscriptionListeners(String data) {
		ALog.d(ALog.APPLIANCE, "Notify subscription listeners - " + data);

		if(mAirPort.isResponseForThisPort(data)){
			mAirPort.handleSubscription(data);
			return;
		}

		// TODO: DIComm Refactor use processresponse of schedulelist port class and make parse methods as private
		if( mScheduleListPort.isResponseForThisPort(data) ){
			SchedulePortInfo schedulePortInfo = mScheduleListPort.parseResponseAsSingleSchedule(data);
			if( schedulePortInfo != null && mPurifierListener!=null) {
				mPurifierListener.notifyScheduleListenerForSingleSchedule(schedulePortInfo);
				return ;
			}
			List<SchedulePortInfo> schedulePortInfoList = mScheduleListPort.parseResponseAsScheduleList(data);
			if(  schedulePortInfoList != null && mPurifierListener!=null ) {
				mPurifierListener.notifyScheduleListenerForScheduleList(schedulePortInfoList);
				return ;
			}

			if( data.contains(AppConstants.OUT_OF_MEMORY)) {
				if(mPurifierListener!=null){
				    mPurifierListener.notifyScheduleListenerForErrorOccured(SchedulerHandler.MAX_SCHEDULES_REACHED);
				}
			}
		}

		if(mFirmwarePort.isResponseForThisPort(data)){
			mFirmwarePort.handleSubscription(data);
			return;
		}

	}

	@Override
	public void onSuccess(String data) {
		ALog.d(ALog.APPLIANCE, "Success event received");
		notifySubscriptionListeners(data);
	}
}
