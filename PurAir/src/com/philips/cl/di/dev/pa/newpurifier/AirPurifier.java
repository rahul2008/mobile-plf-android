package com.philips.cl.di.dev.pa.newpurifier;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;
import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;
import com.philips.cl.di.dev.pa.scheduler.SchedulerHandler;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.security.KeyDecryptListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.CommunicationMarshal;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.communication.ResponseHandler;
import com.philips.cl.di.dicomm.port.AirPort;
import com.philips.cl.di.dicomm.port.FirmwarePort;
import com.philips.cl.di.dicomm.port.ScheduleListPort;

/**
 * @author Jeroen Mols
 * @date 28 Apr 2014
 */
public class AirPurifier implements ResponseHandler, KeyDecryptListener{

	private final String mUsn;
	private String latitude;
	private String longitude;

	private final NetworkNode mNetworkNode = new NetworkNode();
	private final SchedulerHandler mSchedulerHandler;
	private SubscriptionHandler mSubscriptionHandler;

	private final AirPort mAirPort;
	private final FirmwarePort mFirmwarePort;
	private final ScheduleListPort mScheduleListPort;

	private final DISecurity mDISecurity;

	private PurifierListener mPurifierListener;

	private final Handler mResubscriptionHandler = new Handler(Looper.getMainLooper());
	private Runnable mResubscribeRunnable;
	protected static final long RESUBSCRIBING_TIME = 300000;

	private CommunicationStrategy mCommunicationStrategy;

	public AirPurifier(CommunicationStrategy communicationStrategy, String eui64, String usn, String ipAddress, String name,
			long bootId, ConnectionState connectionState) {
		mNetworkNode.setBootId(bootId);
		mNetworkNode.setCppId(eui64);
		mUsn = usn;
		mNetworkNode.setIpAddress(ipAddress);
		mNetworkNode.setName(name);
		mNetworkNode.setConnectionState(connectionState);
		mCommunicationStrategy = communicationStrategy;
		mSubscriptionHandler = new SubscriptionHandler(getNetworkNode(), this);
		mSchedulerHandler = new SchedulerHandler(this);
		mAirPort = new AirPort(mNetworkNode,mCommunicationStrategy);
		mFirmwarePort = new FirmwarePort(mNetworkNode,mCommunicationStrategy);
		mScheduleListPort = new ScheduleListPort(mNetworkNode, mCommunicationStrategy,mSchedulerHandler);
		mDISecurity = new DISecurity(this);
	}

	public AirPurifier(CommunicationStrategy communicationStrategy, String eui64, String usn, String ipAddress, String name,
			long bootId, ConnectionState connectionState, SubscriptionHandler subscriptionHandler) {
		this(communicationStrategy, eui64, usn, ipAddress, name, bootId, connectionState);
		mSubscriptionHandler = subscriptionHandler;
	}

	public NetworkNode getNetworkNode() {
		return mNetworkNode;
	}

	public void setPurifierListener(PurifierListener mPurifierListener) {
		this.mPurifierListener = mPurifierListener;
	}

	public AirPort getAirPort() {
		return mAirPort;
	}

	public FirmwarePort getFirmwarePort() {
		return mFirmwarePort;
	}

	public ScheduleListPort getScheduleListPort() {
		return mScheduleListPort;
	}

	public DISecurity getDISecurity() {
		return mDISecurity;
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

	public synchronized String getName() {
		return getNetworkNode().getName();
	}

	public void setConnectionState(ConnectionState connectionState) {
		mNetworkNode.setConnectionState(connectionState);
	}

	public boolean isDemoPurifier() {
		return (EWSConstant.PURIFIER_ADHOCIP.equals(mNetworkNode.getIpAddress()));
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("name: ").append(getName()).append("   ip: ").append(getNetworkNode().getIpAddress())
				.append("   eui64: ").append(getNetworkNode().getCppId()).append("   bootId: ").append(getNetworkNode().getBootId())
				.append("   usn: ").append(getUsn()).append("   paired: ").append(getNetworkNode().getPairedState())
				.append("   airportInfo: ").append(getAirPort().getAirPortInfo()).append("   firmwareInfo: ").append(getFirmwarePort().getFirmwarePortInfo())
				.append("   connectedState: ").append(getNetworkNode().getConnectionState()).append("   lastKnownssid: ")
				.append("   lat: ").append(getLatitude()).append("   long: ").append(getLongitude())
				.append(getNetworkNode().getHomeSsid());
		return builder.toString();
	}

	@Override
	public void onError(Error error) {
		if(mPurifierListener==null) return;

		switch (error) {
		case SCHEDULER:
			mPurifierListener.notifyScheduleListenerForErrorOccured(SchedulerHandler.DEFAULT_ERROR);
			break;
		default:
			break;
		}
	}

	public void subscribeToAllEvents() {
		ALog.i(ALog.APPLIANCE, "Subscribe to all events for appliance: " + this) ;
		mSubscriptionHandler.subscribeToPurifierEvents();
		//mAirPort.subscribe();
		mSubscriptionHandler.subscribeToFirmwareEvents();
		mResubscriptionHandler.removeCallbacks(mResubscribeRunnable);
		mResubscriptionHandler.post(mResubscribeRunnable);
		mResubscribeRunnable = new Runnable() {
			@Override
			public void run() {
				try{
					mResubscriptionHandler.removeCallbacks(mResubscribeRunnable);
					mSubscriptionHandler.subscribeToPurifierEvents();
					//mAirPort.subscribe();
					mSubscriptionHandler.subscribeToFirmwareEvents();
					mResubscriptionHandler.postDelayed(mResubscribeRunnable, AirPurifier.RESUBSCRIBING_TIME);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	public void unSubscribeFromAllEvents() {
		ALog.i(ALog.APPLIANCE, "UnSubscribe from all events from appliance: " + this) ;
		mResubscriptionHandler.removeCallbacks(mResubscribeRunnable);
		mSubscriptionHandler.unSubscribeFromPurifierEvents();
		mSubscriptionHandler.unSubscribeFromFirmwareEvents();
	}

	private void notifySubscriptionListeners(String data) {
		ALog.d(ALog.APPLIANCE, "Notify subscription listeners - " + data);

		if(mAirPort.isResponseForThisPort(data)){
			mAirPort.handleSubscription(data);
			if(mPurifierListener!=null){
			    mPurifierListener.notifyAirPurifierEventListeners();
			}
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
			if(mPurifierListener!=null){
			    mPurifierListener.notifyFirmwareEventListeners();
			}
			return;
		}

	}

	@Override
	public void keyDecrypt(String key, String deviceEui64) {
		if (key == null) return;

		if (deviceEui64.equals(mNetworkNode.getCppId())) {
			ALog.e(ALog.APPLIANCE, "Updated current appliance encryption key");
			mNetworkNode.setEncryptionKey(key);
			// TODO: DIComm Refactor, modify purifierDatabase to remove purairdevice
			new PurifierDatabase().updatePurifierUsingUsn(this);
		}
	}

	@Override
	public void onSuccess(String data) {
		ALog.d(ALog.APPLIANCE, "Success event received");
		notifySubscriptionListeners(data) ;
	}
}
