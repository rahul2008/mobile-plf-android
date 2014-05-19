package com.philips.cl.di.dev.pa.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.IndoorDetailsActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryEventListener;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class IndoorFragment extends BaseFragment implements AirPurifierEventListener, OnClickListener, DiscoveryEventListener {

	private LinearLayout firmwareUpdatePopup;
	private int prevIndoorAqi;
	
	private FontTextView fanModeTxt ;
	private FontTextView filterStatusTxt ;
	private FontTextView aqiStatusTxt ;
	private FontTextView aqiSummaryTxt ;
	private FontTextView purifierNameTxt ;
	private ImageView aqiPointer ;
	private ImageView aqiMeter ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.hf_indoor_dashboard, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fanModeTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_fan_mode);
		filterStatusTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_filter);
		aqiStatusTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_reading);
		aqiSummaryTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_summary);
		purifierNameTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_purifier_name);
		aqiMeter = (ImageView) getView().findViewById(R.id.hf_indoor_circle_meter);
		initFirmwareUpdatePopup();
	}
	
	private void initFirmwareUpdatePopup() {
		firmwareUpdatePopup = (LinearLayout) getView().findViewById(R.id.firmware_update_available);
		
		FontTextView firmwareUpdateText = (FontTextView) getView().findViewById(R.id.lbl_firmware_update_available);
		firmwareUpdateText.setOnClickListener(this);
		
		ImageButton firmwareUpdateCloseButton = (ImageButton) getView().findViewById(R.id.btn_firmware_update_available);
		firmwareUpdateCloseButton.setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		DiscoveryManager.getInstance().start(this);
		updateDashboard();
		hideFirmwareUpdatePopup();
	}

	private void hideFirmwareUpdatePopup() {
		firmwareUpdatePopup.setVisibility(View.GONE);
	}

	@Override
	public void onPause() {
		super.onPause();
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
		DiscoveryManager.getInstance().stop();
	}

	private void updateDashboard() {
		Activity parent = this.getActivity();
		if (parent == null || !(parent instanceof MainActivity)) return;
		
		PurAirDevice purifier = ((MainActivity) parent).getCurrentPurifier();
		if (purifier == null) {
			purifierNameTxt.setText("");
			hideIndoorMeter();
			setRotationAnimation(aqiPointer, IndoorDashboardUtils.getAqiPointerRotation(0));
			return;
		}
		
		purifierNameTxt.setText(purifier.getName());
		
		AirPortInfo airPortInfo = purifier.getAirPortInfo();
		if (airPortInfo == null) return;
		
		int indoorAqi = airPortInfo.getIndoorAQI();
		fanModeTxt.setText(getString(IndoorDashboardUtils.getFanSpeedText(airPortInfo.getFanSpeed())));
		filterStatusTxt.setText(IndoorDashboardUtils.getFilterStatus(airPortInfo));
		aqiStatusTxt.setText(getString(IndoorDashboardUtils.getAqiTitle(indoorAqi)));
		aqiSummaryTxt.setText(getString(IndoorDashboardUtils.getAqiSummary(indoorAqi)));

		ALog.i(ALog.DASHBOARD, "currentPurifier.getConnectionState() " + purifier.getConnectionState());
		if(purifier.getConnectionState() == ConnectionState.DISCONNECTED) {
			hideIndoorMeter();
		} else {
			showIndoorMeter();
		}

		aqiPointer = (ImageView) getView().findViewById(R.id.hf_indoor_circle_pointer);

		aqiPointer.setOnClickListener(this);
		aqiPointer.setImageResource(IndoorDashboardUtils.getAqiPointerBackgroundId(indoorAqi));
		aqiPointer.invalidate();
		if(prevIndoorAqi != indoorAqi) {
			setRotationAnimation(aqiPointer, IndoorDashboardUtils.getAqiPointerRotation(indoorAqi));
		}
		prevIndoorAqi = indoorAqi;
	}

	private void setRotationAnimation(ImageView aqiPointer, float rotation) {
		Drawable drawable = aqiPointer.getDrawable();
		ALog.i(ALog.DASHBOARD, "IndoorFragment$getRotationAnimation rotation " + rotation + " aqiPointer.getWidth()/2 " + (aqiPointer.getWidth()/2) + " drawable " + drawable.getMinimumHeight());
		
		Animation aqiCircleRotateAnim = new RotateAnimation(0.0f, rotation, drawable.getMinimumWidth()/2, drawable.getMinimumHeight()/2);
		
	    aqiCircleRotateAnim.setDuration(2000);  
	    aqiCircleRotateAnim.setRepeatCount(0);     
	    aqiCircleRotateAnim.setFillAfter(true);
	 
	    aqiPointer.setAnimation(aqiCircleRotateAnim);
	}
	
	private void showFirmwareUpdatePopup() {
		firmwareUpdatePopup.setVisibility(View.VISIBLE);
	}
	
	private void hideIndoorMeter() {
		aqiMeter.setVisibility(View.INVISIBLE);
	}
	
	private void showIndoorMeter() {
		aqiMeter.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onAirPurifierChanged() {
		if (getActivity() == null) return;
		PurAirDevice purifier = ((MainActivity) getActivity()).getCurrentPurifier();
		purifier.getName();
		
		// TODO implement
	}
	
	@Override
	public void onAirPurifierEventReceived() {
		if (getActivity() == null) return;
		final PurAirDevice purifier = ((MainActivity) getActivity()).getCurrentPurifier();
		
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				updateDashboard();
			}
		});
	}
	
	@Override
	public void onFirmwareEventReceived() {
		if (getActivity() == null) return;
		PurAirDevice purifier = ((MainActivity) getActivity()).getCurrentPurifier();
		if (purifier == null) return;
		final FirmwarePortInfo firmwarePortInfo = purifier.getFirmwarePortInfo();
		
		if(firmwarePortInfo.isUpdateAvailable()) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					showFirmwareUpdatePopup();
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lbl_firmware_update_available:
			((MainActivity)getActivity()).startFirmwareUpgradeActivity();
			hideFirmwareUpdatePopup();
			break;
		case R.id.btn_firmware_update_available:
			hideFirmwareUpdatePopup();
			
			break;
		case R.id.hf_indoor_circle_pointer:
			Intent intent = new Intent(getActivity(), IndoorDetailsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDiscoveredDevicesListChanged() {
		ALog.i(ALog.DASHBOARD, "IndoorDashboard$onDiscoveredDevicesListChanged");
		if(PurifierManager.getInstance().getCurrentPurifier() != null) {
			ALog.i(ALog.DASHBOARD, "Purifier connection state " + PurifierManager.getInstance().getCurrentPurifier().getConnectionState());
			switch (PurifierManager.getInstance().getCurrentPurifier().getConnectionState()) {
			case CONNECTED_LOCALLY:
			case CONNECTED_REMOTELY:
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						showIndoorMeter();
					}
				});
				break;
			case DISCONNECTED:
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						hideIndoorMeter();
					}
				});
				break;
			}
		}
	}

}
