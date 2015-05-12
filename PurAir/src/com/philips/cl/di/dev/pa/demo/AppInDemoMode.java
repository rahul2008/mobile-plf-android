package com.philips.cl.di.dev.pa.demo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkStateListener;
import com.philips.cl.di.dicomm.communication.CommunicationMarshal;
import com.philips.cl.di.dicomm.security.DISecurity;

public class AppInDemoMode implements NetworkStateListener {
	private Context ctx;

	public AppInDemoMode(Context ctx) {
		this.ctx = ctx;
	}

	public void addNetworkListenerForDemoMode() {
		NetworkReceiver.getInstance().addNetworkStateListener(this);

	}

	public void rmoveNetworkListenerForDemoMode() {
		NetworkReceiver.getInstance().removeNetworkStateListener(this);
	}

	public void connectPurifier() {
		String ssid = EWSWifiManager.getSsidOfSupplicantNetwork();
		if (ssid != null && ssid.contains(EWSWifiManager.DEVICE_SSID)) {
		    setDemoModePurifier();
		} else {
			showAlertDialogAppInDemoMode(
					ctx.getString(R.string.app_in_demo_mode_title),
					ctx.getString(R.string.app_in_demo_mode_msg));
		}
	}

	public void checkPhilipsSetupWifiSelected() {
		String ssid = EWSWifiManager.getSsidOfSupplicantNetwork();
		if (ssid == null || !ssid.contains(EWSWifiManager.DEVICE_SSID)) {
			showAlertDialogAppInDemoMode(
					ctx.getString(R.string.app_in_demo_mode_title),
					ctx.getString(R.string.app_in_demo_mode_msg));
		}
	}

	private void showAlertDialogAppInDemoMode(String title, String message) {
		try {
			FragmentTransaction fragTransaction = ((MainActivity) ctx)
					.getSupportFragmentManager().beginTransaction();

			Fragment prevFrag = ((MainActivity) ctx)
					.getSupportFragmentManager().findFragmentByTag(
							"app_in_demo_mode");
			if (prevFrag != null) {
				return;
			}

			fragTransaction.add(
					DownloadAlerDialogFragement.newInstance(title, message),
					"app_in_demo_mode").commitAllowingStateLoss();

		} catch (IllegalStateException e) {
			ALog.e(ALog.INDOOR_DETAILS, "Error: " + e.getMessage());
		}
	}

	@Override
	public void onConnected(String ssid) {
		AirPurifier current = AirPurifierManager.getInstance().getCurrentPurifier();
		if (PurAirApplication.isDemoModeEnable()) {
			if (current != null
					&& current.getNetworkNode().getConnectionState() == ConnectionState.CONNECTED_LOCALLY)
				return;
			if (ssid != null && ssid.contains(EWSWifiManager.DEVICE_SSID)) {
			    setDemoModePurifier();
			    return;
			}
		}
	}

	@Override
	public void onDisconnected() {
		// NOP
	}

	private void setDemoModePurifier() {
	    ALog.i(ALog.MAINACTIVITY, "Setting Demo mode purifier");
        
        NetworkNode networkNode = new NetworkNode();
        networkNode.setBootId(-1);
        networkNode.setCppId(PurAirApplication.getDemoModePurifierEUI64());
        networkNode.setName(DemoModeConstant.DEMO);
        networkNode.setIpAddress(EWSConstant.PURIFIER_ADHOCIP);
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
        
        AirPurifier demoModePurifier = new AirPurifier(networkNode, new CommunicationMarshal(new DISecurity()));
        AirPurifierManager.getInstance().setCurrentAppliance(demoModePurifier);
	}
}
