package com.philips.cl.di.dev.pa.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.IndoorDetailsActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEvent;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEventListener;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.fragment.SupportFragment;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PURIFIER_EVENT;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class IndoorFragment extends BaseFragment implements AirPurifierEventListener, OnClickListener,
	DrawerEventListener, AlertDialogBtnInterface, OnPageChangeListener {

	private RelativeLayout firmwareUpdatePopup;
	private int prevIndoorAqi;
	private float prevRotation = 0.0f;
	
	private FontTextView fanModeTxt ;
	private FontTextView filterStatusTxt ;
	private FontTextView aqiStatusTxt ;
	private FontTextView aqiSummaryTxt ;
	private FontTextView purifierNameTxt ;
	private FontTextView purifierEui64Txt;
	private ImageView aqiPointer ;
	private ImageView aqiMeter ;
	
	private FontTextView firmwareUpdateText;
	private FontTextView firmwareInfoButton;
	private ProgressBar firmwareProgress;
	private AlertDialogFragment dialogFragment;
	private AlertDialogFragment firmwareInfoDialog;
	private int position ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.hf_indoor_dashboard, null);
		return view;
	}

	private static MainActivity mActivity;
	
	public static void resetActivity() {
		mActivity = null;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String eui64 = "";

		if(mActivity == null) {
			ALog.i(ALog.TEMP, "Activity is null -- creating new");
			mActivity = (MainActivity) getActivity();
		}
		
		if(getArguments() != null) {
			position = getArguments().getInt("position");
			PurAirDevice purifier = DiscoveryManager.getInstance().getStoreDevices().get(position);
			if (purifier == null) return;
			
			eui64 = purifier.getEui64() ;
		}
		
		ALog.i(ALog.DASHBOARD, "IndoorFragmet$onActivityCreated position: " + position);
		fanModeTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_fan_mode);
		filterStatusTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_filter);
		aqiStatusTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_reading);
		aqiSummaryTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_summary);
		purifierNameTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_purifier_name);
		purifierNameTxt.setSelected(true);
		purifierNameTxt.setText(PurifierManager.getInstance().getCurrentPurifier().getName());
		purifierEui64Txt = (FontTextView) getView().findViewById(R.id.hf_indoor_purifier_eui64);

		purifierEui64Txt.setText(eui64);


		aqiPointer = (ImageView) getView().findViewById(R.id.hf_indoor_circle_pointer);
		aqiPointer.setOnClickListener(this);
		aqiMeter = (ImageView) getView().findViewById(R.id.hf_indoor_circle_meter);


		initFirmwareUpdatePopup();
	}
	
	private void initFirmwareUpdatePopup() {
		firmwareUpdatePopup = (RelativeLayout) getView().findViewById(R.id.firmware_update_available);
		firmwareUpdatePopup.setOnClickListener(this);
		
		firmwareInfoButton = (FontTextView) getView().findViewById(R.id.lbl_firmware_info);
		firmwareInfoButton.setOnClickListener(this);

		firmwareUpdateText = (FontTextView) getView().findViewById(R.id.lbl_firmware_update_available);
		firmwareUpdateText.setOnClickListener(this);
		
		firmwareProgress = (ProgressBar) getView().findViewById(R.id.firmware_update_progress);
		firmwareProgress.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onResume() {
		super.onResume();

		PurifierManager.getInstance().addAirPurifierEventListener(this);
		
		DrawerAdapter.getInstance().addDrawerListener(this);
		
		updateDashboardUI();
		hideFirmwareUpdatePopup();
	}

	private void hideFirmwareUpdatePopup() {
		firmwareUpdatePopup.setVisibility(View.GONE);
		showFirmwareUI = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
		DrawerAdapter.getInstance().removeDrawerListener(this);
	}

	private void updateDashboardUI() {
		Activity parent = this.getActivity();
		if (parent == null || !(parent instanceof MainActivity)) return;
		
		PurAirDevice purifier = ((MainActivity) parent).getCurrentPurifier();
//		if (purifier == null) {
//			purifierNameTxt.setText("");
//			hideIndoorMeter();
//			setRotationAnimation(aqiPointer, IndoorDashboardUtils.getAqiPointerRotation(0));
//			prevRotation = 0.0f;
//			return;
//		}
		String tempEui64 = purifierEui64Txt.getText().toString();

		if (purifier == null || !purifier.getEui64().equals(tempEui64)) {
			purifier = DiscoveryManager.getInstance().getDeviceByEui64(tempEui64);
			if (purifier == null) return;
		}

		purifierNameTxt.setText(purifier.getName());
		
		AirPortInfo airPortInfo = purifier.getAirPortInfo();
		if (airPortInfo == null) return;
		
		int indoorAqi = airPortInfo.getIndoorAQI();
		if (ConnectionState.DISCONNECTED == purifier.getConnectionState()) {
			hideIndoorMeter();
			fanModeTxt.setText(getString(R.string.off));
			filterStatusTxt.setText(AppConstants.EMPTY_STRING);
			aqiStatusTxt.setTextSize(18.0f);
			aqiPointer.setImageResource(R.drawable.grey_circle_2x);
			aqiStatusTxt.setText(getString(R.string.no_connection));
			aqiSummaryTxt.setText(AppConstants.EMPTY_STRING);
			prevRotation = 0.0f;

		} else {
			if(!airPortInfo.getPowerMode().equals(AppConstants.POWER_ON)) {
				fanModeTxt.setText(getString(R.string.off));
			}
			else {
				fanModeTxt.setText(getString(IndoorDashboardUtils.getFanSpeedText(airPortInfo.getFanSpeed())));
			}
			filterStatusTxt.setText(IndoorDashboardUtils.getFilterStatus(airPortInfo));
			aqiPointer.setImageResource(IndoorDashboardUtils.getAqiPointerBackgroundId(indoorAqi));
			aqiStatusTxt.setTextSize(22.0f);
			aqiStatusTxt.setText(getString(IndoorDashboardUtils.getAqiTitle(indoorAqi)));
			aqiSummaryTxt.setText(getString(IndoorDashboardUtils.getAqiSummary(indoorAqi)));
			showIndoorMeter();
		}
		
		ALog.i(ALog.DASHBOARD, "currentPurifier.getConnectionState() " + purifier.getConnectionState());
		
		aqiPointer.invalidate();
		if(prevIndoorAqi != indoorAqi) {
			setRotationAnimation(aqiPointer, IndoorDashboardUtils.getAqiPointerRotation(indoorAqi));
		}
		prevIndoorAqi = indoorAqi;
	}

	private void setRotationAnimation(ImageView aqiPointer, float rotation) {
		Drawable drawable = aqiPointer.getDrawable();
		ALog.i(ALog.DASHBOARD, "IndoorFragment$getRotationAnimation rotation " + rotation + " aqiPointer.getWidth()/2 " + (aqiPointer.getWidth()/2) + " drawable " + drawable.getMinimumHeight());

		Animation aqiCircleRotateAnim = new RotateAnimation(prevRotation, rotation, drawable.getMinimumWidth() / (float) 2, drawable.getMinimumHeight() / (float) 2);
		
	    aqiCircleRotateAnim.setDuration(2000);
	    aqiCircleRotateAnim.setRepeatCount(0);
	    aqiCircleRotateAnim.setFillAfter(true);
	 
	    aqiPointer.setAnimation(aqiCircleRotateAnim);
	    prevRotation = rotation;
	}
	
	private void showFirmwareUpdatePopup() {
		if(View.VISIBLE != firmwareUpdatePopup.getVisibility()) {
			firmwareUpdatePopup.setVisibility(View.VISIBLE);
		}
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
		
		ALog.i(ALog.DASHBOARD, "IndoorDashboard$onDiscoveredDevicesListChanged");
		PurAirDevice current = PurifierManager.getInstance().getCurrentPurifier();
		if (current == null) return;
		
		ALog.i(ALog.DASHBOARD, "Purifier connection state " + current.getConnectionState());
		switch (current.getConnectionState()) {
		case CONNECTED_LOCALLY:
		case CONNECTED_REMOTELY:
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
//					showIndoorMeter();
					updateDashboardUI();
				}
			});
			break;
		case DISCONNECTED:
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
//					hideIndoorMeter();
					updateDashboardUI();
				}
			});
			break;
		}
	}
	
	@Override
	public void onAirPurifierEventReceived() {
		if (getActivity() == null) return;
		
		ALog.i(ALog.DASHBOARD, "IndoorFragment$onAirPurifierEventReceived");
		
		PurAirDevice purifier = ((MainActivity) getActivity()).getCurrentPurifier();
		if(purifier == null || purifier.getConnectionState() == ConnectionState.DISCONNECTED) {
			ALog.i(ALog.DASHBOARD, "onAirPurifierEventReceived ");
			return ;
		}
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				updateDashboardUI();
			}
		});
	}
	
	@Override
	public void onFirmwareEventReceived() {
		if (getActivity() == null || !(getActivity() instanceof MainActivity)) return;

		ALog.i(ALog.DASHBOARD, "IndoorFragment$onFirmwareEventReceived");
		
		PurAirDevice purifier = ((MainActivity) getActivity()).getCurrentPurifier();
		if (purifier == null) return;
		final FirmwarePortInfo firmwarePortInfo = purifier.getFirmwarePortInfo();
		if(firmwarePortInfo == null) return;
		
		updateFirmwareUI(purifier.getEui64(), firmwarePortInfo);
	}
	
	private String status = "";
	private boolean showFirmwareUI = true;
	
	private void updateFirmwareUI(String purifierEui64, FirmwarePortInfo firmwarePortInfo) {
		ALog.i(ALog.FIRMWARE, "updateFirmwareUI state " + firmwarePortInfo.getState());
		
		
		int progressVisibility = View.INVISIBLE;
		int progress = 0;
		int infoVisibility = View.INVISIBLE;
		
		switch (firmwarePortInfo.getState()) {
		case DOWNLOADING:
			showFirmwareUI = true;
			status = getString(R.string.downloading_new_firmware);
			progressVisibility = View.VISIBLE;
			progress = firmwarePortInfo.getProgress();
			infoVisibility = View.VISIBLE;
			break;
		case CHECKING:
			//Follow through
		case PROGRAMMING:
			showFirmwareUI = true;
			status = getString(R.string.installing_firmware_text);
			infoVisibility = View.VISIBLE;
			break;

		case ERROR:
//			if(null != dialogFragment && !dialogFragment.isVisible()) {
//				dialogFragment = AlertDialogFragment.newInstance(R.string.firmware_download_failed, R.string.firmware_failed_msg, R.string.ok, R.string.help);
//				dialogFragment.setOnClickListener(IndoorFragment.this);
//				dialogFragment.show(getActivity().getSupportFragmentManager(), getTag());
//			}
			showFirmwareUI = false;
			return ;
			
		case IDLE:
//			showFirmwareUI = false;
			int prevFirmwareVersion = 0, currentFirmwareVersion = 0; 
			try {
				prevFirmwareVersion = Integer.parseInt(Utils.getFirmwareVersion(purifierEui64));
				currentFirmwareVersion = Integer.parseInt(firmwarePortInfo.getVersion());
			} catch (NumberFormatException e) {
				ALog.e(ALog.FIRMWARE, "Error parsing firmware version " + e.getMessage());
			}
			
			ALog.i(ALog.FIRMWARE, "IDLE prevVersion " + prevFirmwareVersion + " currentVersion " + currentFirmwareVersion);
			
			if(prevFirmwareVersion < currentFirmwareVersion) {
				ALog.i(ALog.FIRMWARE, "Show firmware installed");
				status = getString(R.string.firmware_update) + " " + currentFirmwareVersion;
				showFirmwareUI = true;
				infoVisibility = View.INVISIBLE;
			}
			
			Utils.saveFirmwareVersion(purifierEui64, firmwarePortInfo.getVersion());
			break;

		case CANCELING:
		case READY:
			//NOP : Should not occur in Mandatory firmware update
			return ;
			
		default:
			return ;
		}
		updateFirmwareUI(showFirmwareUI, status,progressVisibility,progress,infoVisibility) ; 
	}
	private void updateFirmwareUI(final boolean showFirmwareUI, final String status, final int progressVisibility, final int progress, final int infoVisibility) {
		ALog.i(ALog.DASHBOARD, "IndoorFragment$updateFirmwareUI showFirmwareUI " + showFirmwareUI);
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(showFirmwareUI) {
					showFirmwareUpdatePopup();
					firmwareUpdateText.setText(status);
					firmwareProgress.setVisibility(progressVisibility);
					firmwareProgress.setProgress(progress);
					firmwareInfoButton.setVisibility(infoVisibility);
				} else {
					hideFirmwareUpdatePopup();
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.firmware_update_available:
			hideFirmwareUpdatePopup();
			break;
		case R.id.lbl_firmware_info:
			updateFirmwareUI(false, null, View.INVISIBLE, 0, View.INVISIBLE);
			
			firmwareInfoDialog = AlertDialogFragment.newInstance(R.string.firmware, R.string.firmware_download_info, R.string.ok);
			firmwareInfoDialog.show(getActivity().getSupportFragmentManager(), getTag());
			firmwareInfoDialog.setOnClickListener(new AlertDialogBtnInterface() {
				
				@Override
				public void onPositiveButtonClicked() {
					firmwareInfoDialog.dismiss();
				}
				
				@Override
				public void onNegativeButtonClicked() {
					//NOP
				}
			});
			break;
		case R.id.hf_indoor_circle_pointer:
			PurAirDevice purifier = ((MainActivity) getActivity()).getCurrentPurifier();
			if (getActivity() != null && purifier != null) {
				Intent intent = new Intent(getActivity(), IndoorDetailsActivity.class);
				startActivity(intent);
			} else {
				if (getActivity() != null) {
					Toast.makeText(getActivity(), 
							getString(R.string.purifier_not_connect_error), Toast.LENGTH_LONG).show();
				}
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onDrawerEvent(DrawerEvent event, View drawerView) {
		switch (event) {
		case DRAWER_CLOSED:
			break;
		case DRAWER_OPENED:
			hideFirmwareUpdatePopup();
			break;
		default:
			break;
		}
	}

	@Override
	public void onPositiveButtonClicked() {
		hideFirmwareUpdatePopup();
		dialogFragment.dismiss();
	}

	@Override
	public void onNegativeButtonClicked() {
		showSupportFragment();
	}

	private void showSupportFragment() {
		dialogFragment.dismiss();
		((MainActivity) getActivity()).showFragment(new SupportFragment());
	}

	@Override
	public void onErrorOccurred(PURIFIER_EVENT purifierEvent) {
		
	}
	

	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		
	}
	
	@Override
	public void onPageSelected(int position) {
		ALog.i(ALog.TEMP, "IndoorFragment$onPageSelected " + position);
		if(mActivity != null) {
			mActivity.setRightMenuVisibility(View.VISIBLE) ;
		}
		
		if( position >= DiscoveryManager.getInstance().getStoreDevices().size()) {
			if(mActivity != null) {
				mActivity.setRightMenuVisibility(View.INVISIBLE);
			}
			return;
		}
		if( position < DiscoveryManager.getInstance().getStoreDevices().size()) {
			PurAirDevice purifier = DiscoveryManager.getInstance().getStoreDevices().get(position);
			ALog.i(ALog.TEMP, "IndoorFragment$onPageSelected purifier from DB " + purifier);
			if (purifier == null) return;
	
			PurifierManager.getInstance().setCurrentPurifier(purifier) ;
		}
	}
}
